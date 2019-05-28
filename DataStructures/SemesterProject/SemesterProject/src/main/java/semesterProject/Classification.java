
package semesterProject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription.DataType;

/*
 * A class to classify data. The table itself also keeps track of 
 */

class Classification {

	private DataType type;
	private String description;
	private Boolean isDefault;
	private boolean isUnique;
	private boolean isNull;
	private int varCharLength;
	private int integerLength;
	private int fractionalLength;
	private String defaultValue;

	protected Classification(ColumnDescription columnDescription) {

		setType(columnDescription.getColumnType());
		setDescription(columnDescription.getColumnName());
		setIsDefault(columnDescription.getHasDefault());
		isNull = columnDescription.isNotNull();
		varCharLength = columnDescription.getVarCharLength();
		fractionalLength = columnDescription.getFractionLength();
		integerLength = columnDescription.getWholeNumberLength();
		setDefaultValue(columnDescription.getDefaultValue());
		isUnique = columnDescription.isUnique();

	}

	protected Classification(String columnName) {
		setDescription(columnName);

	}

	protected boolean getIsUnique() {
		return isUnique;
	}

	protected boolean getIsNull() {
		return isNull;
	}

	protected void setIsUnique(Boolean isUnique) {
		this.isUnique = isUnique;
	}

	protected Boolean isDefault() {
		return isDefault;
	}

	protected void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	protected String getDescription() {
		return description;
	}

	protected void setDescription(String description) {
		this.description = description;

	}

	protected DataType getType() {
		return type;
	}

	protected void setType(DataType type) {
		this.type = type;
	}

	protected int getVarCharLength() {
		return varCharLength;
	}

	protected int getIntegerLength() {
		return integerLength;
	}

	protected int getFractionalLength() {
		return fractionalLength;
	}

	protected void setIsNull(Boolean b) {
		isNull = b;
	}

	protected String getDefaultValue() {
		return defaultValue;
	}

	protected void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}



}
