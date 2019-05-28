package edu.yu.oats.oatsdb.dbms.v0c;

import java.util.concurrent.TimeUnit;

class MyMaps<V, K> {
	MyMap<K,V> map;
	//Time time;


	public MyMaps(MyMap<K, V> myMap) {
		this.map = myMap;
		//this.time = new Time();
	}

}