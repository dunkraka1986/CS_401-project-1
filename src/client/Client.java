package client;

import java.io.*;
import java.net.*;
import common.Message;
import common.Type;
import common.Status;

class Client {
	public static void main(String[] args) {
	    Socket socket = null;
	    ObjectOutputStream out = null;
	    ObjectInputStream in = null;

	    try {
	        socket = new Socket("localhost", 1234);
	        out = new ObjectOutputStream(socket.getOutputStream());
	        in = new ObjectInputStream(socket.getInputStream());

	        Message loginMsg = new Message(Type.CONNECT, Status.NULL, "Logging in...");
	        out.writeObject(loginMsg);
	        Message response = (Message) in.readObject();
	        System.out.println("Server: " + response.getText());

	        StudentGUI student = new StudentGUI(socket, in, out);
	        student.processCommands();

	    } catch (IOException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	}
}