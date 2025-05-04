
package server;

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

				System.out.println("New client connected "
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
	                
	                    case CONNECT:
	                    	System.out.println("Received connection request.");
	                        loggedIn = true;
	                        Message loginResponse = new Message(Type.LOGIN, Status.SUCCESS, "Welcome!");
	                        out.writeObject(loginResponse);
	                        break;
	
	                    case LOGIN:
	                    	handleLogin(message);
	                        break;
	
	                    case LOGOUT:
	                    	
	                    	System.out.println("Client logged out.");
                            clientSocket.close();
	                        return;
	
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
		
		public void handleLogin(Message message) throws IOException {
			
			System.out.println("Received login request.");
			
			String info = message.getText();
			
			String[] parts = info.split(",");
			String name = parts[0].trim();
			String password = parts[1].trim();
			
			String folder = "data/";
			File file = new File(folder + name);
			
			if(!file.exists()) {
				System.out.println("Login Failed: User not found");
				Message loginResponse = new Message(Type.LOGIN, Status.FAILED, "Login Failed: User not found");
				out.writeObject(loginResponse);
				return;
			}
			
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
		        String line = reader.readLine();  // read first line: name,password,phone
		        if (line != null) {
		            String[] fileParts = line.split(",");
		            String storedName = fileParts[0].trim();
		            String storedPassword = fileParts[1].trim();
		            // String storedPhone = fileParts[2].trim();  // optional, you can use this later

		            if (password.equals(storedPassword)) {
		                System.out.println("Login successful.");
		                Message loginResponse = new Message(Type.LOGIN, Status.SUCCESS, "Login successful. Welcome, " + storedName + "!");
		                out.writeObject(loginResponse);
		            } else {
		                System.out.println("Login failed: incorrect password.");
		                Message loginResponse = new Message(Type.LOGIN, Status.FAILED, "Login failed: incorrect password.");
		                out.writeObject(loginResponse);
		            }
		        } else {
		            System.out.println("Login failed: empty user file.");
		            Message loginResponse = new Message(Type.LOGIN, Status.FAILED, "Login failed: empty user file.");
		            out.writeObject(loginResponse);
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		        Message loginResponse = new Message(Type.LOGIN, Status.FAILED, "Login failed: server error.");
		        out.writeObject(loginResponse);
		    }
		}
		
		private void handleRegister(Message message) {
			
			System.out.println("Received registration request.");
			
			String info = message.getText();
			
			String[] parts = info.split(",");
			String name = parts[0].trim();
			String password = parts[1].trim();
			String phoneNumber = parts[2].trim();
			
			Student student = new Student(name, password, Long.parseLong(phoneNumber));
			
			uni.addStudent(student);
			
			student.save();
			
		}
	}
}
