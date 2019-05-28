package edu.yu.oats.oatsdb.dbms.v0b;

import java.util.concurrent.TimeUnit;

class Time {
	
	long ms;
	long us;
	
	public Time() {
		try {
			TimeUnit.NANOSECONDS.sleep(1); // we must garentee that these times are diferent
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ms = System.currentTimeMillis();
		us = System.nanoTime();
	}
	

	
	static boolean isAfter(Time time1, Time time2) {
		if(time1.ms == time2.ms) {
			return (time2.us - time1.us > 0);
		}
		return (time2.ms -time1.ms) > 0;
	}

}