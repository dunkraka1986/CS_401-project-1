package Client;

import java.io.*;
import java.net.*;
import java.util.*;
import common.Message;
import common.Type;
import common.Status;

class Client {
	
	public static void main(String[] args)
	{
		try (Socket socket = new Socket("localhost", 1234)) {
			
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
				
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			ObjectInputStream in = new ObjectInputStream(inputStream);

			Scanner scanner = new Scanner(System.in);

			Message loginMsg = new Message(Type.LOGIN, Status.NULL, "Logging in...");
            out.writeObject(loginMsg);
            Message response = (Message) in.readObject();
            System.out.println("Server: " + response.getText());
            
            while (true) {
                System.out.print("Enter message (or 'logout' to exit): ");
                System.out.println("Enter name: ");
                String input = scanner.nextLine();
                System.out.println("Enter password: ");
                String password = scanner.nextLine();

                if (input.equalsIgnoreCase("logout")) {
                    Message logoutMsg = new Message(Type.LOGOUT, Status.NULL, "");
                    out.writeObject(logoutMsg);
                    Message logoutResponse = (Message) in.readObject();
                    System.out.println("Server: " + logoutResponse.getText());
                    break;
                }

                String student = input + "," + password;
                Message textMsg = new Message(Type.REGISTER, Status.NULL, student);
                out.writeObject(textMsg);
            }

			
			scanner.close();
		}
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}