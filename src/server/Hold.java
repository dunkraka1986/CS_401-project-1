package server;

import java.io.Serializable;

public class Hold implements Serializable {

    private String holdID;
    private String reason;
    private String status;

    public Hold(String holdID, String reason) {
        this.holdID = holdID;
        this.reason = reason;
        this.status = "ACTIVE";
    }

    // Constructor for JUnit Testing
    public Hold(String holdID, String reason, String status) {
        this.holdID = holdID;
        this.reason = reason;
        this.status = status;
    }

    public String getHoldID() {
        return holdID;
    }

    public String getReason() {
        return reason;
    }

    public String getStatus() {
        return status;
    }

    // Activate hold
    public void placeHold() {
        status = "ACTIVE";
    }

    // Clear hold
    public void clearHold() {
        status = "CLEARED";
    }

    public String toString() {
        return "HoldID: " + holdID + ", Reason: " + reason + ", Status: " + status;
    }
}
