package edu.yu.oats.oatsdb.dbms.v0b;

import java.util.concurrent.ConcurrentHashMap;
//11/10
public enum Database { Instance;
	
	public static ConcurrentHashMap<String, MyMap<?, ?>> database = new ConcurrentHashMap<String, MyMap<?,?>>();
	public static ConcurrentHashMap<String, MyMap<?, ?>> tempdb = new ConcurrentHashMap<String, MyMap<?,?>>();

}