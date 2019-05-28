package edu.yu.oats.oatsdb.dbms.v0b;
//11/10
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MySerializer {
	
/*	public static byte[] serialize(Object object) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();//source https://howtodoinjava.com/java/serialization/how-to-do-deep-cloning-using-in-memory-serialization-in-java/
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(object);  
        return bos.toByteArray();
		
	}*/
	
	public static Buffer serialize(Object object) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();//source https://howtodoinjava.com/java/serialization/how-to-do-deep-cloning-using-in-memory-serialization-in-java/
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(object); 
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        Buffer buf = new Buffer(in);
        return buf;
		
	}
	 
	public static Object deSerialize(Buffer in ) throws IOException, ClassNotFoundException {
		if(in == null) return null;
        return in.getObject();
        
 	} 
}