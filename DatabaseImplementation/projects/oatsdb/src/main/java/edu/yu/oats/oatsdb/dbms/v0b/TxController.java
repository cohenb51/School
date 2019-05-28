package edu.yu.oats.oatsdb.dbms.v0b;
//11/10
class TxController {
	protected static ThreadLocal<TxThread> tl = new ThreadLocal<>();

	protected static void startTX() {
		Thread thread = Thread.currentThread();
		TxThread txThread = new TxThread(thread);
		tl.set(txThread);
	}

}