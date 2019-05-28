package semesterProject;

import java.util.ArrayList;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnID;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnValuePair;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.CreateIndexQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.CreateTableQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.DeleteQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.InsertQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SQLParser;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SQLQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery.FunctionInstance;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.UpdateQuery;
import net.sf.jsqlparser.JSQLParserException;

/**
 * @author Benjamin Cohen
 * @version 1320 A class to execute SQL commands.
 */

public class Main {
	int m = 4;

	ArrayList<Condition> whereCondition = new ArrayList<Condition>();
	static DataBase dataBase = new DataBase();

	/**
	 * Excutes a given SQL string
	 * 
	 * @param SQL
	 *            the SQL string
	 * @return The resultSet. resultSet for insert, delete, has 1 row of 1 data
	 *         element, true or false. For create table the result set contains an
	 *         arraylist of describers (which describe the columns just created
	 *         (name and datatype) For select the result set contains an array list
	 *         of describers and an array list of rows. For select functions the
	 *         result set is an array list of describers, 1 describer per function,
	 *         and the functions value underneath with the name of the function (for
	 *         clarity in the event of multiple functions)
	 * 
	 * 
	 */
	public ResultSet Execute(String SQL) throws JSQLParserException, IllegalAccessException {
		ResultSet rs = new ResultSet();
		SQLParser parser = new SQLParser();
		SQLQuery query = null;
		try {
			query = parser.parse(SQL);
		} catch (Exception e) {
			System.out.println("ERROR PARSING");
			e.printStackTrace();
		}
		try {
			if (query instanceof CreateTableQuery) {
				rs = createTable((CreateTableQuery) query);
			}
			if (query instanceof SelectQuery) {
				rs = selectTable((SelectQuery) query);

			}
			if (query instanceof InsertQuery) {
				insert((InsertQuery) query); // originally insert was supposed to return a rs now it just does it here
												// for no real reason.
				rs.setTrue(rs);

			}

			if (query instanceof DeleteQuery) {
				deleteRow((DeleteQuery) query);
				rs.setTrue(rs);

			}
			if (query instanceof UpdateQuery) {
				updateRow((UpdateQuery) query);
				rs.setTrue(rs);
			}
			if (query instanceof CreateIndexQuery) {
				CreateIndexQuery((CreateIndexQuery) query);
				rs.setTrue(rs);
			}

		} catch (Exception e) {
			e.printStackTrace();
			rs = new ResultSet();
			rs.setFalse(rs);

		}

		return rs;
	}

	@SuppressWarnings("unchecked")
	private <T extends Comparable<T>> ResultSet<T> CreateIndexQuery(CreateIndexQuery query)
			throws IllegalAccessException {

		Table<T> table = null;
		for (int i = 0; i < dataBase.getTables().size(); i++) {
			if (((Table<T>) dataBase.getTables().get(i)).getName().equals(query.getTableName())) {
				table = (Table<T>) dataBase.getTables().get(i);
			}

		}
		String columnName = query.getColumnName();
		int column = Select.getColumn(table, columnName);

		String type = (table.getCollumnClassification(column)).getType().toString();
		
		
			BTree<T> btree = Helpers.getBtree(table, columnName);
			if(btree != null) {
			throw new IllegalArgumentException(columnName + " is already indexed");
			}
			
		
		
		BTree<T> bTree = new BTree<T>(m, type);

		for (int rowNumber = 0; rowNumber < table.getNumberOfRows(); rowNumber++) {
			Row<T> row = (Row<T>) table.getRow(rowNumber);

			Node<T> node = new Node<T>(row.getData(column), m);
			if (node.getData() != null) {
				bTree.put(node, row);

			} else {
				bTree.addNullRow(row);

			}

		}

		bTree.setName(query.getIndexName());
		bTree.setColumn(query.getColumnName());
		table.getBtrees().add(bTree);

		return null;
	}

	@SuppressWarnings("unused")
	private <T extends Comparable<T>> ResultSet<T> updateRow(UpdateQuery query) throws IllegalAccessException {

		Condition condition = query.getWhereCondition();
		@SuppressWarnings("unchecked")
		Table<T> table = dataBase.getTable(query.getTableName());
		ArrayList<Row<T>> rows = new ArrayList<Row<T>>();
		if (condition != null) {
			rows = ConditionFinder.checkCondition(condition, table, new ArrayList<Row<T>>(), 0);
		} else {
			rows = table.getRowData();
		}
		ArrayList<Row<T>> test = new ArrayList<Row<T>>();

		Helpers.changeValues2(query.getColumnValuePairs(), rows, table, m);

		return null;
	}

