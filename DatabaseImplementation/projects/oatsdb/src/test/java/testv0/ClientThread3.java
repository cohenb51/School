package testv0;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import edu.yu.oats.oatsdb.dbms.DBMS;
import edu.yu.oats.oatsdb.dbms.OATSDBType;
import edu.yu.oats.oatsdb.dbms.TxMgr;

public class ClientThread3 extends Thread{ 
	
	public synchronized void run() {
		try {
			
		
		DBMS dbms = OATSDBType.dbmsFactory(OATSDBType.V0);
		TxMgr txmgr = OATSDBType.txMgrFactory(OATSDBType.V0);
		txmgr.begin();
	
		Map<String,Student> testMap = dbms.getMap("Students", String.class, Student.class);
		wait(1000);

		Student Jeff = testMap.get("Helen");
		assertEquals("Helen", Jeff.getName());
		testMap.put("Helen", new Student("news", 2.0));
		Jeff = testMap.get("Helen");
		assertEquals("news", Jeff.getName());
		testMap.remove("Helen");
		Jeff = testMap.get("Helen");
		assertEquals(null, Jeff);
		testMap.put("Helen", new Student("Helen", 2.0));
		Jeff = testMap.get("Helen");
		assertEquals("Helen", Jeff.getName());
		testMap.remove("Helen");
		Jeff = testMap.get("Helen");
		assertEquals(null, Jeff);
		
		testMap.put("Helen", new Student("news", 2.0));
		Jeff = testMap.get("Helen");
		assertEquals("news", Jeff.getName());
		testMap.put("Helen", new Student("news1", 2.0));
		Jeff = testMap.get("Helen");
		assertEquals("news1", Jeff.getName());
		
		
		



		

		txmgr.commit();
		
		
	
		}
		
		catch (Exception e){
			e.printStackTrace();
			
	}
	}

}
