package server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class ReportLogger {

    // Logs system event
    public static void logSystemEvent(String message) {
        String timestamp = LocalDateTime.now().toString();
        String entry = "[" + timestamp + "] System: " + message;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("report.txt", true))) {
            writer.write(entry);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Logs admin event
    public static void logAdminEvent(String adminName, String message) {
        String timestamp = LocalDateTime.now().toString();
        String entry = "[" + timestamp + "] Admin " + adminName + ": " + message;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("report.txt", true))) {
            writer.write(entry);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
