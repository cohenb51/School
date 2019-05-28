package semesterProject;

import java.util.ArrayList;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnID;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery.FunctionInstance;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery.FunctionName;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery.OrderBy;

/*
 * This class contains a bunch of methods relating to selecting data from the table
 */

class Select {

	protected static <T extends Comparable<T>> double getAvg(ArrayList<Row<T>> rows, int position) {

		Row<T> row = null;
		double sum = 0;
		int n = 0;

		for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {

			row = rows.get(rowIndex);
			if (row.getData(position) != null) {
				Double addend = new Double(row.getData(position).toString());
				row = rows.get(rowIndex);
				sum += addend;
				n++;

			}

		}

		if (n != 0) { // Avoid divide by 0
			return (sum / n);
		} else {
			return Double.NaN;
		}

	}

	private static <T extends Comparable<T>> ArrayList<Row<T>> removeDuplicateData(ArrayList<Row<T>> rows,
			int position) {

		for (int rowNumber = 0; rowNumber < rows.size(); rowNumber++) {
			for (int duplicatePlacer = 0; duplicatePlacer < rows.size(); duplicatePlacer++) {
				if (rows.get(duplicatePlacer).getData(position).equals(rows.get(rowNumber).getData(position))
						&& rowNumber != duplicatePlacer) {
					rows.remove(duplicatePlacer);
					rowNumber--;
					break;
				}
			}
		}
		return rows;
	}

	protected static <T extends Comparable<T>> double getSum(ArrayList<Row<T>> rows, int position) {
		Row<T> row = null;

		double sum = 0;

		for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
			row = rows.get(rowIndex);
			if (row.getData(position) != null) {
				Double addend = new Double(row.getData(position).toString());
				row = rows.get(rowIndex);
				sum += addend;
			}

		}

