
public class Course {
	// attributes
	private String courseID;
	static private int courseCount = 0;
	private String title;
	private int capacity;
	private String[] prerequisites;
	private Students[] enrolledStudents;
	private Waitlist waitlist;
	
	public Course() {
		this.courseID = "" + courseCount++;
		this.capacity = 30; // default class size
		this.enrolledStudents = new Students[this.capacity];
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