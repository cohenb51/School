package testv0;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import edu.yu.oats.oatsdb.dbms.DBMS;
import edu.yu.oats.oatsdb.dbms.NotSupportedException;
import edu.yu.oats.oatsdb.dbms.OATSDBType;
import edu.yu.oats.oatsdb.dbms.RollbackException;
import edu.yu.oats.oatsdb.dbms.SystemException;
import edu.yu.oats.oatsdb.dbms.TxMgr;

public class ClientThread7 extends Thread{

	public synchronized void run() {
		try {
			
		
		DBMS dbms = OATSDBType.dbmsFactory(OATSDBType.V0);
		TxMgr txmgr = OATSDBType.txMgrFactory(OATSDBType.V0);
		txmgr.begin();
		wait(1000);
		Map testMap = dbms.getMap("numbers", String.class, Integer.class);
		System.out.println(testMap);

		
	
		}
		
		catch (Exception e){
			e.printStackTrace();
			assertEquals(0, 1);

			
	}
	}
}
