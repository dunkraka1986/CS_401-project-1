package server;

import java.io.Serializable;
import java.time.LocalDate;

public class Payment implements Serializable {
    
    private double amount;          
    private LocalDate date;         
    private String studentName;    

    public Payment(double amount, String studentName) {
        this.amount = amount;
        this.date = LocalDate.now(); 
        this.studentName = studentName;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStudentName() {
        return studentName;
    }

    public String toString() {
        return "[" + date + "] Payment of $" + amount + " by " + studentName;
    }
}