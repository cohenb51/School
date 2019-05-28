package edu.yu.oats.oatsdb.dbms.v0b;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyLock {
	private final static Logger logger = LogManager.getLogger(MyLock.class);

	protected ReentrantLock lock;
	protected LinkedList<Thread> threadQueue;

	protected MyLock() {
		this.lock = new ReentrantLock();
		this.threadQueue = new LinkedList<>();

	}

	protected boolean tryLock() {
		boolean temp = lock.tryLock();
		if (temp)
			return true;
		if (!lock.isHeldByCurrentThread()) {
			try {
				int toSleep = 0;
				if(DBMSImpl.timeout >100) {
					toSleep = DBMSImpl.timeout; // an adjustment to compensate for method calls
				}
				else { toSleep = DBMSImpl.timeout;}
				this.threadQueue.add(Thread.currentThread());
				Thread.sleep(toSleep);
			} catch (InterruptedException e) {
				logger.info("SOMEONE INTERUPTED MY SLEEP GRRR");
				return lock.tryLock();
			}
		}
		return false;
	}

	protected void unlock() {
		while (lock.isHeldByCurrentThread()) {
			lock.unlock();
		}
		if(this.threadQueue.size() == 0) {
			return;
		}
		else {
			Thread thread = threadQueue.removeFirst();
			thread.interrupt();
		}
		
	}

	protected void lock() {
		lock.lock();
	}

	protected boolean isHeldByCurrentThread() {
		return lock.isHeldByCurrentThread();
	}

}
