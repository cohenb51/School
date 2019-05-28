package semesterProject;

import java.util.ArrayList;

//Just a helper class that I built because of an emergency with select function tables not working. 
public class MyMap {
	private ArrayList<String> columnName;
	private ArrayList<Integer> timesFound;
	
	protected MyMap() {
		columnName = new ArrayList<String>();
		timesFound = new ArrayList<Integer>();
	}

	protected void put(String column) {
		columnName.add(column);
		timesFound.add(0);
		
	}

	protected ArrayList<String> getColumnName() {
		return columnName;
	}

	public void found(int index){
		int temp = timesFound.get(index);
		temp++;
		timesFound.set(index, temp);
		
		
	}
	
	protected Integer getTimesFound(int index) {
		return timesFound.get(index);
	}
	
	protected int size() {
		return columnName.size();
	}
	

}
