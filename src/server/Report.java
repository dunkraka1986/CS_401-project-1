package server;

import java.util.ArrayList;
import java.util.List;

public class Report {
    private String adminName;
    private List<String> entries;

    public Report(String adminName) {
        this.adminName = adminName;
        this.entries = new ArrayList<>();
    }

    // Log admin action
    public void log(String action) {
        String entry = adminName + ": " + action;
        entries.add(entry);
        ReportLogger.logAdminEvent(adminName, action);
    }

    // Write all entries
    public void generate() {
        for (String entry : entries) {
            ReportLogger.logAdminEvent(adminName, entry);
        }
        entries.clear();
    }
}
