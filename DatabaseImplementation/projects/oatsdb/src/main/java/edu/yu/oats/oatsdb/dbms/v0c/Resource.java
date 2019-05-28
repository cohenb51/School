package edu.yu.oats.oatsdb.dbms.v0c;
//11/10
import java.io.ByteArrayOutputStream;
import java.util.concurrent.locks.ReentrantLock;

public class Resource {
	
	private ByteArrayOutputStream bos;

	public Resource(ByteArrayOutputStream bos) {
		this.bos = bos;
		ReentrantLock lock = new ReentrantLock();
	}
	

}