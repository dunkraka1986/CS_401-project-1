package Server;

import java.util.*;

public class Admin extends User{
	
	private String email;
	
	public Admin(String name, String password) {
		super(name, password);
	}
	
	// methods
	public Course createCourse() {
		return null;
	}
	
	public void updateCourse() {
		
	}
	
	public void deleteCourse(Course course) {
		
	}
	
	public void generateReport() {
		
	}
	
	public Role getRole() {
		return Role.STUDENT;
	}
	
	// getters and setters
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return this.email;
	}
	
}