package semesterProject;

import java.util.ArrayList;

/*
 * arraylist.addall was not listed as a method we could use therefore for some of the methods I had to use a loop to join lists together. 
 */
class BTree<T extends Comparable<T>> {
	private Page<T> root;
	private int m;
	private String column;
	private int height;
	private String name;
	private String type;
	private T sentinel;
	private ArrayList<Row<T>> NullRows; // Nulls don't get indexed in the btree. My btree is only made up of the pages
	boolean isEmpty = true;									// and entries so by definetion this arraylist is kept off of it.
	
	
	
	@SuppressWarnings("unchecked")
	protected BTree(int m, String type) {
		root = new Page<T>(m, true);
		this.m = m;
		height = 0;
		this.sentinel = (T) getSentinelType(type);
		root.getEntries()[0] = new Node<T>(sentinel, m); // TODO make
		NullRows = new ArrayList<Row<T>>();
		root.setEntryCount(1);
		this.setType(type);

	}
	
	protected boolean getIsEmpty() {
		return isEmpty;
	}

	private Object getSentinelType(String type) {
		if (type.toString().equals(("VARCHAR"))) {
			return "";
		}
		if (type.toString().equals(("DECIMAL"))) {
			Double doub = Double.MIN_VALUE;

			return doub;
		}

		if (type.toString().equals(("INT"))) {
			return Integer.MIN_VALUE;

		}

		if (type.toString().equals(("BOOLEAN"))) {
			return Boolean.FALSE;
		}

		return null;

	}

	protected Page<T> put(Page<T> currentPage, Node<T> data, Row<T> row, int height) {
		int j;

		if (data.getData() == null) {
			NullRows.add(row);
			return null;
		}
		
		isEmpty = false;
		boolean flag = false;
		Node<T> newNode = new Node<T>(data.getData(), m);
		if (height == 0) { // if at external
			newNode = new Node<T>(data.getData(), m);
			for (j = 0; j < currentPage.getEntryCount(); j++) {

				if (Less(data.getData(), currentPage.getEntries()[j].getData())) {
					break;
				}
				if (isEqual(data.getData(), currentPage.getEntries()[j])) {
					currentPage.getEntries()[j].getRows().add(row);
					flag = true;
					break;
				}

			}

		}

		else {
			for (j = 0; j < currentPage.getEntryCount(); j++) {
				if ((j + 1 == currentPage.getEntryCount())
						|| Less(data.getData(), currentPage.getEntries()[j + 1].getData())) {
					Page<T> newPage = this.put(currentPage.getEntries()[j++].getChild(), data, row, height - 1);
					if (newPage == null) {
						return null;
					}
					T data2 = newPage.getEntries()[0].getData();
					newNode.setData(data2);
					newNode.setChild(newPage);
					break;
				}
			}
		}

		for (int i = currentPage.getEntryCount(); i > j && flag == false; i--) {
			currentPage.getEntries()[i] = currentPage.getEntries()[i - 1];
		}
		if (flag == false) {
			currentPage.getEntries()[j] = newNode;
			if (height == 0) {
				currentPage.getEntries()[j].getRows().add(row);
			} else {
				currentPage.getEntries()[j].setRows(null);
			}
			currentPage.setEntryCount(currentPage.getEntryCount() + 1);
		}

		if (currentPage.getEntryCount() < m) {
			return null;
		} else {
			currentPage.setEntryCount(m / 2);
			return split(currentPage);
		}

	}

	private Page<T> split(Page<T> currentPage) {
		Page<T> newPage = new Page<T>(m, false);
		newPage.setEntryCount(m / 2);
		currentPage.setEntryCount(m / 2);

		for (int j = 0; (m / 2) + j < m; j++) {
			newPage.getEntries()[j] = currentPage.getEntries()[(m / 2) + j];
			currentPage.getEntries()[(m / 2) + j] = null;

		}

		return newPage;

	}

	void put(Node<T> data, Row<T> row) {

		Page<T> newPage = this.put(root, data, row, this.height);

		if (newPage == null) {
			return;
		} else {
			Page<T> newRoot = new Page<T>(m, false);
			Node<T> left = new Node<T>(root.getEntries()[0].getData(), null, m, this.root);
			Node<T> right = new Node<T>(newPage.getEntries()[0].getData(), null, m, newPage);
			newRoot.getEntries()[0] = left;
			newRoot.getEntries()[1] = right;
			newRoot.setEntryCount(2);

			this.root = newRoot;
			this.height++;

		}

	}

