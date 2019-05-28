package edu.yu.oats.oatsdb.dbms.v0;

import java.util.Collections;
import java.util.Map;

class Token<K,V> {
	Class<K> key;
	Class<V> val;
	String name;

	public Token(String name, Class<K> key, Class<V> val) {
		this.name = name;
		this.key = key;
		this.val = val;
	}
	
	public Map<K, V> cast(Object obj) {
		Map map = (Map) obj;
		System.out.println("calling cast");
		return Collections.checkedMap(map,key,val);		
		
	}
	@Override
	public boolean equals(Object token) {
		return this.name.equals(((Token) token).getName());
		
	}
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	public String getName() {
		return this.name;
	}

}
