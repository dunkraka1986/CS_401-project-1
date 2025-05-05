package jUnitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import server.Student;
import server.Course;
import server.Hold;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    private Student student;

    @BeforeEach
    public void setup() {
        student = new Student("Harry Potter", "magic123", 1234567890L);
    }

    @Test
    public void testSetAndGetBalance() {
        student.setBalance(100.0);
        assertEquals(100.0, student.getBalance());
    }

    @Test
    public void testApplyPayment() {
        student.setBalance(150.0);
        student.applyPayment(50.0);
        assertEquals(100.0, student.getBalance());
    }

    @Test
    public void testSetAndCheckHold() {
        assertFalse(student.hasHold());

        Hold hold = new Hold("H001", "Library fine");
        student.setHold(hold);
        assertTrue(student.hasHold());

        ArrayList<Hold> holds = student.getHold();
        assertEquals(1, holds.size());
        assertEquals("H001", holds.get(0).getHoldID());
    }

    @Test
    public void testAddAndDropCourse() {
        Course course = new Course("Charms", "Magic spells", "Flitwick", 10, 3);
        student.addCourse(course);

        // Course should not be added if student has no unit space
        assertTrue(student.getCourseList().contains(course));

        student.dropCourse(course);
        assertFalse(student.getCourseList().contains(course));
    }

    @Test
    public void testCanEnrollWithinUnitCap() {
        Course course1 = new Course("Potions", "Brewing basics", "Snape", 10, 5);
        Course course2 = new Course("Herbology", "Plants and fungi", "Sprout", 10, 6);

        student.addCourse(course1);
        student.addCourse(course2);

        assertTrue(student.canEnroll(6));
        assertFalse(student.canEnroll(8));
    }

    @Test
    public void testToStringFormat() {
        String expected = "Harry Potter,magic123,1234567890";
        assertEquals(expected, student.toString());
    }
}
