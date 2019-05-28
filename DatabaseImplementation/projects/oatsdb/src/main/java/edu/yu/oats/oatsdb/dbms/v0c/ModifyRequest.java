package edu.yu.oats.oatsdb.dbms.v0c;

//11/10
class ModifyRequest<K, V> {
	
	private String mapName;
	private K key;
	private V value;
	
	
	protected ModifyRequest(String mapName, K key, V value) {
		this.mapName = mapName;
		this.key = key;
		this.value = value;
		
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