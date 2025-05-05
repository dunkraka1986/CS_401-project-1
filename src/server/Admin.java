package server;

import java.util.*;
import server.Payment;
import server.ReportLogger;


public class Admin extends User {

    private List<String> permissions = new ArrayList<>();

    public Admin(String name, String password) {
        super(name, password);
    }

    public Role getRole() {
        return Role.ADMIN;
    }

    // Admin can enroll a student in a course
    public boolean enrollStudentInCourse(Student student, Course course) {
        if (student.hasHold()) {
            System.out.println("Cannot enroll. Student has a hold.");
            return false;
        }

        if (!student.canEnroll(course.getUnits())) {
            System.out.println("Cannot enroll. Unit limit exceeded.");
            return false;
        }

        boolean added = course.addStudent(student);
        if (added) {
            student.addCourse(course);
            return true;
        } else {
            course.addToWaitlist(student);
            System.out.println("Course is full. Student added to waitlist.");
            return false;
        }
    }

    // Admin can drop a student from a course
    public boolean dropStudentFromCourse(Student student, Course course) {
        boolean removed = course.removeStudent(student);
        if (removed) {
            student.dropCourse(course);
            return true;
        }
        return false;
    }

    // Admin can view a student's courses
    public void viewStudentCourses(Student student) {
        System.out.println("Courses for " + student.getName() + ":");
        for (Course c : student.getCourseList()) {
            System.out.println("- " + c.getTitle());
        }
    }

    // Admin can apply a hold to a student
    public void addHoldToStudent(Student student, Hold hold) {
        student.setHold(hold);
        System.out.println("Hold added to student: " + student.getName());
    }

    // Admin can clear all holds
    public void clearHolds(Student student) {
        student.getHold().clear();
        System.out.println("All holds cleared for student: " + student.getName());
    }

    // Admin can apply payment to student balance
    public void applyPaymentToStudent(Student student, double amount) {
        student.applyPayment(amount);  // update student balance
        Payment payment = new Payment(amount, student.getName());  // create payment object
        ReportLogger.logSystemEvent(payment.toString());  // log to report.txt
        ReportLogger.logAdminEvent(getName(), "Applied payment of $" + amount + " to " + student.getName());  // log to admin report
    }
}