package testv0;

import java.io.IOException;
import java.util.Map;
import javax.xml.bind.JAXBException;



import edu.yu.oats.oatsdb.dbms.DBMS;
import edu.yu.oats.oatsdb.dbms.NotSupportedException;
import edu.yu.oats.oatsdb.dbms.OATSDBType;
import edu.yu.oats.oatsdb.dbms.RollbackException;
import edu.yu.oats.oatsdb.dbms.SystemException;
import edu.yu.oats.oatsdb.dbms.Tx;
import edu.yu.oats.oatsdb.dbms.TxMgr;

public class Request extends Thread {
	
	
	
	public static void main(String[] args) throws  InstantiationException, NotSupportedException, SystemException, IllegalStateException, RollbackException, IOException, ClassNotFoundException, JAXBException, InterruptedException {
		DBMS dbms = OATSDBType.dbmsFactory(OATSDBType.V0);
		TxMgr txmgr = OATSDBType.txMgrFactory(OATSDBType.V0);
		
		
		Tx tx = txmgr.getTx();
		
		txmgr.begin();
		tx = txmgr.getTx();
		Map<Integer, String> m = dbms.createMap("numbers", Integer.class, String.class);
		Map<Student, String> o = dbms.createMap("numb", Student.class, String.class);

		System.out.println(tx.getCompletionStatus());
		System.out.println(tx.getStatus());
		m.put(1, "one");
		m.put(2, "two");
		m.put(1, "three");
		m.put(10, "ten");
		m.remove(2);
		txmgr.commit();
		txmgr.begin();
		tx = txmgr.getTx();
		
		m.put(1, "one");
		txmgr.commit();
		txmgr.begin();
		m.put(1, "");
		System.out.println(o);
		txmgr.rollback();
		txmgr.begin();
		txmgr.begin();
		


		


		//txmgr.rollback();
		//o = dbms.createMap("numb", Student.class, String.class);

		

		
		
	
		
		
		
		
		
	
		
		clientThread1 client1 = new clientThread1();
		ClientThread2 client2 = new ClientThread2();
		ClientThread3 client3 = new ClientThread3();
	    /*dbms.createMap("numbers", Integer.class, String.class);

		
		client1.start();
		client1.join();
		
		System.out.println("_____________________");
		System.out.println("_____________________");
		System.out.println("_____________________");
		System.out.println("_____________________");

		
		client2.start();
		client2.yield();
		System.out.println("HELLO PERSON");
		client3.start();
		client2.join();
		client3.join();
		System.out.println("_____________________");
		System.out.println("_____________________");
		System.out.println("_____________________");
		System.out.println("_____________________");
		
		ClientThread4 client4 = new ClientThread4();
		ClientThread5 client5 = new ClientThread5();
		client4.start();
		client4.yield();
		client5.start();
		System.out.println("_____________________");
		System.out.println("_____________________");
		System.out.println("_____________________");
		System.out.println("_____________________");
		clientThread6 client6 = new clientThread6();
		ClientThread7 client7 = new ClientThread7();
		
		client6.start();
		client7.start();
	*/
		
		
		

		
		
	}

		

	
	
}
	            
	         

	    

