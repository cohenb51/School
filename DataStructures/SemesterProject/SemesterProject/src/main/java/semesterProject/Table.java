package semesterProject;

import java.util.ArrayList;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.CreateTableQuery;

/*
 * This class is simply responsible for storing data. A helper class with static methods called select
 * is used to select data from the data. Table also keeps track of columns and default, null, unique values. 
 * An arraylist called classification maps the columns to the table. Classification basically are the columns
 * and they have acess to its type, nulls, unique... The table also keeps track of which columns are
 *  null, unique, defualt since it shouldn't have to look up every column to see which columns fit those descriptions
 *  
 */

class Table<T extends Comparable<T>> {
	private String name;
	private ArrayList<Row<T>> rowData;
	private ArrayList<Classification> classification;
	private ColumnDescription primaryKey;
	private ArrayList<T> Default;
	private ArrayList<Integer> DefaultIndex;
	private ArrayList<Integer> NullIndex;
	private ArrayList<Integer> UniqueIndex;

	private ArrayList<BTree<T>> btrees = new ArrayList<BTree<T>>();

	protected Table(String name, CreateTableQuery query) {
		UniqueIndex = new ArrayList<Integer>();
		Default = new ArrayList<T>();
		DefaultIndex = new ArrayList<Integer>();
		NullIndex = new ArrayList<Integer>();
		ResultSet<T> rs = new ResultSet<T>();
		rowData = new ArrayList<Row<T>>();
		classification = new ArrayList<Classification>(); // The description of each column
		ColumnDescription[] var = query.getColumnDescriptions();
		primaryKey = query.getPrimaryKeyColumn();
		createTable(var, rs);
		this.name = name;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ResultSet createTable(ColumnDescription[] keys, ResultSet rs) { //
		for (int i = 0; i < keys.length; i++) {
			boolean isPrimaryKey = keys[i].getColumnName().toString().equals(primaryKey.getColumnName().toString());
			classification.add(new Classification(keys[i]));

			if (keys[i].getHasDefault()) {
				if (keys[i].toString().equals(primaryKey.toString())) {
					throw new IllegalArgumentException("Primary Key CANNOT have DEFAULT value");
				}
				Comparable<?> data = Helpers.parseType(keys[i].getDefaultValue().toString(),
						keys[i].getColumnType().toString());
				Default.add((T) data);
				DefaultIndex.add(i);
			}
			if (keys[i].isNotNull() && !isPrimaryKey) {
				NullIndex.add(i);
			}
			if (keys[i].isUnique() && !isPrimaryKey) {
				UniqueIndex.add(i);
			}
			if (keys[i].toString().equals(primaryKey.toString())) {
				UniqueIndex.add(i);
				classification.get(i).setIsUnique(true);
				classification.get(i).setIsNull(false);
				NullIndex.add(i);

			}

		}

		BTree<T> bTree = new BTree<T>(4, primaryKey.getColumnType().toString());

		bTree.setColumn(primaryKey.getColumnName().toString());
		btrees.add(bTree);

		return rs;

	}

	protected ArrayList<BTree<T>> getBtrees() {
		return btrees;
	}

	protected Table<T> getTable() {
		return this;
	}

	protected String getName() {
		return name;
	}

	protected int getNumberOfCollumns() {
		return classification.size();
	}

	protected Classification getCollumnClassification(int index) {
		return classification.get(index);
	}

	protected Row<T> insert(int index, T value, Row<T> row) {
		row.setData(value, index);

		return row;
	}

	protected Row<T> createRow() {
		Row<T> row = new Row<T>(classification.size() - 1);
		for (int i = 0; i < DefaultIndex.size(); i++) {
			row.setData((T) Default.get(i), DefaultIndex.get(i));
		}

		return row;
	}

	protected Table<T> removeRow(int row) {
		rowData.remove(row);
		return getTable();
	}

	protected Row<T> getRow(int row) {
		return rowData.get(row);
	}

	protected ArrayList<Row<T>> getRowData() {
		return rowData;
	}

	protected void addRow(Row<T> row) {
		rowData.add(row);
	}

	protected int getNumberOfRows() {
		return rowData.size();
	}

	protected ArrayList<Integer> getUniqueIndex() {
		return UniqueIndex;
	}

	protected ArrayList<Integer> getNullIndex() {
		return NullIndex;
	}

	public void setRows(ArrayList<Row<T>> rowCopy) {
		this.rowData = rowCopy;
		
	}

}
