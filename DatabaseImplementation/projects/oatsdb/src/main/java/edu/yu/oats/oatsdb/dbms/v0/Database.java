package edu.yu.oats.oatsdb.dbms.v0;

import java.util.concurrent.ConcurrentHashMap;

enum Database { Instance;
	
	static ConcurrentHashMap<String, MyMap<?, ?>> database = new ConcurrentHashMap<String, MyMap<?,?>>();
	
}