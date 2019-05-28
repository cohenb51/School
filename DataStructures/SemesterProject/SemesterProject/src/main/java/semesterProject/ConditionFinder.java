package semesterProject;

import java.util.ArrayList;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition.Operator;

class ConditionFinder {
	static <T extends Comparable<T>> ArrayList<Row<T>> checkCondition(Condition condition, Table<T> table,
			ArrayList<Row<T>> total, int rowIndex) throws IllegalAccessException {

		if (!condition.getOperator().toString().equals("AND") && !condition.getOperator().toString().equals("OR")) { // if
																														// there
																														// is
																														// no
																														// and/or
																														// it
																														// is
																														// a
																														// simple
																														// condition
			return getSimpleCondition(condition, table, total, rowIndex);
		} else {
			return getComplexCondition(condition, table, total, rowIndex);

		}

	}

	private static <T extends Comparable<T>> ArrayList<Row<T>> getSimpleCondition(Condition condition, Table<T> table,
			ArrayList<Row<T>> total, int rowIndex) throws IllegalAccessException {

		String leftOperand = condition.getLeftOperand().toString();
		@SuppressWarnings("unchecked")
		Comparable<T> rightOperand = (Comparable<T>) condition.getRightOperand();
		Operator operator = condition.getOperator();

		BTree<T> btree = Helpers.getBtree(table, leftOperand);
		if (btree != null) {
			total = getBTreeRows(condition, btree);
			return total;
		}
		for (int rowNumber = 0; rowNumber < table.getNumberOfRows(); rowNumber++) { // if no btree search rows
			Row<T> row = table.getRow(rowNumber);
			int columnNumber = Select.getColumn(table, leftOperand);
			boolean match = checkRowCondition(row, operator, columnNumber, rightOperand);

			if (match == true) {
				total.add(row);
			}

		}

		return total;
	}

	private static <T extends Comparable<T>> ArrayList<Row<T>> getComplexCondition(Condition condition, Table<T> table,
			ArrayList<Row<T>> total, int rowIndex) throws IllegalAccessException {

		boolean rightFlag = false;
		boolean leftFlag = false;
		ArrayList<Row<T>> temp = new ArrayList<Row<T>>(); // rows for the left side
		ArrayList<Row<T>> temp2 = new ArrayList<Row<T>>(); // rows for the right side
		ArrayList<Row<T>> result = new ArrayList<Row<T>>(); // after and/or
		Condition rightOperand = null;
		Condition leftOperand = null;

		if (condition.getRightOperand() instanceof Condition) {
			rightOperand = (Condition) ((Condition) condition).getRightOperand();
		}

		if (condition.getLeftOperand() instanceof Condition) {
			leftOperand = (Condition) ((Condition) condition).getLeftOperand();
		}
		boolean leftBtreeFlag = false;
		if (leftOperand != null) {
			if (!leftOperand.getOperator().toString().equals("AND")
					&& !leftOperand.getOperator().toString().equals("OR")) { // thus the left side is a simple condition
				leftFlag = true;
				BTree<T> btree = Helpers.getBtree(table, leftOperand.getLeftOperand()); // same as getSimple condition.
																						// Was easier to test it though
																						// keeping it seperate.
				if (btree != null) {
					temp2 = getBTreeRows(leftOperand, btree);
					leftBtreeFlag = true;
				}

				for (int rowNumber = 0; rowNumber < table.getNumberOfRows() && !leftBtreeFlag; rowNumber++) {
					Row<T> row = table.getRow(rowNumber);
					if (leftOperand.getLeftOperand() instanceof Condition) {
						break;
					}
					int columnNumber = Select.getColumn(table, leftOperand.getLeftOperand().toString());
					boolean match = checkRowCondition(row, (Operator) leftOperand.getOperator(), columnNumber,
							leftOperand.getRightOperand());

					if (match == true) {
						temp2.add(row);
					}

				}
			}
			boolean rightBtreeFlag = false;
			if (rightOperand != null) {

				if (!rightOperand.getOperator().toString().equals("AND")
						&& !rightOperand.getOperator().toString().equals("OR")) {
					rightFlag = true;
					BTree<T> btree = Helpers.getBtree(table, rightOperand.getLeftOperand());
					if (btree != null) {
						temp = getBTreeRows(rightOperand, btree);
						rightBtreeFlag = true;

					}

				}

			}
			for (int rowNumber = 0; rowNumber < table.getNumberOfRows() && !rightBtreeFlag; rowNumber++) {
				Row<T> row = table.getRow(rowNumber);
				if (rightOperand.getLeftOperand() instanceof Condition) {
					break;
				}
				int columnNumber = Select.getColumn(table, rightOperand.getLeftOperand().toString());
				boolean match = checkRowCondition(row, (Operator) rightOperand.getOperator(), columnNumber,
						rightOperand.getRightOperand());
				if (match == true) {
					temp.add(row);

				}

			}

			// if there is an and/or then the condition must be broken down more
		}
		if (leftFlag == false) {
			temp2 = checkCondition(leftOperand, table, total, rowIndex); // each call will return the rows from left
																			// side broken down up to here

		}
		if (rightFlag == false) {
			temp = checkCondition(rightOperand, table, total, rowIndex);
		}

		if (((Condition) condition).getOperator().toString().indexOf("AND") != -1) {
			int tempSize = temp.size();
			int temp2Size = temp2.size();
			for (int a = 0; a < tempSize; a++) {
				for (int b = 0; b < temp2Size; b++) {
					if (temp.get(a).hashCode() == temp2.get(b).hashCode()) { // just check their memory locations and
																				// see if they are the same object
						result.add(temp.get(a));
					}
				}
			}

		}
		if (((Condition) condition).getOperator().toString().indexOf("OR") != -1) {
			if (temp != null) {
				for (int a = 0; a < temp.size(); a++) {
					result.add(temp.get(a));
				}
			}
			if (temp2 != null) {
				for (int b = 0; b < temp2.size(); b++) {
					result.add(temp2.get(b));
				}
			}
			
			Select.removeDuplicates(result); // since there will be duplicates. Also could've used hashet to fix time

		}

		// LinkedHashSet<Row> temp9 = new LinkedHashSet<Row>(result);
		// ArrayList<Row> temp8 = new ArrayList<Row>(temp9);
		
		
		return result;
	}

