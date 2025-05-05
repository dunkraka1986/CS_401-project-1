package jUnitTests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import server.Hold;

public class HoldTest {

    @Test
    public void testConstructorAndGetters() {
        Hold hold = new Hold("H001", "Missing Documents");
        assertEquals("H001", hold.getHoldID(), "HoldID should match");
        assertEquals("Missing Documents", hold.getReason(), "Reason should match");
        assertEquals("ACTIVE", hold.getStatus(), "Default status should be ACTIVE");
    }

    @Test
    public void testCustomConstructor() {
        Hold hold = new Hold("H002", "Fee Due", "CLEARED");
        assertEquals("CLEARED", hold.getStatus(), "Custom status should be used");
    }

    @Test
    public void testPlaceHold() {
        Hold hold = new Hold("H003", "Late Payment", "CLEARED");
        hold.placeHold();
        assertEquals("ACTIVE", hold.getStatus(), "Hold should be activated");
    }

    @Test
    public void testClearHold() {
        Hold hold = new Hold("H004", "Unpaid Tuition");
        hold.clearHold();
        assertEquals("CLEARED", hold.getStatus(), "Hold should be cleared");
    }

    @Test
    public void testToString() {
        Hold hold = new Hold("H005", "Test Reason");
        String expected = "HoldID: H005, Reason: Test Reason, Status: ACTIVE";
        assertEquals(expected, hold.toString(), "Should match expected");
    }
}
