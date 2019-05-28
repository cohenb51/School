package semesterProject;


/*
 * Just used to store 1 piece of data. Data is final.
 */
class Data<T extends Comparable<T>> {
	
	private T data;
	
	protected Data(T data) {
		this.data = data;
	}

	protected T getData() {
		return this.data;
	}

	

	

}
