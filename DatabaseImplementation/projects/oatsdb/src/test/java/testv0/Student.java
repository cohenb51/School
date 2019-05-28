package testv0;

public class Student  {
	
	private String name;
	private Double GPA;
	
	public Student(int number) {
		
	}
	public Student() {
		
	}
	
	public Student(String name, double d) {
		this.name = name;
		this.GPA = d;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Double getGPA() {
		return GPA;
	}
	
	public void setGPA(Double GPA) {
		GPA = GPA;
	}

	public static Student createStudent() {
		return new Student("benny", 0.00);
	}
 

}
