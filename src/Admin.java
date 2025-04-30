import java.util.*;

public class Admin extends User{	
	public Admin(String id, String name, String password) {
		super(id, name, password);
	}
	
	// methods
	public Course createCourse() {

	}
	
	public void updateCourse() {
		
	}
	
	public void deleteCourse(Course course) {
		
	}
	
	public void generateReport() {
		
	}
	
	// getters and setters
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return this.email;
	}
	
}