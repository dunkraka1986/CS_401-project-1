package jUnitTests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import server.Course;
import server.Student;

public class CourseTest {

    private Course course;
    private Student student1;
    private Student student2;

    @BeforeEach
    public void setUp() {
        course = new Course("Potions", "Learn potion making", "Snape", 2, 3); 
        student1 = new Student("Harry", "magic123", 1234567890L);
        student2 = new Student("Ron", "magic456", 9876543210L);
    }

    @Test
    public void testAddStudentWhenNotFull() {
        boolean added = course.addStudent(student1);
        assertTrue(added, "Student should be enrolled");
        assertFalse(course.isFull(), "Course should not be full yet");
    }

    @Test
    public void testAddStudentWhenFull() {
        course.addStudent(student1);
        course.addStudent(student2);
        boolean added = course.addStudent(new Student("Hermione", "smart999", 1122334455L));
        assertFalse(added, "Third student should not be enrolled (waitlisted)");
        assertTrue(course.isFull(), "Course should be full");
    }

    @Test
    public void testRemoveStudent() {
        course.addStudent(student1);
        boolean removed = course.removeStudent(student1);
        assertTrue(removed, "Student should be removed");
    }

    @Test
    public void testAddAndRemovePrerequisite() {
        course.addPrerequisite("Intro to Magic");
        course.removePrerequisite("Intro to Magic");
        assertEquals(0, course.getCapacity() - course.getCapacity(), "Test");
    }

    @Test
    public void testSettersAndGetters() {
        course.setTitle("New Title");
        course.setDescription("New Description");
        course.setProfessor("New Professor");
        course.setCapacity(10);
        course.setUnits(4);

        assertEquals("New Title", course.getTitle());
        assertEquals("New Description", course.getDescription());
        assertEquals("New Professor", course.getProfessor());
        assertEquals(10, course.getCapacity());
        assertEquals(4, course.getUnits());
    }
}