	protected ArrayList<Row<T>> get(T data) {
		if (data.toString().equals("NULL")) {
			return NullRows;
		}
		return this.get(root, data, height);

	}

	ArrayList<Row<T>> get(Page<T> currentPage, T data, int height) {
		if (height == 0) {
			for (int j = -1; j < currentPage.getEntryCount() - 1; j++) {
				if (isEqual(data, currentPage.getEntries()[j + 1])) {
					return currentPage.getEntries()[j + 1].getRows();
				}
			}
			return new ArrayList<Row<T>>();

		} else {
			for (int j = 0; j < currentPage.getEntryCount(); j++) {
				if (j + 1 == currentPage.getEntryCount() || Less(data, currentPage.getEntries()[j + 1].getData())
						|| isEqual(data, currentPage.getEntries()[j])) {
					return this.get(currentPage.getEntries()[j].getChild(), data, height - 1);
				}
			}
		}

		return new ArrayList<Row<T>>();
	}

	private <P extends Comparable<P>> boolean Less(T data, T data2) {
		if (data2 != null) {
			if (data.compareTo(data2) < 0) {
				return true;
			}
		}

		return false;

	}

	private boolean LessOrEqual(T data, T entries) {
		if (entries != null) {
			if ((data.compareTo(entries) < 0) || (data.compareTo(entries)) == 0) {
				return true;
			}
		}
		return false;
	}

	private boolean isEqual(T data, Node<T> entries) {
		if (entries.getData() != null) {
			if (data.compareTo(entries.getData()) == 0) {
				return true;
			}
		}

		return false;
	}

	void print(Page<T> currentPage, int height) {
		System.out.println();
		System.out.println(height + " height");
		System.out.println("new Page" + currentPage.getEntryCount());
		for (int i = 0; i < currentPage.getEntryCount(); i++) {
			if (currentPage.getEntries()[i] != null)
				System.out.println(currentPage.getEntries()[i].getData());
			if (currentPage.getEntries()[i].getRows() != null) {
				System.out.print("rows inside ");
				System.out.println(currentPage.getEntries()[i].getRows().size());

			}
		}

		for (int i = 0; i < currentPage.getM(); i++) {
			if (currentPage.getEntries()[i] != null) {

				if (currentPage.getEntries()[i].getChild().getEntryCount() != 0) {
					System.out.println("next page: child of node with key " + currentPage.getEntries()[i].getData());
					print(currentPage.getEntries()[i].getChild(), height - 1);

				}
			}
		}

	}

	protected ArrayList<Row<T>> getGreaterThan(Page<T> currentPage, T data, int height, ArrayList<Row<T>> greaterRows) {

		if (height != 0) {

			for (int j = currentPage.getEntryCount() - 1; j >= 0; j--) {
				if (j - 1 >= 0) {
					int childEntryCount = currentPage.getEntries()[j].getChild().getEntryCount();
					if (LessOrEqual(data,
							currentPage.getEntries()[j].getChild().getEntries()[childEntryCount - 1].getData())) {
						greaterRows = this.getGreaterThan(currentPage.getEntries()[j].getChild(), data, height - 1,
								greaterRows);

					} else {
						break;
					}
				} else {
					greaterRows = this.getGreaterThan(currentPage.getEntries()[j].getChild(), data, height - 1,
							greaterRows);

				}

			}

		}

		else {
			for (int i = currentPage.getEntryCount() - 1; i >= 0; i--) {
				if ((Less(data, currentPage.getEntries()[i].getData()))) {
					for (int adend = 0; adend < currentPage.getEntries()[i].getRows().size(); adend++) { // Would have used add all if we were allowed
						greaterRows.add((Row<T>) currentPage.getEntries()[i].getRows().get(adend));
					}
				}
			}
		}
		return greaterRows;
	}

