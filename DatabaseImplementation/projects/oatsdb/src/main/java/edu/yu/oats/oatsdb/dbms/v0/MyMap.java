package edu.yu.oats.oatsdb.dbms.v0;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.yu.oats.oatsdb.dbms.ClientNotInTxException;

class MyMap<K,V> implements Map<K,V> { // create another map to house the object we need to update
	boolean beingWrittento = false;
	protected String name;
	private Class<K> keyClass;
	private Class<V> valueClass;
	ConcurrentHashMap<K, V> map;
	private final static Logger logger = LogManager.getLogger(MyMap.class);
	Time lastCommitTime;
	

	
	private void checkIfOldMap() {
		if(TxController.tl.get().currentTables.get(this.name) == null) {
			 MyMap<K, V> myMap = (MyMap<K, V>) Database.database.get(this.name); // does the same thing as dbms.getMap
			 MyMap<K, V> returnMap = myMap.MyMapCopy(myMap);
			 this.map = returnMap.map;
			 MyMaps<K,V> myMaps = new MyMaps(this);
		     TxController.tl.get().currentTables.put(this.name, myMaps); //start tracking
			 
		}
	}
	
	protected MyMap(){
		
		
	}
	
	
	
	protected MyMap(String name, Class<K> keyClass, Class<V> valueClass) {
		map = new ConcurrentHashMap<>();
		this.name = name;
		this.setKeyClass(keyClass);
		this.setValueClass(valueClass);
		lastCommitTime = new Time();

	}
	
	public  MyMap<K, V> MyMapCopy(MyMap<K,V> myMap) { //One day use this to deep copy the map
		ConcurrentHashMap<K, V> newMap = new ConcurrentHashMap<K,V>();	
		newMap.putAll(myMap); //todo
		MyMap<K,V> newMyMap = new MyMap<K,V>();
		newMyMap.name = myMap.name;
		newMyMap.map = newMap;
		newMyMap.setKeyClass(myMap.getKeyClass());
		newMyMap.setValueClass(myMap.getValueClass());	
		TxThread TXObject = TxController.tl.get();
		return newMyMap;
	}
	
	public V put(K key, V value) {
		if(TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying put");
		}
		checkIfOldMap();
		if(key == null) {
			throw new IllegalArgumentException("key can't be null");
		}
		V oldValue = get(key);
		keyClass.cast(key); // key must be same type or this will throw exception
		valueClass.cast(value);
		map.put(key, value);
		TxThread txObject = (TxThread) TxController.tl.get();
		txObject.addPutRequest(name, key, value);
		logger.info(txObject.thread.toString() + ":Recorded put request for Key" + key);
		return oldValue;
		
	}
	
	public V get(Object key){ 
		if(TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to GET");
		}
		checkIfOldMap();

		MyMap myMap = (MyMap<K, V>) Database.database.get(this.name);
		Boolean CommitedToAfterGot = checkTime(myMap);
		if(CommitedToAfterGot) {
			logger.info("Detected that another client wrote to the db after you got the map");
			return getFromMeOrOriginal(key, myMap.lastCommitTime);
			
		}
		V a = map.get(key);
		logger.info(TxController.tl.get().thread.toString() + ": Getting value for Key " + key);
		return a;
	}
	
	private V getFromMeOrOriginal(Object key, Time lastCommitTime) {
		boolean Deleted = false;
		//If I am in the original Database and I didn't delete it yet
		MyMap myMap = (MyMap<K, V>) Database.database.get(this.name);
		ModifyRequest<?, ?> IDeletedIt = checkIfDeleted(key);
		ModifyRequest<?, ?> IPutIt = checkIfPut(key);
		if(IDeletedIt != null) Deleted = true;
		if(myMap.getOriginal(key) != null && !Deleted && IPutIt ==null ){
			logger.info("detected that another client put something here and I didn't delete it");
			return (V) myMap.getOriginal(key);
		}
		else if(myMap.getOriginal(key) != null && Deleted){
			if(deletedAfterCommit(IDeletedIt, lastCommitTime)) {
				//we need to check that I didn't put something here since the time i deleted it
				if(IPutIt != null) {
					if(IPutAfterDelete(IPutIt, IDeletedIt)) {
						return (V) getOriginal(key);
					}
				}
				logger.info("detected that another client put something here but I deleted it after");
				return null;
			}
		}
		
		//else they are not equal  meaning that last commit deleted/updated it or I put it in here/updatedit
		 if(myMap.getOriginal(key) != this.getOriginal(key)) {

			if(IPutIt != null) {
				if(putAfterCommit(IPutIt, lastCommitTime)){
					logger.info("detected that I put something here after the last commit");
					// we need to check that I didn't delete this after I put it here
					if(IDeletedIt != null) {
						if(IDeleteAfterPut(IPutIt, IDeletedIt)) {
						logger.info("detected that my last opetation with this key was a put");
						return null;
						}
					}
					return (V) getOriginal(key);
				}
			}
			
			// I did not put it here.
			else { 
				logger.info("detected that someone else did something with this key since the time client took the map and did its last update with this key");
				return (V) myMap.getOriginal(key);
			}
		}
// if we get to here they should be equal
		logger.info("detected equality");

		return (V) getOriginal(key);
	}
	
	
	
	
	private boolean IPutAfterDelete(ModifyRequest<?, ?> iPutIt, ModifyRequest<?, ?> iDeletedIt) {
		return Time.isAfter(iDeletedIt.time, iPutIt.time);
		
	}



