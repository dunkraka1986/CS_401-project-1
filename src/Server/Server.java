package Server;

import java.io.*;
import java.net.*;
import common.Message;
import common.Type;
import common.Status;

class Server {
	
	public static University uni = new University("Hogwarts");
	
	public static void main(String[] args) 
	{
		ServerSocket server = null;

		try {

			server = new ServerSocket(1234);
			server.setReuseAddress(true);

			while (true) {

				Socket client = server.accept();

				System.out.println("New client connected"
								+ client.getInetAddress()
										.getHostAddress());

				ClientHandler clientSock
					= new ClientHandler(client);

				new Thread(clientSock).start();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (server != null) {
				try {
					server.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;
	    private ObjectInputStream in;
	    private ObjectOutputStream out;
	    private boolean loggedIn = false;

		public ClientHandler(Socket socket)
		{
			this.clientSocket = socket;
		}

		public void run()
		{
			out = null;
			in = null;
			try {
				
				InputStream inputStream = clientSocket.getInputStream();
				OutputStream outputStream = clientSocket.getOutputStream();
					
				out = new ObjectOutputStream(outputStream);
				in = new ObjectInputStream(inputStream);

				while (true) {
					Message message = (Message) in.readObject();
					
	                switch (message.getType()) {
	                
	                	case REGISTER:
	                		handleRegister(message);
	                		break;
	                
	                    case LOGIN:
	                    	System.out.println("Received login request.");
	                        loggedIn = true;
	                        Message loginResponse = new Message(Type.LOGIN, Status.SUCCESS, "Welcome!");
	                        out.writeObject(loginResponse);
	                        break;
	
	                    case Type.TEXT:
	                        break;
	
	                    case Type.LOGOUT:
	                        break;
	
	                    default:
	                        System.out.println("Unknown message type received.");
	                        break;
	                }
				}
			}
			catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			finally {
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
						clientSocket.close();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void handleRegister(Message message) {
			
			System.out.println("Received registration request.");
			
			String info = message.getText();
			
			String[] parts = info.split(",");
			String name = parts[0].trim();
			String password = parts[1].trim();
			
			Student student = new Student(name, password);
			
			uni.addStudent(student);
			
			student.save();
			
		}
		
	}
}
