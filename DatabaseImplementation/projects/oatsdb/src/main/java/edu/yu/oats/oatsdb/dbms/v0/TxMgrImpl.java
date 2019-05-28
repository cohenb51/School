package edu.yu.oats.oatsdb.dbms.v0;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.yu.oats.oatsdb.dbms.NotSupportedException;
import edu.yu.oats.oatsdb.dbms.RollbackException;
import edu.yu.oats.oatsdb.dbms.SystemException;
import edu.yu.oats.oatsdb.dbms.Tx;
import edu.yu.oats.oatsdb.dbms.TxCompletionStatus;
import edu.yu.oats.oatsdb.dbms.TxMgr;
import edu.yu.oats.oatsdb.dbms.TxStatus;

public enum TxMgrImpl implements TxMgr {
	Instance;
	private final static Logger logger = LogManager.getLogger(TxMgrImpl.class);
	private MyTx tx = new MyTx();

	public void begin() throws NotSupportedException, SystemException {
		if (TxController.tl.get() != null) {
			throw new NotSupportedException("System does not support nested TXes");
		}

		TxController.startTX();
		tx.status = TxStatus.ACTIVE;
		tx.completionStatus = TxCompletionStatus.NOT_COMPLETED;
		logger.info("Begining Tax from thread " + TxController.tl.get().thread);

	}

	public void commit() throws RollbackException, IllegalStateException, SystemException {
		synchronized(Database.database) {
		if (TxController.tl.get() == null) {
			throw new IllegalStateException("Thread not in Tx trying to commit");
		}
		tx.status = TxStatus.COMMITTING;
		tx.completionStatus = null;
		logger.info("Commiting Tax from thread " + TxController.tl.get().thread);

		TxThread txObject = (TxThread) TxController.tl.get();
		logger.info("There are " + TxController.tl.get().actions.size() + " Actions");

		int createPointer = 0;
		int updatePointer = 0;
		int deletePointer = 0;

		for (int i = 0; i < txObject.actions.size(); i++) {
			if (txObject.actions.get(i) == Crud.CREATE) {
				MyMap<?, ?> createRequest = txObject.createRequests.get(createPointer);				
				createPointer++;
				Database.database.put(createRequest.name, createRequest);
				logger.info(txObject.thread.toString() + ": Created map " + createRequest.name);
			}

			if (txObject.actions.get(i) == Crud.UPDATE) {
				ModifyRequest<?, ?> modifyRequest = txObject.updateRequests.get(updatePointer);
				updatePointer++;
				MyMap<?, ?> map = Database.database.get(modifyRequest.getMapName());
				map.save(modifyRequest.getKey(), modifyRequest.getValue());
				logger.info(txObject.thread.toString() + ": Updated " + modifyRequest.getKey() + " in " + map.name);
				map.lastCommitTime = new Time();

			}
			if (txObject.actions.get(i) == Crud.DELETEKEY) {
				ModifyRequest<?, ?> modifyRequest = txObject.deleteKeyRequests.get(deletePointer);
				deletePointer++;
				MyMap<?, ?> map = Database.database.get(modifyRequest.getMapName());
				map.deleteKey(modifyRequest.getKey());
				logger.info(txObject.thread.toString() + ": Deleted " + modifyRequest.getKey() + " in " + map.name);
				map.lastCommitTime = new Time();


			}

		}

		TxController.tl.remove();
		tx.status = TxStatus.COMMITTED;
		tx.completionStatus = TxCompletionStatus.COMMITTED;
		tx = new MyTx();
		}
		logger.info("Exiting Commit");


	}

	public TxStatus getStatus() throws SystemException {
		return tx.status;
	}
	//When a 
	public Tx getTx() throws SystemException {
		if (TxController.tl.get() == null) {
			return new MyTx(); // after a thread commits, there is no more TX asscoiated with it so I return this. 
		}
		return tx;
	}

	public void rollback() throws IllegalStateException, SystemException {
		if (TxController.tl.get() == null) {
			throw new IllegalStateException("Thread not in Tx trying to rollback");
		}
		TxThread txObject = TxController.tl.get();
		int createPointer = 0;
		int updatePointer = 0;
		int deletePointer = 0;
		
		// I'm assuming clients must get a new map for each TX

			tx.status = TxStatus.ROLLING_BACK;
			tx.completionStatus = null;
			TxController.tl.remove();
			tx.status = TxStatus.ROLLEDBACK;
			tx.completionStatus = TxCompletionStatus.ROLLEDBACK;
			tx = new MyTx();

		}
	}