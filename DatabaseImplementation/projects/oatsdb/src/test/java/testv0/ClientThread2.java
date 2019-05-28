package testv0;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import edu.yu.oats.oatsdb.dbms.DBMS;
import edu.yu.oats.oatsdb.dbms.NotSupportedException;
import edu.yu.oats.oatsdb.dbms.OATSDBType;
import edu.yu.oats.oatsdb.dbms.RollbackException;
import edu.yu.oats.oatsdb.dbms.SystemException;
import edu.yu.oats.oatsdb.dbms.TxMgr;

public class ClientThread2 extends Thread{

	public synchronized void run() {
		try {
			
		
		DBMS dbms = OATSDBType.dbmsFactory(OATSDBType.V0);
		TxMgr txmgr = OATSDBType.txMgrFactory(OATSDBType.V0);
		txmgr.begin();
	
		
		Map testMap = dbms.getMap("Students", String.class, Student.class);
		Student putStudent = new Student("Helen" , 2.0);
		testMap.put("Helen", putStudent);
		txmgr.commit();

		
	
		}
		
		catch (Exception e){
			e.printStackTrace();
			assertEquals(0, 1);

			
	}
	}
}