	protected ArrayList<Row<T>> getGreaterOrEqualsThan(Page<T> currentPage, T data, int height,
			ArrayList<Row<T>> greaterRows) {

		if (height != 0) {

			for (int j = currentPage.getEntryCount() - 1; j >= 0; j--) {
				if (j - 1 >= 0) {
					int childEntryCount = currentPage.getEntries()[j].getChild().getEntryCount();
					if (LessOrEqual(data,
							currentPage.getEntries()[j].getChild().getEntries()[childEntryCount - 1].getData())) {
						greaterRows = this.getGreaterOrEqualsThan(currentPage.getEntries()[j].getChild(), data,
								height - 1, greaterRows);

					} else {
						break;
					}
				} else {
					greaterRows = this.getGreaterOrEqualsThan(currentPage.getEntries()[j].getChild(), data, height - 1,
							greaterRows);

				}

			}

		}

		else {
			for (int i = currentPage.getEntryCount() - 1; i >= 0; i--) {
				if ((LessOrEqual(data, currentPage.getEntries()[i].getData()))) {
					for (int adend = 0; adend < currentPage.getEntries()[i].getRows().size(); adend++) {

						greaterRows.add((Row<T>) currentPage.getEntries()[i].getRows().get(adend));
					}
				}
			}
		}
		return greaterRows;

	}

	protected ArrayList<Row<T>> getNotEquals(Page<T> currentPage, T data, int height, ArrayList<Row<T>> greaterRows) {

		if (height != 0) {
			for (int j = 0; j < currentPage.getEntryCount(); j++) {
				greaterRows = this.getNotEquals(currentPage.getEntries()[j].getChild(), data, height - 1, greaterRows);
			}

		} else {
			for (int i = 0; i < currentPage.getEntryCount(); i++) {
				// System.out.println(currentPage.entries[i].getData());
				if ((!isEqual(data, currentPage.getEntries()[i]))) {
					for (int adend = currentPage.getEntries()[i].getRows().size() - 1; adend >= 0; adend--) {
						greaterRows.add((Row<T>) currentPage.getEntries()[i].getRows().get(adend));
					}
				}
			}
		}

		return greaterRows;
	}

	protected ArrayList<Row<T>> getGreaterThan(T data) {
		return this.getGreaterThan(root, data, height, new ArrayList<Row<T>>());

	}

	protected ArrayList<Row<T>> getLessThan(Page<T> currentPage, T data, int height, ArrayList<Row<T>> arrayList) {

		if (height != 0) {
			for (int j = 0; j < currentPage.getEntryCount(); j++) {
				if (LessOrEqual(currentPage.getEntries()[j].getData(), data)) {
					arrayList = this.getLessThan(currentPage.getEntries()[j].getChild(), data, height - 1, arrayList);

				} else {
					break;
				}

			}
		} else {

			for (int i = 0; i < currentPage.getEntryCount(); i++) {
				if ((Less(currentPage.getEntries()[i].getData(), data))) {
					for (int adee = 0; adee < currentPage.getEntries()[i].getRows().size(); adee++)
						arrayList.add((Row<T>) currentPage.getEntries()[i].getRows().get(adee)); // aka as add all. to eliminate the loop

				}
			}
		}

		return arrayList;
	}

	protected ArrayList<Row<T>> getLessOrEqualThan(Page<T> currentPage, T data, int height,
			ArrayList<Row<T>> greaterRows) {

		if (height != 0) {
			for (int j = 0; j < currentPage.getEntryCount(); j++) {
				if (LessOrEqual(currentPage.getEntries()[j].getData(), data)) {
					greaterRows = this.getLessOrEqualThan(currentPage.getEntries()[j].getChild(), data, height - 1,
							greaterRows);

				} else {
					break;
				}

			}
		} else {

			for (int i = 0; i < currentPage.getEntryCount(); i++) {
				if ((LessOrEqual(currentPage.getEntries()[i].getData(), data))) {
					for (int adee = 0; adee < currentPage.getEntries()[i].getRows().size(); adee++)
						greaterRows.add((Row<T>) currentPage.getEntries()[i].getRows().get(adee)); // CHECK

				}
			}
		}

		return greaterRows;
	}

	protected void remove(T data, Row<T> row) {
		if (data == null) {
			for (int i = 0; i < NullRows.size(); i++) {
				if (NullRows.get(i) == row) {
					NullRows.remove(row);
				}
			}
			return;
		}
		this.remove(root, data, height);

	}