	private <T extends Comparable<T>> ResultSet<T> insert(InsertQuery query) throws IllegalAccessException {
		ColumnID columnName;
		String columnValue;
		Table<T> table = dataBase.getTable(query.getTableName());
		Row<T> row = table.createRow();
		ColumnValuePair[] var = query.getColumnValuePairs();

		for (int i = 0; i < table.getNumberOfCollumns(); i++) {
			for (int j = 0; j < var.length; j++) {
				columnName = var[j].getColumnID();
				columnValue = var[j].getValue();

				if (columnName.toString().endsWith(table.getCollumnClassification(i).getDescription())) {
					if (!columnValue.toString().equals("NULL")) { // just don't add it if it is null. I'll check at the
																	// end if there are any nulls
						T value = (T) Helpers.parseType(columnValue.toString(),
								table.getCollumnClassification(i).getType().toString());
						row.setData(value, i);
						Helpers.checkValid(columnValue, table.getCollumnClassification(i));

					}

				}

			}

		}
		Helpers.checkIfUnique(table, row);
		Helpers.checkIfNull(table, row);
		table.addRow(row);

		ArrayList<BTree<T>> btrees = table.getBtrees();

		for (int i = 0; i < btrees.size(); i++) {

			BTree<T> btree = btrees.get(i);
			String column = btree.getColumn();
			for (int z = 0; z < table.getNumberOfCollumns(); z++) {
				if (column.equals(table.getCollumnClassification(z).getDescription().toString())) {
					Node<T> node = new Node<T>(row.getData(z), m);
					btree.put(node, row);
				}
			}

		}

		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static ResultSet createTable(CreateTableQuery query) {

		ResultSet rs = new ResultSet();
		String tableName = query.getTableName();
		Table table = new Table(tableName, query);
		dataBase.add(table);
		for (int i = 0; i < table.getNumberOfCollumns(); i++) {
			rs.addDescriber(table.getCollumnClassification(i));
		}
		return rs;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T extends Comparable<T>> ResultSet selectTable(SelectQuery query) throws IllegalAccessException {
		ArrayList<FunctionInstance> functionList = query.getFunctions();
		ResultSet resultSet = new ResultSet();
		ColumnID[] var = query.getSelectedColumnNames();
		String[] tables = query.getFromTableNames();
		int numberOfColumns;
		Condition condition = query.getWhereCondition();

		if (functionList.size() > 0 && var.length > functionList.size()) {
			throw new IllegalAccessError(
					"Per Project specification: For SELECT queries, you don�t have to deal with combinations of the syntax other than those I listed earlier");
			// the parser counts functions also as a column as it should handle.
		}

		for (int t = 0; t < tables.length; t++) { // for each table
			Table<T> table = dataBase.getTable(tables[t]);
			ArrayList<String> functionsCollumed = new ArrayList<String>();
			if (functionList.size() >= 1) {
				for (int i = 0; i < functionList.size(); i++) {
					functionsCollumed.add(functionList.get(i).column.getColumnName());
				}
				return Select.getFunctionData(functionList, table, condition);
			}

			numberOfColumns = (var[0].toString() == "*") ? table.getNumberOfCollumns() : var.length;

			if (var[0].toString().equals("*") || var.length == 0) {
				Select.addAllColumns(numberOfColumns, resultSet, table, query, condition);
			} else {
				Select.addColumns(numberOfColumns, resultSet, table, condition, var, functionsCollumed,
						query.getOrderBys(), query);
				return resultSet;
			}
			if (query.isDistinct()) { // before ordering take the distinct so it takes first of each.
				ArrayList<Integer> colls = new ArrayList<Integer>();
				for (int i = 0; i < table.getNumberOfCollumns(); i++) {
					colls.add(i);
				}
				resultSet.setRows(Select.removeDistinct(resultSet, colls));
			}
			if (query.getOrderBys().length != 0) {
				resultSet = Select.OrderBy(query.getOrderBys(), table, resultSet, 0);
			}
		}

		return resultSet;

	}

	protected <T extends Comparable<T>> ArrayList<Table<T>> getDataBase() {
		return dataBase.getDataBase();
	}

	@SuppressWarnings("unchecked")
	private <T extends Comparable<T>> void deleteRow(DeleteQuery query) throws IllegalAccessException {
		Table<T> table = dataBase.getTable(query.getTableName());
		Condition condition = query.getWhereCondition();
		Select.deleteRows(table, condition);

	}

}