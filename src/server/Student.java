package server;

import java.io.*;
import java.util.*;

public class Student extends User {

    private long phoneNumber;
    private ArrayList<Course> enrolledCourses = new ArrayList<>();
    private ArrayList<Hold> holds = new ArrayList<>();
    private double balance;
    private int unitCap = 18;

    public Student(String name, String password, long phoneNumber) {
        super(name, password);
        this.phoneNumber = phoneNumber;
        this.balance = 0.0;
    }
	
	public Role getRole() {
		return Role.STUDENT;
	}

	
	public void dropCourse(Course course) { 
		enrolledCourses.remove(course); 
	}
	
	public ArrayList<Course> getCourseList() {
		return enrolledCourses;
	}
	
	public boolean hasHold() {
		if(holds.isEmpty()) {
			return false;
		}
		return true;
	}
	
	public ArrayList<Hold> getHold(){
		return holds;
	}
	
	public void setHold(Hold hold) {
		holds.add(hold);
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public void applyPayment(double amt) { 
        balance = balance - amt;
    }

    // ✅ Enroll in course with unit + hold checks
    public String enrollInCourse(Course course) {
        if (course == null) {
            return "null";
        }
        if (hasHold()) {
            return "hold";
        }
        if (!canEnroll(course.getUnits())) {
            return "units";
        }
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
        }
        return "YAY";
    }

    public void addCourse(Course course) {
        if (course != null && !enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
        }
    }
    
    public ArrayList<String> getCorses() {
		
		ArrayList<String> coursee = new ArrayList<>();
		
		for(Course course: enrolledCourses) {
			coursee.add(course.toString());
		}
		
		return coursee;
	}


    public boolean canEnroll(int unitsToAdd) {
        int currentUnits = enrolledCourses.stream()
                .mapToInt(Course::getUnits)
                .sum();
        return currentUnits + unitsToAdd <= unitCap;
    }

    public void save() {
        try {
            String folder = "data/";
            File dir = new File(folder);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(folder + name));
            // ✅ Save balance with the first line
            writer.write(name + "," + password + "," + phoneNumber + "," + balance);
            writer.newLine();

            for (Course course : enrolledCourses) {
                writer.write(course.getTitle());
                writer.newLine();
            }

            writer.close();
            System.out.println("Saved file to " + folder + name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return name + "," + password + "," + phoneNumber;
    }

}
