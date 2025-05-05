package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import common.Message;
import common.Type;
import common.UserType;
import common.Status;

class Server {
	
	public static University uni = new University("Hogwarts");
	
	public static void main(String[] args) 
	{
		initializeCourses();
		
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

		/**
		 *
		 */
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
	                        
	                    case CREATE_COURSE:
	                    	createCourse(message);
	                    	break;
	                    	
	                    case REMOVE_COURSE:
	                    	removeCourse(message);
	                    	break;
	                    	
	                    case REMOVE_STUDENT:
	                    	removeStudent(message);
	                    	break;
	                    
	                    case ENROLL_COURSE:
	                    	enrollStudent(message);
	                    	break;
	                    
	                    case ADD_HOLD:
	                    	addHold(message);
	                    	break;
	                    case REMOVE_HOLD:
	                    	removeHold(message);
	                    	break;
	                    case VIEW_STUDENTS:
	                    	viewStudents(message);
	                    	break;
	                    case REPORT:
	                    	report(message);
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
		
		public void handleLogin(Message message) throws IOException {
			
			System.out.println("Received login request.");
			
			String info = message.getText();
			
			String[] parts = info.split(",");
			String name = parts[0].trim();
			String password = parts[1].trim();
			if(name.equalsIgnoreCase("Albus Dumbledore")) {
				Message loginResponse = new Message(Type.LOGIN, Status.SUCCESS, UserType.ADMIN, "Login successful. Welcome Back Dumbledore");
				out.writeObject(loginResponse);
				return;
			}
			
			String folder = "data/";
			File file = new File(folder + name);
			
			if(name.equalsIgnoreCase("Albus Dumbledore")) {
				System.out.println("BRUHHH");
				Message loginResponse = new Message(Type.LOGIN, Status.SUCCESS, UserType.ADMIN, "Login successful. Welcome Back Fool");
				out.writeObject(loginResponse);
				return;
			}
			
<<<<<<< HEAD
=======
			if(!file.exists()) {
				System.out.println("Login Failed: User not found");
				Message loginResponse = new Message(Type.LOGIN, Status.FAILED, "Login Failed: User not found");
				out.writeObject(loginResponse);
				return;
			}
			
>>>>>>> branch 'main' of https://github.com/cramos2003/CS_401-project-1.git
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
		    
		    String folder = "data/";
		    File file = new File(folder + name);
		    
		    if (file.exists()) {
		        System.out.println("Registration failed: Student already exists.");
		        try {
		            Message response = new Message(Type.REGISTER, Status.FAILED, "Registration failed: Student already exists.");
		            out.writeObject(response);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        return;
		    }
		    
		    Student student = new Student(name, password, Long.parseLong(phoneNumber));
		    uni.addStudent(student);
		    student.save();
		    
		    System.out.println("Student registered successfully: " + name);
		    try {
		        Message response = new Message(Type.REGISTER, Status.SUCCESS, "Registration successful! Welcome, " + name + "!");
		        out.writeObject(response);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    
		    ReportLogger.logSystemEvent("Registered new student: " + name);
		}
		
		private void createCourse(Message message) {
			String[] parsedMessage = message.getText().split(",");
			int capacity = Integer.parseInt(parsedMessage[2]);
			int units = Integer.parseInt(parsedMessage[3]);
			Course course = new Course(parsedMessage[0], "",
									parsedMessage[1], capacity,
									units);
			uni.addCourse(course);
			// send successfully added message
			message = new Message(Type.CREATE_COURSE, Status.SUCCESS, "Course Added Successfully");
			try {
				out.writeObject(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private void removeCourse(Message message) {
			String[] messageText = message.getText().split(",");
			List<Course> courses = uni.getCorses();
			Status removed = Status.NULL;
			String removalMessage = "Course couldn't be removed or doesn\'t exist";
			for (Course c : courses) {
				if (messageText[0].equalsIgnoreCase(c.getTitle())) {
					courses.remove(c);
					removed = Status.SUCCESS;
					removalMessage = "Removed Successfully";
					break;
				}
			}
			
			message = new Message(Type.REMOVE_COURSE, removed, removalMessage);
			try {
				out.writeObject(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private void removeStudent(Message message) {
			String[] messageText = message.getText().split(",");
			String student = messageText[0];
			String courseTitle = messageText[1];
			List<Course> courseList = uni.getCorses();
			for (Course c : courseList) {
				if (c.getTitle().equalsIgnoreCase(courseTitle)) {
					ArrayList<Student> students = c.getEnrolledStudents();
					for (Student s : students) {
						if (s.getName().equalsIgnoreCase(student)) {
							s.dropCourse(c);
							message = new Message(Type.REMOVE_STUDENT, Status.SUCCESS, "Student removed successfully");
							try {
								out.writeObject(message);
							} catch (IOException e) {
								e.printStackTrace();
							}
							return;
						}
						message = new Message(Type.REMOVE_STUDENT, Status.FAILED, "Student doesn\'t exist or error removing student");
						try {
							out.writeObject(message);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		private void enrollStudent(Message message) {
			String[] messageText = message.getText().split(",");
			String studentName = messageText[0];
			String courseName = messageText[1];
			
			List<Course> courses = uni.getCorses();
			List<Student> students = uni.getStudents();
			Student student = null;
			Course course = null;
			Status status = Status.NULL;
			for (Course c : courses) {
				if (c.getTitle().equalsIgnoreCase(courseName)) {
					course = c;
					break;
				}
			}
			for (Student s : students) {
				if (s.getName().equalsIgnoreCase(studentName)) {
					student = s;
				}
			}
			if (student != null && course != null) {
				// send success message
				message = new Message(Type.ENROLL_COURSE, Status.SUCCESS, "Enrolled successfully");
			} else {
				// send failed message
				message = new Message(Type.ENROLL_COURSE, Status.FAILED, "Enrollment failed");
			}
			try {
				out.writeObject(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private void addHold(Message message) {
			String[] messageText = message.getText().split(",");
			String studentName = messageText[0];
			ArrayList<String> holds = message.getList();
			List<Student> students = uni.getStudents();
			for (Student s : students) {
				if (s.getName().equalsIgnoreCase(studentName)) {
					for (String h : holds) {
						Hold hold = new Hold(h, h);
						hold.placeHold();
					}
					message = new Message(Type.ADD_HOLD, Status.SUCCESS, "Hold placed");
					try {
						out.writeObject(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
			}
			message = new Message(Type.ADD_HOLD, Status.FAILED, "Failed to place hold");
			try {
				out.writeObject(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/*
		private void removeHold(Message message) {
			
		}*/
	}
	
	public static void initializeCourses() {
	    ArrayList<Course> courses = new ArrayList<>();

	    courses.add(new Course("Defense Against the Dark Arts", 
	        "Learn to defend against dark creatures, curses, and hexes.", 
	        "Professor Lupin", 30, 3));

	    courses.add(new Course("Potions", 
	        "Master the art of potion-making and brewing magical mixtures.", 
	        "Professor Snape", 25, 4));

	    courses.add(new Course("Herbology", 
	        "Study magical plants and their uses.", 
	        "Professor Sprout", 20, 3));

	    courses.add(new Course("Transfiguration", 
	        "Transform objects and creatures into different forms.", 
	        "Professor McGonagall", 25, 4));

	    courses.add(new Course("Charms", 
	        "Learn charms for levitation, unlocking, and more.", 
	        "Professor Flitwick", 30, 3));

	    courses.add(new Course("Astronomy", 
	        "Explore stars, planets, and magical constellations.", 
	        "Professor Sinistra", 20, 2));

	    courses.add(new Course("Care of Magical Creatures", 
	        "Handle and care for magical creatures safely.", 
	        "Professor Hagrid", 15, 2));

	    courses.add(new Course("Divination", 
	        "Study methods of prophecy and prediction.", 
	        "Professor Trelawney", 15, 2));

	    courses.add(new Course("History of Magic", 
	        "Learn the history of the wizarding world.", 
	        "Professor Binns", 40, 3));

	    courses.add(new Course("Arithmancy", 
	        "Discover the magical properties of numbers.", 
	        "Professor Vector", 15, 3));
	    
	    courses.add(new Course("Apparition", 
	    	    "Learn the magical art of teleporting from one place to another.", 
	    	    "Professor Wilkie Twycross", 10, 2));

    	courses.add(new Course("Advanced Potions", 
    	    "A deeper dive into complex and dangerous potion-making.", 
    	    "Professor Slughorn", 20, 4));

    	courses.add(new Course("Muggle Studies", 
    	    "Understand the non-magical world and its customs.", 
    	    "Professor Burbage", 30, 2));

    	courses.add(new Course("Flying Lessons", 
    	    "Learn the basics of broomstick flying and aerial maneuvers.", 
    	    "Madam Hooch", 15, 1));

    	courses.add(new Course("Ancient Runes", 
    	    "Study the magical symbols and writings of ancient civilizations.", 
    	    "Professor Babbling", 15, 3));

    	courses.add(new Course("Magical Theory", 
    	    "Explore the fundamental principles behind magic.", 
    	    "Professor Marchbanks", 25, 3));

    	courses.add(new Course("Duelling Club", 
    	    "Practice magical combat and defense techniques.", 
    	    "Professor Lockhart", 20, 2));

    	courses.add(new Course("Occlumency", 
    	    "Learn to shield your mind from external influence.", 
    	    "Professor Snape", 10, 2));

    	courses.add(new Course("Legilimency", 
    	    "Master the magical ability to penetrate minds and memories.", 
    	    "Professor Dumbledore", 10, 2));

    	courses.add(new Course("Alchemy", 
    	    "Study the mystical science of transmutation and eternal life.", 
    	    "Professor Alkimia", 10, 4));

    	courses.add(new Course("Magizoology", 
    	    "Explore magical creatures beyond Care of Magical Creatures.", 
    	    "Professor Scamander", 15, 3));

    	courses.add(new Course("Enchantments", 
    	    "Learn to imbue objects with magical properties.", 
    	    "Professor Flitwick", 20, 3));

	    uni.setCourses(courses);
	}


}