package semesterProject;

import java.util.ArrayList;

class Row<T extends Comparable<T>> {
	private ArrayList<T> data;
	private int size;

	protected Row(int columns) {

		data = new ArrayList<T>();
		for (int i = 0; i <= columns; i++) {
			data.add(i, null);
		}

	}

	protected void setData(T object, int index) {
		data.set(index, object);
		if (index > getSize()) {
			setSize(index);
		}
	}

	protected T getData(int index) {
		return data.get(index);
	}

	protected int getSize() {
		return size;
	}

	protected void setSize(int size) {
		this.size = size;
	}

}
