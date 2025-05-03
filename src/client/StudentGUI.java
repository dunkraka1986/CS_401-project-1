package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

import common.Message;
import common.Type;
import common.Status;

public class StudentGUI {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Scanner scanner;
    
    public StudentGUI(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.scanner = new Scanner(System.in);
    }
    
    public void processCommands() throws IOException {
    	String name = JOptionPane.showInputDialog("Enter Name");
    	String password = JOptionPane.showInputDialog("Enter Password");
    	String student = name + "," + password;
    	Message textMsg = new Message(Type.REGISTER, Status.NULL, student);
    	out.writeObject(textMsg);
    }
}