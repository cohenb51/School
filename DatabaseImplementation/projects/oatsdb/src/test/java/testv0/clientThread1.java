package testv0;

import java.util.Map;
import java.util.NoSuchElementException;

import edu.yu.oats.oatsdb.dbms.ClientNotInTxException;
import edu.yu.oats.oatsdb.dbms.DBMS;
import edu.yu.oats.oatsdb.dbms.NotSupportedException;
import edu.yu.oats.oatsdb.dbms.OATSDBType;
import edu.yu.oats.oatsdb.dbms.Tx;
import edu.yu.oats.oatsdb.dbms.TxCompletionStatus;
import edu.yu.oats.oatsdb.dbms.TxMgr;
import edu.yu.oats.oatsdb.dbms.TxStatus;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class clientThread1 extends Thread { // Tests basic getting and retriving values

	public void run() {
		DBMS dbms = null;
		try {
			dbms = OATSDBType.dbmsFactory(OATSDBType.V0);
			TxMgr txmgr = OATSDBType.txMgrFactory(OATSDBType.V0);
			Tx tx = txmgr.getTx();
		
			
			assertEquals(null, tx.getCompletionStatus());
			assertEquals(TxStatus.NO_TRANSACTION, tx.getStatus());
			txmgr.begin();
			tx = txmgr.getTx();
			assertEquals(TxStatus.ACTIVE, tx.getStatus());
			assertEquals(TxCompletionStatus.NOT_COMPLETED, tx.getCompletionStatus());

			
			Map studentsMap = dbms.createMap("Students", String.class, Student.class);
			Student student1 = new Student("Benny", 2.0);
			Student student2 = new Student("Yair", 3.0);
			Student student3 = new Student("Jose", 3.2);
			Student student4 = new Student("Guy", 4.1);
			studentsMap.put("student1", student1);
			studentsMap.put("student2", student2);
			studentsMap.put("student3", student3);
			studentsMap.put("student2", student4);
			Student testStudent0 = (Student) studentsMap.get("student2");
			assertEquals("Guy", testStudent0.getName());
			txmgr.commit();
			assertEquals(TxStatus.COMMITTED, tx.getStatus());
			assertEquals(TxCompletionStatus.COMMITTED, tx.getCompletionStatus());
			assertEquals(null, txmgr.getTx().getCompletionStatus());


			
			txmgr.begin();
			Map<String,Student> testMap = dbms.getMap("Students", String.class, Student.class);
			Student testStudent1 = testMap.get("student1");
			assertEquals("Benny", testStudent1.getName());
			Student testStudent2 = (Student) testMap.get("student2");
			assertEquals((Double)4.1, testStudent2.getGPA());
			testMap.remove("student1"); 
			testMap.remove("student2"); 
			Student Bob = new Student("Bob", 3.0);
			testMap.put("student3", Bob);
			Student testStudent5 = (Student) testMap.get("student3");
			assertEquals(Bob, testStudent5);



			Student testStudent3 = (Student) testMap.get("student1");
			Student testStudent4 = (Student) testMap.get("student2");

			assertEquals(null, testStudent3);
			assertEquals(null, testStudent4);

			txmgr.commit();
			txmgr.begin();
			testMap = dbms.getMap("Students", String.class, Student.class);
			testStudent3 = (Student) testMap.get("student1");
			testStudent4 = (Student) testMap.get("student2");
			testStudent5 = (Student) testMap.get("student3");


			assertEquals(null, testStudent3);
			assertEquals(null, testStudent4);
			assertEquals(Bob, testStudent5);


			txmgr.commit();
			
			try{ dbms.createMap("should not work", Student.class, Student.class);
			assertEquals(0, 1);
			} catch (ClientNotInTxException e) {}
			try { 
				dbms.getMap("Students", String.class, Student.class);			assertEquals(0, 1);
 	
			}
			catch (ClientNotInTxException e) {}
			try {testMap.put("Student6", student4);			assertEquals(0, 1);
}
			catch (ClientNotInTxException e) {;			
}
			try {testMap.remove("Student6");			assertEquals(0, 1);
}
			catch (ClientNotInTxException e) {}
			try {testMap.get("Student6");			assertEquals(0, 1);
}
			catch (ClientNotInTxException e) {}
			
			txmgr.begin();
			try { dbms.createMap("Students", String.class, String.class);assertEquals(0, 1);
}
			catch(IllegalArgumentException e) {}
			try {dbms.getMap("non", String.class, Student.class);assertEquals(0, 1); }
			catch  (NoSuchElementException e) {}
			try {dbms.getMap("Students", Integer.class, Student.class);assertEquals(0, 1); }
			catch  (ClassCastException e) {}
			try {dbms.getMap("Students", String.class, Integer.class);assertEquals(0, 1); }
			catch  (ClassCastException e) {}
			try { txmgr.begin();assertEquals(0, 1);} 
			catch(NotSupportedException e){}
			
			testMap = dbms.getMap("Students", String.class, Student.class);
			testMap.put("student6", student1);
			tx = txmgr.getTx();
			txmgr.rollback();
			assertEquals(null, txmgr.getTx().getCompletionStatus());
			assertEquals(TxStatus.NO_TRANSACTION, txmgr.getTx().getStatus());

			assertEquals(TxCompletionStatus.ROLLEDBACK, tx.getCompletionStatus());
			assertEquals(TxStatus.ROLLEDBACK, tx.getStatus());
			

			



			
			
			try {dbms.getMap("Student", String.class, Student.class);assertEquals(0, 1); }
			catch  (ClientNotInTxException e) {}
			
			
			txmgr.begin();
			testMap = dbms.getMap("Students", String.class, Student.class);
			Student student6 = testMap.get("student6");
			//assertEquals(null, student6);
			try{ dbms.createMap("money", String.class, String.class);
			dbms.createMap("money", String.class, String.class);assertEquals(0, 1);}
			catch (IllegalArgumentException e) {}

			txmgr.rollback();
			txmgr.begin();
			//try { Map<String, String> testMap2 = dbms.getMap("money", String.class, String.class);assertEquals(0, 1); }
			//catch (NoSuchElementException e) {}
			try { Map<String, String> testMap2 = dbms.createMap(null, String.class, String.class);assertEquals(0, 1); }
			catch (IllegalArgumentException e) {}
			txmgr.commit();
			

			
			
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			assertEquals(0, 1);
		
		}
	}
}