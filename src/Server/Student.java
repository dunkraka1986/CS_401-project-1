package server;

import java.io.*;
import java.util.*;

public class Student extends User{
	
	private ArrayList<Course> enrolledCourses = new ArrayList<Course>();
	private ArrayList<Hold> Holds = new ArrayList<Hold>();
	private double balance;
	private int unitCap = 18;
	
	public Student(String name, String password) { 
        super(name, password);
    }
	
	public Role getRole() {
		return Role.STUDENT;
	}
	
	public void addCourse(Course course) {
		if(hasHold()) {
			return;
		}
		if(canEnroll(course.getUnits())) {
			return;
		}
		
		enrolledCourses.add(course);
	}
	
	public void dropCourse(Course course) { 
		enrolledCourses.remove(course); 
	}
	
	public ArrayList<Course> getCourseList() {
		return enrolledCourses;
	}
	
	public boolean hasHold() {
		if(Holds.isEmpty()) {
			return false;
		}
		return true;
	}
	
	public ArrayList<Hold> getHold(){
		return Holds;
	}
	
	public void setHold(Hold hold) {
		Holds.add(hold);
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
	
	public boolean canEnroll(int unit) {
		int maxUnit = 0;
		for(Course course: enrolledCourses) {
			maxUnit += course.getUnits();
		}
		
		maxUnit += unit;
		
        return maxUnit <= unitCap; 
    }
	
	public void save() {
	    try {
	        String folder = "data/";
	        File dir = new File(folder);
	        if (!dir.exists()) {
	            if (dir.mkdirs()) {
	            } else {
	            }
	        }

	        BufferedWriter writer = new BufferedWriter(new FileWriter(folder + name));
	        writer.write(name + "," + password);
	        writer.newLine();

	        for (int i = 0; i < enrolledCourses.size(); i++) {
	            writer.write(enrolledCourses.get(i).toString());
	            writer.newLine();
	        }

	        writer.close();
	        System.out.println("Saved file to " + folder + name);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	
	public String toString() {
		return name + "," + password;
	}
}