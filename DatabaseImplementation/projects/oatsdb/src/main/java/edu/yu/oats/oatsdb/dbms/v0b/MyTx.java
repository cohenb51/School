package edu.yu.oats.oatsdb.dbms.v0b;
//11/10
import edu.yu.oats.oatsdb.dbms.SystemException;
import edu.yu.oats.oatsdb.dbms.Tx;
import edu.yu.oats.oatsdb.dbms.TxCompletionStatus;
import edu.yu.oats.oatsdb.dbms.TxStatus;

class MyTx implements Tx {
	
	protected TxStatus status = TxStatus.NO_TRANSACTION;
	protected TxCompletionStatus completionStatus = null;
	


	@Override
	public TxCompletionStatus getCompletionStatus() {

		return completionStatus;
	}

	@Override
	public TxStatus getStatus() throws SystemException {
		return status;
	}

}