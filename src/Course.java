import java.io.IOException;
import java.util.ArrayList;

public class Course {
	static private int uniqueId = 0;
	private int courseID;
	private String title;
	private String description;
	private int capacity;
	private int units;
	private ArrayList<String> prerequisites;
	private ArrayList<Student> enrolledStudents;
	private ArrayList<Student> waitlistedStudents;
	
	public Course() {
		this.courseID = 0;
		this.title = null;
		this.description = null;
		this.capacity = 0;
		this.prerequisites = new ArrayList<String>();
		this.enrolledStudents = new ArrayList<Student>();
	}
	
	public Course(String title, String description, int capacity, int units, ArrayList<String> prerequisites) {
		this.courseID = ++uniqueId;
		this.title = title;
		this.description = description;
		this.capacity = capacity;
		this.units = units;
		this.prerequisites = prerequisites;
		this.enrolledStudents = new ArrayList<Student>();
	}
	// methods
	public boolean addStudent(Student student) {
		boolean added = false;
		
		// if enrolled students is not full then add, else return false and dont add
		if (!this.isFull()) {
			this.enrolledStudents.add(student);
			added = true;
		}
		
		return added;
	}
	
	public boolean removeStudent(Student student) {
		boolean removed = false;
		
		// changes to true if removed, false if not found or not removed
		removed = this.enrolledStudents.remove(student);
		
		return removed;
	}
	
	public boolean isFull() {
		boolean full = false;
		
		// logic to check if course is full
		if (this.capacity >= this.enrolledStudents.size()) {
			full = true;
		}
		
		return full;
	}
	
	public void addPrerequisite(String prerequisite) {
		this.prerequisites.add(prerequisite);
	}
	
	public void removePrerequisite(String prerequisite) {
		this.prerequisites.remove(prerequisite);
	}
	
	public void addToWaitlist(Student student) {
		this.waitlistedStudents.add(student);
	}
	
	public void removeFromWaitlist(Student student) {
		this.waitlistedStudents.remove(student);
	}
	
	// getters and setters
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public void setUnits(int unit) {
		this.units = unit;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getCourseID() {
		return this.courseID;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public int getCapacity() {
		return this.capacity;
	}
	
	public int getUnits() {
		return this.units;
	}
	
	public String getDescription() {
		return this.description;
	}
}
