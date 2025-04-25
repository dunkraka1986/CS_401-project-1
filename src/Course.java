import java.util.ArrayList;

public class Course {
	static private int uniqueId = 0;
	private int courseID;
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
	// methods
	public boolean addStudent(Student student) {
		boolean added = false;
		
		// logic to add student
		
		return added;
	}
	
	public boolean removeStudent(Student student) {
		boolean removed = false;
		
		// logic to remove student
		
		return removed;
	}
	
	public boolean isFull() {
		boolean full = false;
		
		// logic to check if course is full
		if (this.capacity >= this.enrolledStudents.length()) {
			full = true;
		}
		
		return full;
	}
	
	// getters and setters
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public int getCapacity() {
		return this.capacity;
	}
}