	private boolean IDeleteAfterPut(ModifyRequest<?, ?> iPutIt, ModifyRequest<?, ?> iDeletedIt) {
		return Time.isAfter(iPutIt.time, iDeletedIt.time);
	}



	private ModifyRequest<?, ?> checkIfPut(Object key) {
		ArrayList<ModifyRequest<?, ?>> a = TxController.tl.get().updateRequests;
		if(a.size() == 0) return null;
		for(int i = a.size()-1; i>= 0; i--) {
			ModifyRequest<?, ?> b = a.get(i);
			if(b.getKey().equals(key)){
				return b;
			}
		}
		return null;
	
	
	}

	private boolean putAfterCommit(ModifyRequest iPutIt, Time lastCommitTime) {
		Time time1 = iPutIt.time;
		return Time.isAfter(lastCommitTime, time1);
		
	}



	private Object getOriginal(Object key) {
		return map.get(key);
	}



	private boolean checkTime(MyMap myMap) {
		if (myMap == null) {
			return false; // wasn't commited yet
		}
		Time commitTime = myMap.lastCommitTime;
		Time gotTime;
		MyMaps temp = TxController.tl.get().currentTables.get(name);
		if(temp != null) {
			gotTime = temp.time;
		}
		else {
			gotTime = TxController.tl.get().time;
		}
		return Time.isAfter(gotTime, commitTime);

	}


	private boolean deletedAfterCommit(ModifyRequest<?, ?> iDeletedIt, Time lastCommitTime) {
		Time time1 = iDeletedIt.time;
		return Time.isAfter(lastCommitTime, time1);
		
		
		
	}



	private ModifyRequest<?, ?> checkIfDeleted(Object key) {
		ArrayList<ModifyRequest<?, ?>> a = TxController.tl.get().deleteKeyRequests;
		if(a.size() == 0) return null;
		for(int i = a.size()-1; i >= 0; i--) {
			ModifyRequest<?, ?> b = a.get(i);
			if(b.getKey().equals(key)){
				return b;
			}
		}
		return null;
	}




	
	public void delete(K key) {
		if(TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to delete");
		}
		TxThread txObject = (TxThread) TxController.tl.get();

		logger.info(TxController.tl.get().thread.toString() + ": Recording delete Request for" + key);
		txObject.addRemoveRequest(name, key);
	}

	@Override
	public int size() {
		if(TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on mapres");
		}
		return 0;
	}

	@Override
	public boolean isEmpty() {
		if(TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on map");
		}
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		if(TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on map");
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		if(TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying operate on map");
		}
		return false;
	}



	@Override
	public V remove(Object key) {
		if(TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on map");
		}
		checkIfOldMap();

		V oldValue = get(key);

		map.remove(key);
		TxController.tl.get().addRemoveRequest(name, key);
		return oldValue;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void putAll(Map m) {
		if(TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on map");
		}
		
	}

		


	@Override
	public void clear() {
		if(TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on map");
		}
		
	}

	@Override
	public Set<K> keySet() {
		if(TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on map");
		}
		return map.keySet();
	}

	@Override
	public Collection<V> values() {
		if(TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on map");
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Set entrySet() {
		if(TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on map");
		}
		return map.entrySet();
	}

	@SuppressWarnings("unchecked")
	protected void save(Object object, Object object2) {
		map.put((K) object, (V) object2);
	}

	public Class<K> getKeyClass() {
		return keyClass;
	}

	public void setKeyClass(Class<K> keyClass) {
		this.keyClass = keyClass;
	}

	public Class<V> getValueClass() {
		return valueClass;
	}

	public void setValueClass(Class<V> valueClass) {
		this.valueClass = valueClass;
	}

	public void deleteKey(Object key) {
		map.remove(key);
		
	}


	
	
	
	

}