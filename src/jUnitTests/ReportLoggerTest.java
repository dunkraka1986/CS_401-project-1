package jUnitTests;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;
import server.ReportLogger;

public class ReportLoggerTest {

    private static final String FILE_PATH = "report.txt";

    @BeforeEach
    public void clearFile() throws IOException {
        Files.write(Paths.get(FILE_PATH), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Test
    public void testLogSystemEvent() throws IOException {
        ReportLogger.logSystemEvent("System started");

        String content = Files.readString(Paths.get(FILE_PATH));
        assertTrue(content.contains("System: System started"), "Should contain system log entry");
    }

    @Test
    public void testLogAdminEvent() throws IOException {
        ReportLogger.logAdminEvent("Admin1", "Added course");

        String content = Files.readString(Paths.get(FILE_PATH));
        assertTrue(content.contains("Admin Admin1: Added course"), "Should contain admin log entry");
    }
}
