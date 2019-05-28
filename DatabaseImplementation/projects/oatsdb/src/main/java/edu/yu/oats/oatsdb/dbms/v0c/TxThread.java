package edu.yu.oats.oatsdb.dbms.v0c;
//11/10
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// for create/delete we need a name of a map
//for update we need name of a map, 

class TxThread {
	@SuppressWarnings("unused")
	private final static Logger logger = LogManager.getLogger(TxThread.class);

	Thread thread;
	LinkedList<Crud> actions; // create a list of the order of operations needed to get done. Take off the request lists in this order.
	HashMap<String, MyMaps> currentTables;
	ArrayList<ModifyRequest<?,?>> deleteKeyRequests;
	ArrayList<ModifyRequest<?,?>> updateRequests;
	ArrayList<ModifyRequest<?,?>> readRequests;
	ArrayList<MyMap<?,?>> createRequests;
	HashSet<String> createTables;
	int timeOut;
	TxMgrImpl txmgr;
	ArrayList<MyLock> locks;


	
	public TxThread(Thread thread) {
		this.thread = thread;
		currentTables = new HashMap<String, MyMaps>();
		createTables = new HashSet<String>();
		actions = new LinkedList<>();
		deleteKeyRequests = new ArrayList<>();
		updateRequests = new ArrayList<>();
		createRequests = new ArrayList<>();
		readRequests = new ArrayList<>();
		locks = new ArrayList<>();
	}
	
	public <K, V> void addPutRequest(String mapName, K key, V value) {
		actions.add(Crud.UPDATE);
		ModifyRequest<K,V> mr = new ModifyRequest<K, V>(mapName, key, value);
		updateRequests.add(mr);
	}
	
	public <K, V> void addReadRequest(String mapName, K key, V value) {
		actions.add(Crud.READ);
		ModifyRequest<K,V> mr = new ModifyRequest<K, V>(mapName, key, value);
		readRequests.add(mr);
	}
	
	
	public void addCreateRequest(MyMap<?, ?> map){
		actions.add(Crud.CREATE);
		createRequests.add(map);
		createTables.add(map.name);
	}


	public <V, K> void addRemoveRequest(String mapName, K key) {
		actions.add(Crud.DELETEKEY);
		ModifyRequest<K, V> mr = new ModifyRequest<K, V>(mapName, key, null);
		deleteKeyRequests.add(mr);
		
	}

	public Object deleteKeyRequests() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}