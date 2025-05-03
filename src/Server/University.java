package Server;

import java.util.*;

public class University {
	
	static private int uniqueId = 0;
	private int id;
	private String title;
	List<Student> students = new ArrayList<Student>();
	List<Course> courses = new ArrayList<Course>();
	
	public University() {
		this.id = 0;
		this.title = null;
	}
	
	public University(String title) {
		this.id = ++uniqueId;
		this.title = title;
	}
	
	public void addStudent(Student student) {
		students.add(student);
	}
	
	public void removeStudent(Student student) {
		students.remove(student);
	}
}
