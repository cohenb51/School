package semesterProject;

import java.util.ArrayList;

public class Node<T extends Comparable<T>> {
	
	private T data;
	private Page<T> child;
	private ArrayList<Row<T>> rows;
	
	protected Node(T sentinel, int m) {
		this.setData(sentinel);
		setChild(new Page<T>(m, true));
		setRows(new ArrayList<Row<T>>());
	}

	protected Node(T data2, Row<T> row, int m, Page<T> child) {
		this.setData(data2);
		this.setChild(child);
	}
	
	protected Page<T> getChild() {
		return child;
	}

	protected void setChild(Page<T> child) {
		this.child = child;
	}
	
	protected T getData() {
		return data;
	}
	
	protected void setData(T data) {
		this.data = data;
	}

	protected ArrayList<Row<T>> getRows() {
		return rows;
	}

	protected void setRows(ArrayList<Row<T>> rows) {
		this.rows = rows;
	}

}

