package semesterProject;

import java.util.ArrayList;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnValuePair;

/*
 * A couple of helper methods, mainly used to parse data types, and remove duplicates
 */
class Helpers {

	/*
	 * This functions job is to change a string value to a given type. If type
	 * doesn't match string it throws an error
	 */
	protected static <T extends Comparable> Comparable<?> parseType(T value, String type) {
		String value2 = value.toString();

		try {
			if (type.toString().equals(("VARCHAR"))) {
				if (value2.charAt(0) == 39) {
					value2 = value2.substring(1, value2.length() - 1);
					return (String) value2;
				}
			}
			if (type.toString().equals(("DECIMAL"))) {
				return new Double(value2);
			}
			if (type.toString().equals(("INT"))) {
				return new Integer(value2);
			}

			if (type.toString().equals(("BOOLEAN"))) {
				return new Boolean(value2);
			}
			return value2;
		} catch (Exception e) {
			throw new IllegalArgumentException(value + " is not a valid " + type);
		}

	}

	/*
	 * Sees if a row meets null requirements
	 */
	protected static <T extends Comparable<T>> void checkIfNull(Table<T> table, Row<T> row) {
		ArrayList<Integer> nullIndex = table.getNullIndex();

		for (int columnNumber = 0; columnNumber < nullIndex.size(); columnNumber++) {
			if (row.getData((Integer) nullIndex.get(columnNumber)) == null) {
				throw new IllegalArgumentException("Enter a value for "
						+ (table.getCollumnClassification((Integer) nullIndex.get(columnNumber))).getDescription());
			}
			if (row.getData((Integer) nullIndex.get(columnNumber)).toString().equals("")) {
				throw new IllegalArgumentException("Enter a value for "
						+ ((Classification) table.getCollumnClassification((Integer) nullIndex.get(columnNumber)))
								.getDescription());

			}
		}
	}

	/*
	 * Sees if a row meets unique requirements
	 */
	protected static <T extends Comparable<T>> void checkIfUnique(Table<T> table, Row<T> row) {

		ArrayList<Integer> uniqueIndex = table.getUniqueIndex();
		for (int columnNumber = 0; columnNumber < uniqueIndex.size(); columnNumber++) {
			for (int rowNumber = 0; rowNumber < table.getNumberOfRows(); rowNumber++) {

				if (!(table.getRow(rowNumber).getData(uniqueIndex.get(columnNumber)) == null)) {

					if (table.getRow(rowNumber).getData(uniqueIndex.get(columnNumber))
							.equals(row.getData(uniqueIndex.get(columnNumber))))
						throw new IllegalArgumentException(
								table.getCollumnClassification(uniqueIndex.get(columnNumber)).getDescription()
										+ " Must be unique");

				}
			}
		}
	}

	/*
	 * Changes the value in a table and btree
	 */

	protected static <T extends Comparable<T>> void changeValues2(ColumnValuePair[] changes, ArrayList<Row<T>> rows,
			Table<T> table, int m) throws IllegalAccessException {

		ArrayList<Integer> changeIndexes = new ArrayList<Integer>();
		for (int i = 0; i < changes.length; i++) {
			int temp = Select.getColumn(table, changes[i].getColumnID().getColumnName().toString());
			changeIndexes.add(temp);
			if (changes[i].getValue().toString().equals("NULL")) {
				if (table.getCollumnClassification(changeIndexes.get(i)).getIsNull()) {
					throw new IllegalArgumentException(changes[i].getColumnID().getColumnName() + " can't be null");
				}
			} else {
				T value = (T) Helpers.parseType(changes[i].getValue().toString(),
						table.getCollumnClassification(temp).getType().toString());
				Helpers.checkValid(value.toString(), table.getCollumnClassification(changeIndexes.get(i))); // all this
																											// is to
																											// make sure
																											// query is
																											// good
																											// before
																											// updating
			}
		}

		ArrayList<Row<T>> copyrows = new ArrayList<Row<T>>(); // just a copy
		for (int i = 0; i < rows.size(); i++) {
			Row<T> row = rows.get(i);
			copyrows.add(row);
		}
		boolean isNull = false;
		int size = rows.size();
		for (int rowNumber = 0; rowNumber < size; rowNumber++) {
			T value = null;
			Row<T> row = copyrows.get(rowNumber);
			boolean unique = false;
			for (int changeNumber = 0; changeNumber < changes.length; changeNumber++) { // for each change

				int changeColumn = changeIndexes.get(changeNumber);
				if (changes[changeNumber].getValue().toString().equals("NULL")) {
					isNull = true;
				} else {
					value = (T) Helpers.parseType(changes[changeNumber].getValue().toString(),
							table.getCollumnClassification(changeColumn).getType().toString());
					Helpers.checkValid(value.toString(), table.getCollumnClassification(changeColumn));

					unique = Helpers.checkUniqueValue(table, value, changeColumn);
				}
				if (unique && rows.size() > 1) {
					throw new IllegalArgumentException(
							table.getCollumnClassification(changeColumn).getDescription() + " Must be unique");
				}
				ArrayList<BTree<T>> btrees = table.getBtrees();
				boolean set = true;
				for (int btreeNumber = 0; btreeNumber < btrees.size(); btreeNumber++) {

					BTree<T> btree = btrees.get(btreeNumber);
					if (btree.getColumn().equals((changes[changeNumber].getColumnID().getColumnName()).toString())) {
						T data = row.getData(changeColumn);
						btree.removeRow(data, row); // delete old to get rid of
						if (!isNull) {
							row.setData(value, changeColumn);
						}

						btree.put(value, row); // put in new value
						continue;

					}
				}
				if (!isNull) {
					row.setData(value, changeColumn);
				} else {
					row.setData(value, changeColumn); // set only after btree is fixed
				}
			}
		}

	}

