import java.io.*;
import java.net.*;
import java.util.*;

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
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("logout")) {
                    Message logoutMsg = new Message(Type.LOGOUT, Status.NULL, "");
                    out.writeObject(logoutMsg);
                    Message logoutResponse = (Message) in.readObject();
                    System.out.println("Server: " + logoutResponse.getText());
                    break;
                }

                Message textMsg = new Message(Type.TEXT, Status.NULL, input);
                out.writeObject(textMsg);
                Message serverResponse = (Message) in.readObject();
                System.out.println("Server response: " + serverResponse.getText());
            }

			
			scanner.close();
		}
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
