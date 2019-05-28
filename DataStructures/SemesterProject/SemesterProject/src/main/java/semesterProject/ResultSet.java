package semesterProject;

import java.util.ArrayList;


class ResultSet<T extends Comparable<T>> {
	
	private ArrayList<String> description;
	private ArrayList<String> dataType;
	private ArrayList<Row<T>> rows;

	protected ResultSet() {
		setDescription(new ArrayList<String>());
		setDataType(new ArrayList<String>());
		setRows(new ArrayList<Row<T>>());
	}

	protected void addDescriber(Classification classification) {
		getDescription().add(classification.getDescription());
		getDataType().add(classification.getType().toString());
		

	}

	protected ArrayList<Row<T>> getRows() {
		return rows;
	}

	

	protected Row<T> addRow(int columns) {
		Row<T> row = new Row<T>(columns);
		getRows().add(row);
		return row;

	}

	@SuppressWarnings("unchecked")
	protected void setFalse(@SuppressWarnings("rawtypes") ResultSet rs) { // just a string
		Row<T> row = rs.addRow(1);
		row.setData((T) "false", 0);
		
	}

	@SuppressWarnings("unchecked")
	protected void setTrue(@SuppressWarnings("rawtypes") ResultSet rs) { //in this case it's just a string
		Row<T> row = rs.addRow(1);
		row.setData((T) "true", 0);
	}

	protected ArrayList<String> getDescription() {
		return description;
	}

	protected void setDescription(ArrayList<String> description) {
		this.description = description;
	}

	protected ArrayList<String> getDataType() {
		return dataType;
	}

	protected void setDataType(ArrayList<String> dataType) {
		this.dataType = dataType;
	}

	protected void setRows(ArrayList<Row<T>> rows) {
		this.rows = rows;
	}

	

	
}


