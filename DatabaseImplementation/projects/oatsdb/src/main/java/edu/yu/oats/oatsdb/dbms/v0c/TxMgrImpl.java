package edu.yu.oats.oatsdb.dbms.v0c;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

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

	public synchronized void begin() throws NotSupportedException, SystemException {
		if (TxController.tl.get() != null) {
			throw new NotSupportedException("System does not support nested TXes");
		}
		TxController.startTX();
		tx.status = TxStatus.ACTIVE;
		tx.completionStatus = TxCompletionStatus.NOT_COMPLETED;
		logger.debug("Begining Tax from thread " + TxController.tl.get().thread);
		TxController.tl.get().txmgr = this;
	

	}

	public synchronized void commit() throws RollbackException, IllegalStateException, SystemException {
		{
			if (TxController.tl.get() == null) {
				throw new IllegalStateException("Thread not in Tx trying to commit");
			}

			tx.status = TxStatus.COMMITTING;
			tx.completionStatus = null;
			logger.debug("Commiting Tax from thread " + TxController.tl.get().thread);

			TxThread txObject = (TxThread) TxController.tl.get();
			logger.debug("There are " + TxController.tl.get().actions.size() + " Actions");

			int createPointer = 0;
			int updatePointer = 0;
			int deletePointer = 0;
			int readPointer = 0;

			for (int i = 0; i < txObject.actions.size(); i++) {
				if (txObject.actions.get(i) == Crud.CREATE) {
					MyMap<?, ?> createRequest = txObject.createRequests.get(createPointer);
					createPointer++;
					Database.database.put(createRequest.name, createRequest);
					logger.debug(txObject.thread.toString() + ": Created map " + createRequest.name);
				}

				if (txObject.actions.get(i) == Crud.UPDATE) {
					ModifyRequest<?, ?> modifyRequest = txObject.updateRequests.get(updatePointer);
					updatePointer++;
					MyMap<?, ?> map = Database.database.get(modifyRequest.getMapName());
					try {
						Buffer bos = MySerializer.serialize(modifyRequest.getValue());
						map.save(modifyRequest.getKey(), bos);



					} catch (NotSerializableException e) {
						unlockLocks();
						throw new SystemException("Can't serialize value class ");
					} catch (IOException e) {
						unlockLocks();
						throw new SystemException("Error with input and output");
					}

					logger.debug(txObject.thread.toString() + ": Updated " + modifyRequest.getKey() + " in " + map.name);

				}

				if (txObject.actions.get(i) == Crud.READ) { // same as above but used for testing
					ModifyRequest<?, ?> modifyRequest = txObject.updateRequests.get(updatePointer);
					updatePointer++;
					MyMap<?, ?> map = Database.database.get(modifyRequest.getMapName());
					try {
						Buffer bos = MySerializer.serialize(modifyRequest.getValue());
						map.save(modifyRequest.getKey(), bos);
					} catch (IOException e) {
						throw new IllegalArgumentException("error commiting");
					}

					logger.debug(txObject.thread.toString() + ": Read " + modifyRequest.getKey() + " in " + map.name);

				}

				if (txObject.actions.get(i) == Crud.DELETEKEY) {
					ModifyRequest<?, ?> modifyRequest = txObject.deleteKeyRequests.get(deletePointer);
					deletePointer++;
					MyMap<?, ?> map = Database.database.get(modifyRequest.getMapName());
					map.deleteKey(modifyRequest.getKey());
					logger.debug(txObject.thread.toString() + ": Deleted " + modifyRequest.getKey() + " in " + map.name);
				}

			}
			
			unlockLocks();
			logger.debug("finished unlocking");
			TxController.tl.remove();
			tx.status = TxStatus.COMMITTED;
			tx.completionStatus = TxCompletionStatus.COMMITTED;
			tx = new MyTx();
		}
		logger.debug("Exiting Commit");

	}

	public TxStatus getStatus() throws SystemException {
		return tx.status;
	}

	public Tx getTx() throws SystemException {
		if (TxController.tl.get() == null) {
			return new MyTx(); // after a thread commits, there is no more TX asscoiated with it so I return
								// this.
		}
		return tx;
	}

	public void rollback() throws IllegalStateException, SystemException {
		if (TxController.tl.get() == null) {
			throw new IllegalStateException("Thread not in Tx trying to rollback");
		}
		TxThread txObject = TxController.tl.get();

		int updatePointer = 0;
		int readPointer = 0;
		// we must reinstantiate the input streams so we don't keep refering to the same
		// thing. Will get EOF error
		for (int i = 0; i < txObject.actions.size(); i++) {
			if (txObject.actions.get(i) == Crud.UPDATE || txObject.actions.get(i) == Crud.READ) {
				ModifyRequest<?, ?> modifyRequest = txObject.updateRequests.get(updatePointer);
				updatePointer++;
				Object key = modifyRequest.getKey();
				MyMap<?, ?> map = Database.database.get(modifyRequest.getMapName());
				if(map == null) {
					continue;
				}
				Object value = map.map.get(key);
				try {
					Buffer bos = MySerializer.serialize(value);
					map.save(key, bos);

				} catch (NotSerializableException e) {
					unlockLocks();
					//throw new SystemException("Can't serialize value class ");
				} catch (IOException e) {
					//throw new SystemException("Error with input and output");
				}
			}
		
		}
 
		tx.status = TxStatus.ROLLING_BACK;
		tx.completionStatus = null;
		unlockLocks();
		TxController.tl.remove();
		tx.status = TxStatus.ROLLEDBACK;
		tx.completionStatus = TxCompletionStatus.ROLLEDBACK;
		tx = new MyTx();

	}

	private void unlockLocks() {
		ArrayList<MyLock> locks = (ArrayList<MyLock>) TxController.tl.get().locks;
		for (int i = 0; i < locks.size(); i++) {
			MyLock lock = locks.get(i);
			lock.removeFromThread();
			while (lock.isHeldByCurrentThread()) {
				logger.debug("right before unlock");
				lock.unlock();
			}

		}

	}
}