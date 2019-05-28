package edu.yu.oats.oatsdb.dbms.v0;


class ModifyRequest<K, V> {
	
	private String mapName;
	private K key;
	private V value;
	Time time;
	
	
	protected ModifyRequest(String mapName, K key, V value) {
		this.mapName = mapName;
		this.key = key;
		this.value = value;
		this.time = new Time();
		
	}

	protected String getMapName() {
		return mapName;
	}

	protected K getKey() {
		return key;
	}
	protected V getValue() {
		return value;
	}


	
	

}