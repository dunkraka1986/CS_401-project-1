package jUnitTests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

import server.Payment;

public class PaymentTest {

    @Test
    public void testConstructorAndGetters() {
        Payment payment = new Payment(100.0, "Harry Potter");

        assertEquals(100.0, payment.getAmount(), 0.001, "Amount should match");
        assertEquals("Harry Potter", payment.getStudentName(), "Student name should match");
        assertEquals(LocalDate.now(), payment.getDate(), "Date should be today");
    }

    @Test
    public void testToStringFormat() {
        Payment payment = new Payment(50.0, "Hermione Granger");

        String expectedPrefix = "[" + LocalDate.now() + "] Payment of $50.0 by Hermione Granger";
        assertEquals(expectedPrefix, payment.toString(), "toString format should match expected output");
    }
}
