package edu.yu.oats.oatsdb.dbms.v0c;

import java.io.IOException;
import java.io.ObjectInputStream;

public class Buffer {
	Object obj = null;
	ObjectInputStream stream;
	
	public Buffer(ObjectInputStream stream) {
		this.stream = stream;
	}

	Object getObject() throws ClassNotFoundException, IOException {
		if(obj == null) {
			obj = stream.readObject();
			return obj;
		}

		return obj;
	}

}