package edu.yu.oats.oatsdb.dbms.v0b;

import java.util.Map;
import java.util.NoSuchElementException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import edu.yu.oats.oatsdb.dbms.ClientNotInTxException;
import edu.yu.oats.oatsdb.dbms.ConfigurableDBMS;

//11/10
public enum DBMSImpl implements ConfigurableDBMS {
	Instance;
	
	static int timeout = 200;
	private final static Logger logger = LogManager.getLogger(DBMSImpl.class);

	public  <K, V> Map<K, V> createMap(String arg0, Class<K> arg1, Class<V> arg2) {
		if(TxController.tl.get() == null) {
			throw new ClientNotInTxException("Thread not in Tx trying create map");
		}
		if(arg0 == null || arg1 == null || arg2 == null) {
			throw new IllegalArgumentException("Name can't be null");
		}
		if(arg0.trim().isEmpty()) {
			throw new IllegalArgumentException("Name can't be empty");
		}
		
		if(Database.database.get(arg0) != null) {
			throw new IllegalArgumentException("Map '" + arg0 + "' already created" );
		}
		if(TxController.tl.get().createTables.contains(arg0)){
			throw new IllegalArgumentException("Client already creating this map" );
		}
		
		MyMap<K, V> myMap = new MyMap<K, V>(arg0, arg1, arg2);
		MyMap<K, V> returnMap = myMap.MyMapCopy(myMap);
		TxThread txObject = TxController.tl.get();
		txObject.addCreateRequest(myMap);
		MyMaps m = new MyMaps(returnMap);
		TxController.tl.get().currentTables.put(arg0, m);	
		return (Map<K, V>) returnMap;
		
	}
	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> getMap(String arg0, Class<K> arg1, Class<V> arg2) {
	boolean found = false;
	logger.info("getting a map");
		if(TxController.tl.get()== null) {
			throw new ClientNotInTxException("Thread not in Tx trying to get map");
		}
		if(arg0 == null) {
			throw new IllegalArgumentException("name can't be null");
		}
		if(arg0.trim().isEmpty()) {
			throw new IllegalArgumentException("Name can't be empty");
		}
		
		
		MyMap<K,V> myMap = null;
		MyMaps<K,V> temp = TxController.tl.get().currentTables.get(arg0);

		if(temp!= null){
			found = true;
			myMap = (MyMap<K, V>) temp.map;// if already being used by this client then just spit the same map back. TODO ask on piazza
		}

		else if (myMap == null) {
			myMap = (MyMap<K, V>) Database.database.get(arg0);
		}
		if(myMap == null) {
				throw new NoSuchElementException("No map called " + arg0);
			}
		if(myMap.getKeyClass() != arg1) {
			throw new ClassCastException ("Key class does not match with map type");
		}
		if(myMap.getValueClass() != arg2) {
			System.out.println(myMap.getValueClass());
			System.out.println(arg2);
			throw new ClassCastException ("Value class does not match with map type");
		}
		if(found == true) {
			System.out.println(myMap);
			return myMap;
		}
				
	  
	
		
		
		MyMap<K, V> returnMap = myMap.MyMapCopy(myMap);
		MyMaps<K,V> myMaps = new MyMaps(returnMap);
		TxController.tl.get().currentTables.put(arg0, myMaps);
		
		return returnMap;
		
		
		
	}
	@Override
	public int getTxTimeoutInMillis() {
	
		return timeout;
	}
	
	
	@Override
	public void setTxTimeoutInMillis(int arg0) {
		timeout = arg0;
		
		
	}

}