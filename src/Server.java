import java.io.*;
import java.net.*;

class Server {
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
	                    case LOGIN:
	                        System.out.println("Received login request.");
	                        loggedIn = true;
	                        Message loginResponse = new Message(Type.LOGIN, Status.SUCCESS, "Welcome!");
	                        out.writeObject(loginResponse);
	                        break;
	
	                    case Type.TEXT:
	                        if (loggedIn) {
	                            System.out.println("Received text: " + message.getText());
	                            String upper = message.getText().toUpperCase();
	                            Message reply = new Message(Type.TEXT, Status.SUCCESS, upper);
	                            out.writeObject(reply);
	                        }
	                        break;
	
	                    case Type.LOGOUT:
	                        if (loggedIn) {
	                            System.out.println("Client logged out.");
	                            Message logoutMsg = new Message(Type.LOGOUT, Status.SUCCESS, "Goodbye!");
	                            out.writeObject(logoutMsg);
	                            clientSocket.close();
	                            return;
	                        }
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
	}
}