	protected void removeRow(T data, Row<T> row) {

		if (data == null) {
			for (int i = 0; i < NullRows.size(); i++) {
				if (NullRows.get(i) == row) {
					NullRows.remove(i);
					return;
				}
			}
			return;
		}
		this.removeRow(root, data, height, row);

	}

	private void remove(Page<T> currentPage, T data, int height) {
		if (height == 0) {
			for (int j = -1; j < currentPage.getEntryCount() - 1; j++) {
				if (isEqual(data, currentPage.getEntries()[j + 1])) {

					currentPage.getEntries()[j + 1].setRows(new ArrayList<Row<T>>());

				}
			}

		} else {
			for (int j = 0; j < currentPage.getEntryCount(); j++) {
				this.remove(currentPage.getEntries()[j].getChild(), data, height - 1);

				if (Less(data, currentPage.getEntries()[j].getData()) || isEqual(data, currentPage.getEntries()[j])) {
					this.remove(currentPage.getEntries()[j].getChild(), data, height - 1);
				}
			}
		}
	}

	protected T getMin() {

		return (T) getMin(this.root, this.height);

	}

	private Comparable<T> getMin(Page<T> currentPage, int height) {
		if (height != 0) {
			for (int j = 0; j < currentPage.getEntryCount();) {
				T min = (T) this.getMin(currentPage.getEntries()[j].getChild(), height - 1);
                if(min != sentinel) {
				return min;
                }

			}

		} else {
			for (int i = 0; i < currentPage.getEntryCount(); i++) {
				if (currentPage.getEntries()[i].getRows().size() != 0) {
					return currentPage.getEntries()[i].getData();
				}

			}
		}
		return (Comparable) Double.NaN; // ie is empty

	}

	protected Comparable<T> getMax() {

		return getMax(this.root, this.height);

	}

	private Comparable<T> getMax(Page<T> currentPage, int height) {
		if (height != 0) {
			for (int j = currentPage.getEntryCount() - 1; j >= 0;) {
				Comparable<T> max = this.getMax(currentPage.getEntries()[j].getChild(), height - 1);
				return max;

			}

		} else {
			for (int i = currentPage.getEntryCount() - 1; i >= 0; i--) {
				if (currentPage.getEntries()[i].getRows().size() != 0) {
					if(currentPage.getEntries()[i].getData() == sentinel) {
						return (Comparable) Double.NaN;
					}
					return currentPage.getEntries()[i].getData();
				}

			}
		}
		return (Comparable) Double.NaN;

	}

	private void removeRow(Page<T> currentPage, T data, int height, Row<T> row) {
		ArrayList<Row<T>> rows = get(root, data, height);
		for (int p = 0; p < rows.size(); p++) {
			if (row.equals(rows.get(p))) {
				rows.remove(p);
			}
		}
	}

	protected void put(T data, Row<T> row) {
		Node<T> node = new Node<T>(data, m);
		put(node, row);

	}

	protected String getColumn() {
		return column;
	}

	protected void setColumn(String column) {
		this.column = column;
	}

	protected String getType() {
		return type;
	}

	protected void setType(String type) {
		this.type = type;
	}

	protected String getName() {
		return this.name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected ArrayList<Row<T>> getLessThan(T data) {
		return getLessThan(this.root, data, this.height, new ArrayList<Row<T>>());

	}

	protected ArrayList<Row<T>> getLessOrEqualThan(T data) {
		return getLessOrEqualThan(this.root, data, this.height, new ArrayList<Row<T>>());

	}

	protected ArrayList<Row<T>> getGreaterOrEqualsThan(T data) {
		return getGreaterOrEqualsThan(this.root, data, this.height, new ArrayList<Row<T>>());

	}

	protected ArrayList<Row<T>> getNotEquals(T data) {
		ArrayList<Row<T>> temp = null;
		if (!data.toString().equals("NULL")) {
			temp = getNotEquals(this.root, data, this.height, new ArrayList<Row<T>>());
		} else {
			temp = getNotEquals(this.root, sentinel, this.height, new ArrayList<Row<T>>());
			return temp;
		}

		if (temp != null) {
			for (int nullNumber = 0; nullNumber < NullRows.size(); nullNumber++) {
				temp.add(NullRows.get(nullNumber));
			}
		}

		return temp;

	}

	protected void print() {
		print(root, height);

	}

	protected void addNullRow(Row<T> row) {
		NullRows.add(row);
	}

}