	private static <T extends Comparable<T>> boolean checkRowCondition(Row<T> row, Operator operator, int column,
			Object value) throws IllegalAccessException {
		if(value instanceof String) {
			if (((String) value).charAt(0) == 39) {
				value = ((String) value).substring(1, ((String) value).length() - 1);
			
			}
		}
		
		if (operator.toString().equals("<")) {
			if (value.toString().equals("NULL")) {
				throw new IllegalAccessException("Null is neither greater nor less than any number");
			}
			return checkLess(row, column, value);
		} else if (operator.toString().equals("<=")) {
			if (value.toString().equals("NULL")) {
				throw new IllegalAccessException("Null is neither greater nor less than any number");
			}
			return checkLessOrEqual(row, column, value);
		}

		else if (operator.toString().equals(">")) {
			if (value.toString().equals("NULL")) {
				throw new IllegalAccessException("Null is neither greater nor less than any number");
			}
			return checkGreater(row, column, value);
		}

		else if (operator.toString().equals(">=")) {
			if (value.toString().equals("NULL")) {
				throw new IllegalAccessException("Null is neither greater nor less than any number");
			}
			return checkGreaterOrEqual(row, column, value);
		} else if (operator.toString().equals("<>")) {
			return checkNotEquals(row, column, value);
		} else if (operator.toString().equals("=")) {
			return checkEquals(row, column, value);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>> ArrayList<Row<T>> getBTreeRows(Object condition, BTree<T> btree)
			throws IllegalAccessException {
		ArrayList<Row<T>> result = null;
		String type = btree.getType();
		T data = null;
		if (((Condition) condition).getRightOperand() instanceof Comparable) {
			T rightOperand = (T) ((Condition) condition).getRightOperand();
			data = rightOperand;
			if (!data.toString().equals("NULL")) {
				data = (T) Helpers.parseType(((Condition) condition).getRightOperand().toString(), type);
			}

		}
		if (((Condition) condition).getOperator().toString().equals("=")) {
			result = btree.get(data);
			if (!data.toString().equals("NULL")) {

				System.out.println(result.size() + " rows gotten from " + condition.toString() + " in Btree for column "
						+ btree.getColumn());
			}
			return result;

		} else if (((Condition) condition).getOperator().toString().equals("<")) {
			if (data.toString().equals("NULL")) {
				throw new IllegalAccessException("Null is neither greater nor less than any number");
			}
			result = btree.getLessThan(data);
			System.out.println(result.size() + " rows gotten from " + condition.toString() + " in Btree for column "
					+ btree.getColumn());
			return result;

		} else if (((Condition) condition).getOperator().toString().equals("<=")) {
			if (data.toString().equals("NULL")) {
				throw new IllegalAccessException("Null is neither greater nor less than any number");
			}
			result = btree.getLessOrEqualThan(data);
			System.out.println(result.size() + " rows gotten from " + condition.toString() + " in Btree for column "
					+ btree.getColumn());

			return result;
		}

		else if (((Condition) condition).getOperator().toString().equals(">")) {
			if (data.toString().equals("NULL")) {
				throw new IllegalAccessException("Null is neither greater nor less than any number");
			}
			result = btree.getGreaterThan(data);
			System.out.println(result.size() + " rows gotten from " + condition.toString() + " in Btree for column "
					+ btree.getColumn());

			return result;
		}

		else if (((Condition) condition).getOperator().toString().equals(">=")) {
			if (data.toString().equals("NULL")) {
				throw new IllegalAccessException("Null is neither greater nor less than any number");
			}
			result = btree.getGreaterOrEqualsThan(data);
			System.out.println(result.size() + " rows gotten from " + condition.toString() + " in Btree for column "
					+ btree.getColumn());

			return result;
		} else if (((Condition) condition).getOperator().toString().equals("<>")) {

			result = btree.getNotEquals(data);
			if (result == null) {
				result = new ArrayList<Row<T>>();
			}
			
			if (!data.toString().equals("NULL")) {
				System.out.println(result.size() + " rows gotten from " + condition.toString() + " in Btree for column "
						+ btree.getColumn());
			}
			return result;

		}
		return null;

	}

	private static <T extends Comparable<T>> boolean checkNotEquals(Row<T> row, int columnNumber, Object rightOperand) {
		if (row.getData(columnNumber) == null) {
			if (row.getData(columnNumber) == null) {
				return false;
			} else {
				return true;
			}
		}

		if (row.getData(columnNumber) == null) {
			return false;
		}
		if (!row.getData(columnNumber).toString().equals(rightOperand.toString())) {
			return true;

		}

		return false;

	}

	private static <T extends Comparable<T>> boolean checkGreaterOrEqual(Row<T> row, int columnNumber,
			Object rightOperand) {
		if (row.getData(columnNumber) == null) {
			return false;
		}
		if (row.getData(columnNumber).toString().compareTo(rightOperand.toString()) >= 0) {
			return true;
		}

		return false;

	}

	private static <T extends Comparable<T>> boolean checkGreater(Row<T> row, int columnNumber, Object rightOperand) {
		if (row.getData(columnNumber) == null) {
			return false;
		}
		if (row.getData(columnNumber).toString().compareTo(rightOperand.toString()) > 0) {
			return true;
		}
		return false;
	}

	private static <T extends Comparable<T>> boolean checkLessOrEqual(Row<T> row, int columnNumber,
			Object rightOperand) {
		if (row.getData(columnNumber) == null) {
			return false;
		}

		if (row.getData(columnNumber).toString().compareTo(rightOperand.toString()) <= 0) {
			return true;
		}

		return false;

	}

	private static <T extends Comparable<T>> boolean checkLess(Row<T> row, int columnNumber, Object rightOperand) {
		if (row.getData(columnNumber) == null) {
			return false;
		}

		if (row.getData(columnNumber).toString().compareTo(rightOperand.toString()) < 0) {
			return true;

		}

		return false;
	}

	private static <T extends Comparable<T>> boolean checkEquals(Row<T> row, int columnNumber, Object value) {
		if (value.toString().equals("NULL")) {
			if (row.getData(columnNumber) == null) {
				return true;
			} else {
				return false;
			}
		}
		if (row.getData(columnNumber) == null) {
			return false;
		}
		if (row.getData(columnNumber).toString().equals(value.toString())) {
			return true;

		}
		return false;
	}
}
