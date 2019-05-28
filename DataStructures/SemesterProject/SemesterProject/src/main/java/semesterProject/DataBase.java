package semesterProject;

import java.util.ArrayList;

class DataBase<T extends Comparable<T>> {

	private ArrayList<Table<T>> tables;

	protected DataBase() {
		tables = new ArrayList<Table<T>>();

	}

	protected Table<T> getTable(String name) throws IllegalAccessException {
		for (int i = 0; i < getTables().size(); i++) {
			if (getTables().get(i).getName().equals(name)) {
				return getTables().get(i);
			}
		}
		throw new IllegalAccessException("Table " + name + " doesn't exist");
	}

	protected void add(Table<T> table) {
		getTables().add(table);

	}
	
	protected ArrayList<Table<T>> getDataBase() {
		return getTables();
	}

	protected int numberOfTables() {
		return getTables().size();
	}

	public int getTableNumber(String tableName) {
		for (int i = 0; i < getTables().size(); i++) {
			if (getTables().get(i).getName().equals(tableName)) {
				return i;
			}
		}
		return -1;

	}

	protected ArrayList<Table<T>> getTables() {
		return tables;
	}



}