		return (sum);

	}

	protected static <T extends Comparable<T>> double getCount(ArrayList<Row<T>> rows, int position) {

		Row<T> row = null;
		int n = 0;

		for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
			row = rows.get(rowIndex);
			if (row.getData(position) != null) {
				n++;
			}

		}
		return n;
	}

	protected static <T extends Comparable<T>> double getDistinct(Table<T> table, String column, int position) {
		int distinct = -1;

		if (table.getNumberOfRows() == 0) {
			return 0;
		}

		Row<T> row = null;
		ArrayList<Row<T>> temp = new ArrayList<Row<T>>();

		for (int rowNumber = 0; rowNumber < table.getNumberOfRows(); rowNumber++) {
			row = table.getRow(rowNumber);
			if (!contains(row.getData(position), temp, position)) { // TODO fix time complexity
				distinct++;
				temp.add(row);
			}

		}

		return distinct;

	}

	protected static <T extends Comparable<T>> ArrayList<Row<T>> removeDuplicates(ArrayList<Row<T>> result) {
		for (int rowNumber = 0; rowNumber < result.size(); rowNumber++) {
			for (int duplicatePlacer = 0; duplicatePlacer < result.size(); duplicatePlacer++) { // TODO unsquarify
				if (result.get(duplicatePlacer) == result.get(rowNumber) && rowNumber != duplicatePlacer) {
					result.remove(rowNumber);
					break;
				}
			}
		}
		return result;
	}

	private static <T extends Comparable<T>> boolean contains(Comparable<T> value, ArrayList<Row<T>> temp,
			int position) {
		if (temp.size() == 0) {
			return false;
		}

		for (int i = 0; i < temp.size(); i++) {
			if (temp.get(i).getData(position).equals(value)) {
				return true;
			}
		}
		return false;
	}

	protected static <T extends Comparable<T>> double getMax(ArrayList<Row<T>> rows, int position) {
		Row<T> row = null;
		double max = 0;

		if (getCount(rows, position) == 0) {
			return Double.NaN;
		}

		for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
			row = rows.get(rowIndex);
			if (row.getData(position) != null) {
				Double d = new Double(row.getData(position).toString());
				if (d > max) {
					max = d;
				}
			}

		}
		return max;

	}

	protected static <T extends Comparable<T>> double getMin(ArrayList<Row<T>> rows, int position) {
		if (getCount(rows, position) == 0) {
			return Double.NaN;
		}

		boolean found = false;
		Row<T> row = null;
		double min = Double.MAX_VALUE;

		if (rows.size() == 1) {
			return new Double(rows.get(0).getData(position).toString());
		}

		for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
			row = rows.get(rowIndex);
			Double d = new Double(row.getData(position).toString());
			if (d < min) {
				min = d;

			}

		}
		return min;

	}

	protected static <T extends Comparable<T>> int getColumn(Table<T> table, String column)
			throws IllegalAccessException {
		for (int tableColumn = 0; tableColumn < table.getNumberOfCollumns(); tableColumn++) {
			if (table.getCollumnClassification(tableColumn).getDescription().toString().equals(column)) {
				return tableColumn;
			}
		}
		throw new IllegalAccessException("Column " + column + " Doesn't exist");

	}

	protected static <T extends Comparable<T>> Comparable<T> evaluate(boolean distinct, FunctionName function,
			Table<T> table, String column, int columnIndex, Condition condition) throws IllegalAccessException {
		boolean didCondition = false;
		ArrayList<Row<T>> rows = new ArrayList<Row<T>>();
		if (condition != null) {
			rows = ConditionFinder.checkCondition(condition, table, new ArrayList<Row<T>>(), 0);
			didCondition = true;
		}

		String type = table.getCollumnClassification(columnIndex).getType().toString();
		if (type.equals("VARCHAR") || type.equals("BOOLEAN")) {
			if (function.toString().equals("SUM")) {
				throw new IllegalArgumentException(
						"SUM does not apply to " + table.getCollumnClassification(columnIndex).getDescription());
			}
		}
		if (type.equals("BOOLEAN")) {
			if (function.toString().equals("MAX") || function.toString().equals("MIN")) {
				throw new IllegalArgumentException(function + " does not apply to "
						+ table.getCollumnClassification(columnIndex).getDescription());
			}
		}
		if (condition == null) { // if there is already a condition here, then the number of rows is shorter so
									// less beneficial to use tree
			ArrayList<BTree<T>> btrees = table.getBtrees();
			for (int i = 0; i < btrees.size(); i++) {
				BTree<T> btree = btrees.get(i);
				if (btree.getColumn().equals(table.getCollumnClassification(columnIndex).getDescription().toString())) {

					if (function.toString().equals("MAX")) {
						System.out.println("getting MAX from Btree " + btree.getColumn()); // I am not accounting for a
																							// btree being empty, but
																							// this would be a users
																							// fault and should be
																							// obvious based on the
																							// result what the value
																							// means
						return (Comparable<T>) btree.getMax().toString();
					}

					if (function.toString().equals("MIN")) {
						System.out.println("getting MIN from Btree " + btree.getColumn());
						return (Comparable<T>) btree.getMin().toString();
					}

				}
			}
		}
		if (didCondition == false) {
			for (int i = 0; i < table.getNumberOfRows(); i++) {
				rows.add(table.getRow(i));
			}
		}

		if (distinct == true) {
			rows = removeDuplicateData(rows, columnIndex);
		}

		if (function.toString().equals("AVG")) {
			return (Comparable) Select.getAvg(rows, columnIndex);
		}

		if (function.toString().equals("COUNT")) {
			return (Comparable) Select.getCount(rows, columnIndex);
		}
		if (function.toString().equals("MAX")) {
			return (Comparable) Select.getMax(rows, columnIndex);
		}

		if (function.toString().equals("MIN")) {
			return (Comparable) Select.getMin(rows, columnIndex);
		}
		if (function.toString().equals("SUM")) {
			return (Comparable) Select.getSum(rows, columnIndex);

		}
		throw new IllegalArgumentException("Function can't be found");

	}

	protected static <T extends Comparable<T>> ArrayList<Row<T>> removeDistinct(ResultSet<T> tempRS,
			ArrayList<Integer> columnIndexes) {
		ArrayList<Row<T>> duplicateTester = new ArrayList<Row<T>>();

		for (int rowNumber = 0; rowNumber < tempRS.getRows().size(); rowNumber++) {

			Row<T> row = (Row<T>) tempRS.getRows().get(rowNumber);

			duplicateTester.add(row);

			for (int duplicatePosition = 0; duplicatePosition < duplicateTester.size(); duplicatePosition++) {
				if (duplicatePosition != duplicateTester.size() - 1) { // don't
																		// check
																		// the
																		// row
																		// just
																		// added
					if (isDifference(row, duplicateTester, tempRS, duplicatePosition, 0, columnIndexes)) {
						duplicateTester.remove(row);
					}
				}

			}
		}
		return duplicateTester;

	}

	protected static <T extends Comparable<T>> boolean isDifference(Row<T> row, ArrayList<Row<T>> duplicateTester,
			ResultSet<T> resultSet, int duplicatePosition, int column, ArrayList<Integer> ColumnIndexes) {
		{

			if (column < ColumnIndexes.size()) { // If it gets here
													// then every
													// column was
													// equal so it
													// must be
													// removed
			} else
				return true;
		}
		if (row.getData(ColumnIndexes.get(column)) == null
				&& duplicateTester.get(duplicatePosition).getData(ColumnIndexes.get(column)) == null) {
			return isDifference(row, duplicateTester, resultSet, duplicatePosition, ++column, ColumnIndexes);
		} else if (row.getData(ColumnIndexes.get(column)) != null
				&& duplicateTester.get(duplicatePosition).getData(ColumnIndexes.get(column)) != null) {

			if (row.getData(ColumnIndexes.get(column)).toString()
					.equals(duplicateTester.get(duplicatePosition).getData(ColumnIndexes.get(column)).toString())) {

				return isDifference(row, duplicateTester, resultSet, duplicatePosition, ++column, ColumnIndexes);
			}
		}

		return false;
	}

	protected static <T extends Comparable<T>> ResultSet<T> OrderBy(OrderBy[] order, Table<T> table, ResultSet<T> rs,
			int index) {

		if (rs.getRows().size() == 0) {
			return rs;
		}

		ArrayList<Row<T>> temp = new ArrayList<Row<T>>();
		int column = 0;
		Row<T> row = (Row<T>) rs.getRows().get(0);

		temp.add(row);
		for (int columnNumber = 0; columnNumber < rs.getDescription().size(); columnNumber++) {

			if (rs.getDescription().get(columnNumber).equals(order[index].getColumnID().getColumnName().toString())) {
				column = columnNumber;

				break;
			}
		}

		rs.setRows(orderList(rs.getRows(), column));

		if (order[0].isDescending()) {
			reverseRS(rs.getRows());
		}

		if (order.length > 1) {
			rs = orderWithinOrdered(order, index + 1, table, rs, column);
		}

		return rs;
	}

	protected static <T extends Comparable<T>> ResultSet<T> orderWithinOrdered(OrderBy[] order, int index,
			Table<T> table, ResultSet<T> rs, int previousColumn) {
		ArrayList<Row<T>> temp = new ArrayList<Row<T>>();
		ArrayList<Row<T>> orderedTemp = new ArrayList<Row<T>>();
		int column = 0;
		Row<T> row = (Row<T>) rs.getRows().get(0);
		temp.add(row);
		boolean descending = order[index].isDescending();

		for (int columnNumber = 0; columnNumber < table.getNumberOfRows(); columnNumber++) {
			if (rs.getDescription().get(columnNumber).toString()
					.equals(order[index].getColumnID().getColumnName().toString())) {
				column = columnNumber;
				break;
			}
		}

		Row<T> nextRow = null;
		rowLoop: for (int rowNumber = 1; rowNumber < rs.getRows().size(); rowNumber++) {
			Row<T> previousRow = null;

			if (rowNumber != 0) {
				previousRow = (Row<T>) rs.getRows().get(rowNumber - 1);
			}
			if (rowNumber <= rs.getRows().size() - 2) {
				nextRow = (Row<T>) rs.getRows().get(rowNumber + 1);
			} else {
				nextRow = null;
			}

			row = (Row<T>) rs.getRows().get(rowNumber);
			if (row.getData(previousColumn) != null && previousRow.getData(previousColumn) != null) {
				if (temp.size() > 1 && !row.getData(previousColumn).toString()
						.equals(previousRow.getData(previousColumn).toString())) { // there is ain the previous
																					// column and something to order
					orderedTemp = orderList(temp, column); // order the list

					if (descending == true) {
						reverseRS(orderedTemp);
					}

					for (int a = orderedTemp.size() - 1; a >= 0; a--, rowNumber--) {
						rs.getRows().set(rowNumber - 1, orderedTemp.get(a));
					}

					rowNumber += orderedTemp.size();
					temp = new ArrayList<Row<T>>();
					temp.add(row);
					continue rowLoop;

				} else if (!row.getData(previousColumn).toString()
						.equals(previousRow.getData(previousColumn).toString())) {
					temp = new ArrayList<Row<T>>();
					temp.add(row);
					continue rowLoop;

				}

				else if (previousRow != null && nextRow != null) {
					if (row.getData(previousColumn).toString().equals(previousRow.getData(previousColumn).toString())
							|| (!row.getData(previousColumn).toString().equals(previousRow.getData(previousColumn))
									&& row.getData(previousColumn).toString()
											.equals(nextRow.getData(previousColumn)))) {
						temp.add(row);

						continue rowLoop;

					}
				}
			} else if (row.getData(previousColumn) == null ^ previousRow.getData(previousColumn) == null
					&& temp.size() > 1) { // if one is true but not the other then order the list

				orderedTemp = orderList(temp, column); // order the list

				if (descending == true) {

					reverseRS(orderedTemp);
				}

				for (int a = orderedTemp.size() - 1; a >= 0; a--, rowNumber--) {
					rs.getRows().set(rowNumber - 1, orderedTemp.get(a));
				}

				rowNumber += orderedTemp.size();
				temp = new ArrayList<Row<T>>();
				temp.add(row);
				continue rowLoop;

			} else if (!(row.getData(previousColumn) == null && previousRow.getData(previousColumn) == null)) { // The
																												// opposite
																												// of
																												// above
				temp = new ArrayList<Row<T>>();
				temp.add(row);
				continue rowLoop;

			}

			else if (previousRow != null && nextRow != null) {
				temp.add(row);
				continue rowLoop;

			}
		}
		row = (Row<T>) rs.getRows().get(rs.getRows().size() - 1);
		Row<T> previousRow = (Row<T>) rs.getRows().get(rs.getRows().size() - 2);
		if (row.getData(previousColumn) == null && previousRow.getData(previousColumn) == null) {
			temp.add(row);

		} else if (row.getData(previousColumn) == null) { // just avoid the null pointer exception
		} else if (row.getData(previousColumn).equals(previousRow.getData(previousColumn))) {
			temp.add(row);
		}

		if (temp.size() > 1) { // if the previous isn't equal to
			orderedTemp = orderList(temp, column); // order the list

			if (descending == true) {
				reverseRS(orderedTemp);

			}
			int rowNumber = rs.getRows().size();
			for (int a = orderedTemp.size() - 1; a >= 0; a--, rowNumber--) {
				rs.getRows().set(rowNumber - 1, orderedTemp.get(a));
			}
		}

		if (index < order.length - 1) {
			rs = orderWithinOrdered(order, index + 1, table, rs, column);
		}

		return rs;

	}

	private static <T extends Comparable<T>> ArrayList<Row<T>> reverseRS(ArrayList<Row<T>> orderedTemp) {
		for (int i = 0; i < orderedTemp.size() / 2; i++) {
			Row<T> temp = orderedTemp.get(i);
			orderedTemp.set(i, orderedTemp.get(orderedTemp.size() - 1 - i));
			orderedTemp.set(orderedTemp.size() - 1 - i, temp);

		}
		return orderedTemp;
	}

	protected static <T extends Comparable<T>> ArrayList<Row<T>> orderList(ArrayList<Row<T>> toSort, int column) {

		ArrayList<Row<T>> temp = new ArrayList<Row<T>>();
		Row<T> row = toSort.get(0);
		temp.add(row);

		for (int rowNumber = 1; rowNumber < toSort.size(); rowNumber++) {
			row = toSort.get(rowNumber);
			if (row.getData(column) == null) {
				temp.add(0, row); // nulls go at the front
			}
		}

		RowLoop: for (int rowNumber = 1; rowNumber < toSort.size(); rowNumber++) {
			row = toSort.get(rowNumber);
			if (row.getData(column) == null) {
				continue RowLoop; // nulls are sorted by now
			}
			if (temp.size() == 0) {
				temp.add(row);
			}

			for (int j = 0; j < temp.size(); j++) {
				if (temp.get(j).getData(column) != null) {

					if (temp.get(j).getData(column).compareTo(row.getData(column)) > 0) {
						temp.add(j, row);
						continue RowLoop;
					} else {
						if (j + 1 >= temp.size()) { // this is the last element
							temp.add(row);
							continue RowLoop;
						}
					}
				} else if (j + 1 >= temp.size()) {
					temp.add(row);
					continue RowLoop;
				}
			}

		}

		return temp;

	}

	protected static <T extends Comparable<T>> ResultSet<T> addAllColumns(int numberOfColumns, ResultSet<T> resultSet,
			Table<T> table, SelectQuery query, Condition condition) throws IllegalAccessException {
		for (int columnName = 0; columnName < numberOfColumns; columnName++) {
			resultSet.addDescriber(table.getCollumnClassification(columnName));
		}
		if (condition != null) {
			ArrayList<Row<T>> checked = ConditionFinder.checkCondition(condition, table, new ArrayList<Row<T>>(), 0);
			for (int i = 0; i < checked.size(); i++) {
				resultSet.getRows().add(checked.get(i));
			}
		} else {
			for (int rowNumber = 0; rowNumber < table.getNumberOfRows(); rowNumber++) {
				resultSet.getRows().add(table.getRow(rowNumber));
			}
		}
		return resultSet;

	}

	protected static <T extends Comparable<T>> ResultSet<T> addColumns(int numberOfColumns, ResultSet<T> resultSet,
			Table<T> table, Condition condition, ColumnID[] var, ArrayList<String> functionsCollumed, OrderBy[] orderby,
			SelectQuery query) throws IllegalAccessException {
		ResultSet<T> tempRS = new ResultSet();

		ArrayList<Row<T>> checked = null;

		if (condition != null) {
			checked = ConditionFinder.checkCondition(condition, table, new ArrayList<Row<T>>(), 0);
		} else {
			checked = table.getRowData();
		}

		tempRS.setRows(checked);

		for (int i = 0; i < table.getNumberOfCollumns(); i++) {
			tempRS.addDescriber(table.getCollumnClassification(i));
		}

		if (query.isDistinct()) { // before ordering take the distinct so it takes first of
									// each.
			ArrayList<Integer> ColumnIndexes = new ArrayList<Integer>();
			for (int i = 0; i < var.length; i++) {
				ColumnIndexes.add(getColumn(table, var[i].getColumnName()));
			}
			tempRS.setRows(Select.removeDistinct(tempRS, ColumnIndexes));

		}
		if (orderby.length != 0) {
			tempRS = Select.OrderBy(orderby, table, tempRS, 0);
		}

		checked = tempRS.getRows();

		boolean addedDescribers = false;
		if (checked != null) {
			for (int i = 0; i < checked.size(); i++) {
				Row<T> row = resultSet.addRow(numberOfColumns);
				for (int column = 0; column < var.length; column++) {
					int index = getColumn(table, var[column].getColumnName().toString());

					T data = (T) checked.get(i).getData(index);
					if (addedDescribers == false) {
						resultSet.addDescriber(table.getCollumnClassification(index));
					}

					row.setData(data, column);
				}

				addedDescribers = true;

			}
			if (addedDescribers == false) {
				for (int column = 0; column < var.length; column++) {
					int index = getColumn(table, var[column].getColumnName().toString()); // wouldve fixed with index
																							// array
					resultSet.addDescriber((Classification) table.getCollumnClassification(index));
				}
			}
		}

		return resultSet;
	}

	protected static <T extends Comparable<T>> void deleteRows(Table<T> table, Condition condition)
			throws IllegalAccessException {
		if (condition == null) {
			deleteAll(table);
			return;
		}

		ArrayList<Row<T>> rows = ConditionFinder.checkCondition(condition, table, new ArrayList<Row<T>>(), 0);

		removefromBtree(table, rows);

		for (int i = 0; i < rows.size(); i++) {
			Row<T> row = rows.get(i);
			for (int p = 0; p < table.getNumberOfRows(); p++) {
				if (table.getRow(p).equals(row)) {
					table.removeRow(p);
				}
			}
		}

	}

	private static <T extends Comparable<T>> void deleteAll(Table<T> table) {

		removefromBtree(table, table.getRowData());

		int size = table.getNumberOfRows();
		for (int i = 0; i < size; i++) {
			table.removeRow(0);
		}

	}

	private static <T extends Comparable<T>> void removefromBtree(Table<T> table, ArrayList<Row<T>> rowData) {
		int columnNumber = -1;
		ArrayList<BTree<T>> btrees = table.getBtrees();

		for (int i = 0; i < btrees.size(); i++) {
			BTree<T> btree = (BTree<T>) btrees.get(i);
			for (int z = 0; z < table.getNumberOfCollumns(); z++) {
				if (btree.getColumn().equals(table.getCollumnClassification(z).getDescription().toString())) {
					columnNumber = z;
					break;
				}

			}
			ArrayList<Row<T>> copyRows = new ArrayList<Row<T>>();

			for (int q = 0; q < rowData.size(); q++) {
				copyRows.add(rowData.get(q));
			}

			for (int rowNumber = 0; rowNumber < copyRows.size(); rowNumber++) {
				Row<T> row = copyRows.get(rowNumber);
				btree.remove((T) row.getData(columnNumber), row);
			}
		}
	}

	@SuppressWarnings("unchecked") // by definition result will extend comparable
	protected static <T extends Comparable<T>> ResultSet<T> getFunctionData(ArrayList<FunctionInstance> functionArray,
			Table<T> table, Condition condition) throws IllegalAccessException {
		ResultSet<T> resultSet = new ResultSet<T>();
		Row<T> row = resultSet.addRow(functionArray.size());

		for (int i = 0; i < functionArray.size(); i++) {
			boolean distinct = functionArray.get(i).isDistinct;
			FunctionName function = functionArray.get(i).function;
			int columnIndex = Select.getColumn(table, functionArray.get(i).column.getColumnName().toString());
			resultSet.addDescriber(table.getCollumnClassification(columnIndex));
			String result = Select.evaluate(distinct, function, table, functionArray.get(i).column.getColumnName(),
					columnIndex, condition).toString();

			result = function + ":" + result;
			if (distinct) {
				result = result + "(DIST)";
			}
			row.setData((T) result, i);

		}
		return resultSet;

	}

}