	/*
	 * Checks if meets unique
	 */
	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>> boolean checkUniqueValue(Table<T> table, T value, int column) {

		T valued = (T) Helpers.parseType(value, table.getCollumnClassification(column).getType().toString());
		if (table.getCollumnClassification(column).getIsUnique() == false) {
			return false;
		}
		for (int i = 0; i < table.getNumberOfRows(); i++) {
			Row<T> row = (Row<T>) table.getRow(i);
			if (row.getData(column) != null) {
				if (row.getData(column).compareTo((T) valued) == 0) {
					throw new IllegalArgumentException(
							(table.getCollumnClassification(column).getDescription() + " must be unique"));
				}
			}
		}
		return true;
	}

	/*
	 * Checks if meets tables requirements
	 */
	protected static void checkValid(String columnValue, Classification collumnClassification) {

		if (collumnClassification.getType().toString().equals("INT")) {
			try {
				@SuppressWarnings("unused")
				Integer d = new Integer(columnValue);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Enter a valid INT for " + collumnClassification.getDescription());
			}
		}
		if (collumnClassification.getType().toString().equals("BOOLEAN")) {
			if (!columnValue.toLowerCase().equals("true") && !columnValue.toLowerCase().equals("false")) {
				throw new IllegalArgumentException(
						"Enter a valid BOOLEAN for " + collumnClassification.getDescription());
			}
		}
		if (collumnClassification.getType().toString().equals("VARCHAR")) {
			int length = collumnClassification.getVarCharLength();
			if (columnValue.toString().charAt(0) == 39) {
				length += 2;

			}
			if (columnValue.length() >= length) {

				throw new IllegalArgumentException(
						"The VARCHAR is too long in " + collumnClassification.getDescription());
			}
		}

		if (collumnClassification.getType().toString().equals("DECIMAL")) {
			try {
				Double doub = new Double(columnValue);
				int decimal = doub.toString().indexOf(".");
				if (decimal != -1) {
					if (doub.toString().substring(0, decimal).length() > collumnClassification.getIntegerLength()) {
						throw new IllegalArgumentException("Whole Number Part of DECIMAL is Too Long in "
								+ collumnClassification.getDescription());
					} else if (doub.toString().substring(decimal + 1).length() > collumnClassification
							.getFractionalLength()) {
						throw new IllegalArgumentException(
								"Fractional Part of DECIMAL is Too Long in " + collumnClassification.getDescription());
					}
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(
						"Enter a valid DECIMAL for " + collumnClassification.getDescription());
			}
		}
	}

	/*
	 * Really should've used this function more but it exists
	 */
	protected static <T extends Comparable<T>> BTree<T> getBtree(Table<T> table, Object columnName) {
		ArrayList<BTree<T>> btrees = table.getBtrees();
		for (int i = 0; i < btrees.size(); i++) {
			for (int columnNumber = 0; columnNumber < table.getNumberOfCollumns(); columnNumber++) {
				BTree<T> btree = (BTree<T>) btrees.get(i);
				if (btree.getColumn().toString().equals(columnName.toString())) {
					return (BTree<T>) btrees.get(i);
				}
			}

		}
		return null;

	}
}
