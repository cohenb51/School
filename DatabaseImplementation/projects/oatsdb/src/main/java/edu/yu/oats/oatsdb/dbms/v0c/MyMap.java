package edu.yu.oats.oatsdb.dbms.v0c;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.yu.oats.oatsdb.dbms.ClientNotInTxException;
import edu.yu.oats.oatsdb.dbms.ClientTxRolledBackException;
import edu.yu.oats.oatsdb.dbms.RollbackException;
import edu.yu.oats.oatsdb.dbms.SystemException;

public class MyMap<K, V> implements Map<K, V> { // create another map to house the object we need to update
	boolean beingWrittento = false;
	protected String name;
	private Class<K> keyClass;
	private Class<V> valueClass;
	ConcurrentHashMap<K, V> map;
	private final static Logger logger = LogManager.getLogger(MyMap.class);
	public ConcurrentHashMap<String, MyLock> locks;
	public MyMap originalMap; // if this is null this is the original map
	boolean oldMap;

	private void checkIfOldMap() {
		if (TxController.tl.get().currentTables.get(this.name) == null) {
			MyMap<K,V> myMap = null;
			myMap = (MyMap<K, V>) Database.database.get(name);
			MyMaps<K,V> myMaps = new MyMaps(this);
			originalMap = myMap;
			TxController.tl.get().currentTables.put(name, myMaps);
			
			//throw new ClientNotInTxException("USING OLD MAP ILLEGALy");// An infamous line causing my code to blow up - kept for memories.  
		}
	}
	


	protected MyMap(String name, Class<K> keyClass, Class<V> valueClass) {
		map = new ConcurrentHashMap<>();
		this.name = name;
		this.setKeyClass(keyClass);
		this.setValueClass(valueClass);
		locks = new ConcurrentHashMap<>();

	}

	public MyMap() {
		// TODO Auto-generated constructor stub
	}

	public MyMap<K, V> MyMapCopy(MyMap<K, V> myMap) {
		ConcurrentHashMap<K, V> newMap = new ConcurrentHashMap<K, V>();
		newMap.putAll(myMap); // todo
		MyMap<K, V> newMyMap = new MyMap<K, V>();
		newMyMap.name = myMap.name;
		newMyMap.map = newMap;
		newMyMap.setKeyClass(myMap.getKeyClass());
		newMyMap.setValueClass(myMap.getValueClass());
		newMyMap.originalMap = myMap;
		TxThread TXObject = TxController.tl.get(); // no need to copy locks since we access the actual DB to get that
		return newMyMap;
	}

	public V put(K key, V value) {
		if (TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying put");
		}
		checkIfOldMap();

		if (key == null) {
			throw new IllegalArgumentException("key can't be null");
		}
		if (!(value instanceof Serializable)) {
			try {
				throw new SystemException("Value class not serializable");
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	

		V oldValue = get(key);
		keyClass.cast(key); // key must be same type or this will throw exception
		valueClass.cast(value);
		if (oldValue == null) { // create a lock for this key
			MyLock lock = new MyLock();
			originalMap.locks.put(key, lock);
			getLock(key, lock);
		}
		map.put(key, value);

		TxThread txObject = (TxThread) TxController.tl.get();
		txObject.addPutRequest(name, key, value);
		logger.debug(txObject.thread.toString() + ":Recorded put request for Key" + key);
		if (oldValue instanceof Buffer) {
			try {
				
				return (V) MySerializer.deSerialize((Buffer) oldValue);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		} else {
			return oldValue;
		}
		return null;

	}

	void quickPut(Object key, Object bos) {
		if (bos != null)
			map.put((K) key, (V) bos);
	}

	private Object myGet(Object key) {
		if (TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to GET");
		}
		if(originalMap == null) return null;
		checkIfOldMap();
		

		MyMap myMap = (MyMap<K, V>) Database.database.get(this.name);
		MyLock lock = (MyLock) originalMap.locks.get(key);
		V a = getLock(key, lock);

		logger.debug(TxController.tl.get().thread.toString() + ": Getting value for Key " + key);
		return a;

	}

	V quickGet(Object key) {
		return map.get(key);
	}

	public V get(Object key) {
		Object value = myGet(key);
		//System.out.println("INGET");
		if (value instanceof Buffer)
			try {
				V v = (V) MySerializer.deSerialize(( Buffer) value);
				TxThread txObject = (TxThread) TxController.tl.get();
				txObject.addPutRequest(name, key, v);
				if (v != null)
					this.quickPut(key, v); // quick put it so that we could modify the value
				return v;
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		else {
			//System.out.println("returning");
			return (V) value;
		}
		return null;
	}

	public void delete(K key) {
		if (TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to delete");
		}
		TxThread txObject = (TxThread) TxController.tl.get();
		MyLock lock = (MyLock) originalMap.locks.get(key);
		getLock(key, lock);

		logger.debug(TxController.tl.get().thread.toString() + ": Recording delete Request for" + key);
		txObject.addRemoveRequest(name, key);
	}

	@Override
	public int size() {
		if (TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on mapres");
		}
		return 0;
	}

	@Override
	public boolean isEmpty() {
		if (TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on map");
		}
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		if (TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on map");
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		if (TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying operate on map");
		}
		return false;
	}

	@Override
	public V remove(Object key) {
		if (TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on map");
		}
		checkIfOldMap();
		V oldValue = get(key);

		map.remove(key);
		TxController.tl.get().addRemoveRequest(name, key);
		return oldValue;
	}

	private void quickRemove(Object key) {
		map.remove(key);

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void putAll(Map m) {
		if (TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on map");
		}

	}

	@Override
	public void clear() {
		if (TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on map");
		}

	}

	@Override
	public Set<K> keySet() {
		if (TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on map");
		}
		return map.keySet();
	}

	@Override
	public Collection<V> values() {
		if (TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on map");
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Set entrySet() {
		if (TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying to operate on map");
		}
		return map.entrySet();
	}

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

	private V getLock(Object key, MyLock lock) {
		 logger.debug("Getting lock " + lock);
		if (key == null || lock == null) {
			return null;
		} // no locks on something not there

		if (lock.isHeldByCurrentThread()) {
			logger.debug("quick get"); 
			lock.lock();
			return (V) quickGet((K) key);
		}
		try { lock.tryLock(); }
		catch (Exception e) {
			try {
				TxController.tl.get().txmgr.rollback();
			} catch (IllegalStateException | SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw new ClientTxRolledBackException("Waiting for lock too long");
		}
		if (!lock.isHeldByCurrentThread()) {
			try {
				TxController.tl.get().txmgr.rollback();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			throw new ClientTxRolledBackException("Waiting for lock too long");
		}

		TxController.tl.get().locks.add(lock);
		logger.debug("GOT Lock");
		
		return (V) originalMap.quickGet(key);
		} /*catch (Exception e) {
			e.printStackTrace();
			try {
				TxController.tl.get().txmgr.rollback();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
          throw new ClientTxRolledBackException(" exception getting lock");
		}*/


	

	private boolean NoCommitSinceLastTime() {
		//System.out.println(this.lastCommitTime);
		//System.out.println(this.originalMap.lastCommitTime);
		//if(this.lastCommitTime == this.originalMap.lastCommitTime) {
			//System.out.println("returning true");
			//return true;
		//}
		//System.out.println("returning false");
		return false;
	}
}