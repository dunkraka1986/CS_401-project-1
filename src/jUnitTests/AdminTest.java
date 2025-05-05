package jUnitTests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import server.Admin;
import server.Course;
import server.Student;
import server.Hold;

public class AdminTest {

    private Admin admin;
    private Student student;
    private Course course;

    @BeforeEach
    public void setUp() {
        admin = new Admin("TestAdmin", "adminpass");
        student = new Student("TestStudent", "studentpass", 5551234567L);
        course = new Course("Potions", "Basic potion-making", "Snape", 30, 3);
    }

    @Test
    public void testAddHoldToStudent() {
        Hold hold = new Hold("H001", "Test hold");
        admin.addHoldToStudent(student, hold);
        assertTrue(student.getHold().contains(hold), "Hold should be added to student");
    }

    @Test
    public void testViewStudentCourses() {
        student.getCourseList().add(course);
        admin.viewStudentCourses(student);
        assertTrue(true);
    }

    @Test
    public void testDropStudentFromCourse() {
        admin.enrollStudentInCourse(student, course);

        boolean result = admin.dropStudentFromCourse(student, course);

        assertTrue(result, "Student should be dropped from course");
        assertFalse(student.getCourseList().contains(course), "Course should be removed from student's list");
    }

    @Test
    public void testApplyPayment() {
        student.setBalance(100.0);

        admin.applyPaymentToStudent(student, 50.0);

        assertEquals(50.0, student.getBalance(), 0.001, "Balance should be reduced by payment amount");
    }

    @Test
    public void testClearHolds() {
        Hold hold1 = new Hold("H001", "Missing Documents", "Active");
        Hold hold2 = new Hold("H002", "Fee Due", "Active");

        admin.addHoldToStudent(student, hold1);
        admin.addHoldToStudent(student, hold2);

        admin.clearHolds(student);

        assertTrue(student.getHold().isEmpty(), "All holds should be cleared");
    }
}