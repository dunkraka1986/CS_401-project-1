import java.util.ArrayList;

public class Course {
	static private int uniqueId = 0;
	private int id;
	private String title;
	private String description;
	private int capacity;
	private ArrayList<Course> prerequisites;
	private ArrayList<Student> enrolledStudents;
	
	public Course() {
		this.id = 0;
		this.title = null;
		this.description = null;
		this.capacity = 0;
		this.prerequisites = new ArrayList<Course>();
		this.enrolledStudents = new ArrayList<Student>();
	}
	
	public Course(String title, String description, int capacity, ArrayList<Course> prerequisites) {
		this.id = ++uniqueId;
		this.title = title;
		this.description = description;
		this.capacity = capacity;
		this.prerequisites = prerequisites;
	}
}
