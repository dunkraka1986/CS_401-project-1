package server;

import java.io.*;
import java.util.*;

public class University {
	
	static private int uniqueId = 0;
	private int id;
	private String title;
	ArrayList<Student> students = new ArrayList<Student>();
	ArrayList<Course> courses = new ArrayList<Course>();
	
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
	
	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}
	
	public Student getStudentByName(String name) {
	    for (Student student : students) {
	        if (student.getName().equalsIgnoreCase(name)) {
	            return student;
	        }
	    }
	    return null;
	}
	
	public List<Student> getAllStudents() {
	    return new ArrayList<>(students);
	}

	public Course getCourseByTitle(String title) {
	    for (Course course : courses) {
	        if (course.getTitle().equalsIgnoreCase(title)) {
	            return course;
	        }
	    }
	    return null;
	}
	
	public ArrayList<Course> getCorses(){
		return courses;
	}
	
	public ArrayList<String> getCorse() {
		
		ArrayList<String> coursee = new ArrayList<>();
		
		for(Course course: courses) {
			coursee.add(course.toString());
		}
		
		return coursee;
	}
	
	
	public void addCourse(Course course) {
		this.courses.add(course);
	}
	
	public List<Student> getStudents(){
		return this.students;
	}
	
	public void loadStudents() {
	    File folder = new File("data/");
	    if (!folder.exists() || !folder.isDirectory()) {
	        System.out.println("Data folder not found. No students loaded.");
	        return;
	    }

	    File[] files = folder.listFiles();
	    if (files == null) {
	        System.out.println("No student files found in data folder.");
	        return;
	    }

	    for (File file : files) {
	        if (file.isFile()) {
	            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	                String line = reader.readLine(); // Read first line: name,password,phone
	                if (line != null) {
	                    String[] parts = line.split(",");
	                    if (parts.length >= 3) {
	                        String name = parts[0].trim();
	                        String password = parts[1].trim();
	                        long phoneNumber = Long.parseLong(parts[2].trim());

	                        Student student = new Student(name, password, phoneNumber);

	                        while ((line = reader.readLine()) != null) {
	                            String courseTitle = line.trim();
	                            Course course = getCourseByTitle(courseTitle);
	                            if (course != null) {
	                                student.addCourse(course);
	                                course.addStudent(student);
	                            } else {
	                                System.out.println("Warning: Course '" + courseTitle + "' not found for student '" + name + "'");
	                            }
	                        }

	                        this.addStudent(student);
	                        System.out.println("Loaded student: " + name);
	                    }
	                }
	            } catch (IOException e) {
	                System.out.println("Failed to load student from file: " + file.getName());
	                e.printStackTrace();
	            }
	        }
	    }
	}
}
