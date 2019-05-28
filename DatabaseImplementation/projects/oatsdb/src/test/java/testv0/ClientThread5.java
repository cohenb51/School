package testv0;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import edu.yu.oats.oatsdb.dbms.DBMS;
import edu.yu.oats.oatsdb.dbms.OATSDBType;
import edu.yu.oats.oatsdb.dbms.TxMgr;

public class ClientThread5 extends Thread{ 
	
	public synchronized void run() {
		try {
			
		
		DBMS dbms = OATSDBType.dbmsFactory(OATSDBType.V0);
		TxMgr txmgr = OATSDBType.txMgrFactory(OATSDBType.V0);
		txmgr.begin();
	
		Map<String,Student> testMap = dbms.getMap("Students", String.class, Student.class);
		wait(1000);

		Student Jeff = testMap.get("student3");
		assertEquals(null,Jeff);
		testMap.put("student3", new Student("student3", 2.0));
		Jeff = testMap.get("student3");
		assertEquals("student3", Jeff.getName());
		testMap.remove("student3");
		testMap.put("student3", new Student("student5", 2.0));
		testMap.put("student3", new Student("student7", 2.0));
		Jeff = testMap.get("student3");
		assertEquals("student7",Jeff.getName());
		testMap.remove("student3");
		Jeff = testMap.get("Jeff");
		assertEquals(null,Jeff);

		
		


		
		

		

		txmgr.commit();
		
		
	
		}
		
		catch (Exception e){
			e.printStackTrace();
			
	}
	}

}
