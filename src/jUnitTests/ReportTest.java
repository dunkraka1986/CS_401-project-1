package jUnitTests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import server.Report;

import java.util.ArrayList;
import java.util.List;

public class ReportTest {

    @Test
    public void testLogAddsEntry() {
        Report report = new Report("AdminA");
        report.log("Added a student");
        report.log("Removed a hold");
        assertDoesNotThrow(() -> report.generate(), "Generate should not throw");
    }

    @Test
    public void testGenerateClearsEntries() {
        Report report = new Report("AdminB");
        report.log("Reset password");
        report.log("Issued refund");

        // Run twice to check if second call still works after clearing
        assertDoesNotThrow(() -> report.generate());
        assertDoesNotThrow(() -> report.generate());
    }
}
