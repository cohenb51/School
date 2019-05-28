package semesterProject;

class Page<T extends Comparable<T>> {
	private Node<T>[] entries;
	private boolean full;
	private boolean external;
	private int m;
	private int entryCount;

	@SuppressWarnings("unchecked")
	protected Page(int m, boolean isExternal) {
		setEntries(new Node[m]);
		full = false;
		external = isExternal;
		this.setM(m);
		setEntryCount(0);
	}

	protected boolean isFull() {
		return full;
	}

	protected boolean isExternal() {
		return external;
	}

	protected int getM() {
		return m;
	}

	protected void setM(int m) {
		this.m = m;
	}

	protected int getEntryCount() {
		return entryCount;
	}

	protected void setEntryCount(int entryCount) {
		this.entryCount = entryCount;
	}

	protected Node<T>[] getEntries() {
		return entries;
	}

	protected void setEntries(Node<T>[] entries) {
		this.entries = entries;
	}

}