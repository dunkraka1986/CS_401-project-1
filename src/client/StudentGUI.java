package client;

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.Scanner;

import common.Message;
import common.Type;
import common.UserType;
import common.Status;

public class StudentGUI {
	
	private ArrayList<String> enrolledCourseTitles = new ArrayList<>();
	
	private JPanel pagesPanel;
	
	private static String student;
	
	private static JFrame mainFrame;
	private static JPanel buttonPannel;
	private static JPanel schedule;
	private static JPanel courseCatalog;
	
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
    	
    	mainFrame = new JFrame("College Enrollment System");
    	mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    	mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    	mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
    	    @Override
    	    public void windowClosing(java.awt.event.WindowEvent e) {
    	        int result = JOptionPane.showConfirmDialog(
    	            mainFrame,
    	            "Are you sure you want to exit?",
    	            "Exit Confirmation",
    	            JOptionPane.YES_NO_OPTION
    	        );

    	        if (result == JOptionPane.YES_OPTION) {
    	            try {
    	                out.writeObject(new Message(Type.LOGOUT, Status.NULL, ""));
    	                out.close();
    	                in.close();
    	                socket.close();
    	            } catch (IOException ex) {
    	                ex.printStackTrace();
    	            }

    	            System.exit(0);
    	        }
    	    }
    	});
    	mainFrame.setLocationRelativeTo(null);
    	
    	CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        JPanel entryPanel = createEntryPanel(cardLayout, cardPanel);
        JPanel loginPanel = createLoginPanel(cardLayout, cardPanel);
        JPanel registerPanel = createRegisterPanel(cardLayout, cardPanel);
        JPanel studentAppPanel = createStudentAppPanel(cardLayout, cardPanel);
        JPanel adminAppPanel = CreateAdminAppPanel(cardLayout, cardPanel);
        
        cardPanel.add(entryPanel, "ENTRY");
        cardPanel.add(loginPanel, "LOGIN");
        cardPanel.add(registerPanel, "REGISTER");
        cardPanel.add(studentAppPanel, "STUDENTAPP");
        cardPanel.add(adminAppPanel, "ADMINAPP");
        
        mainFrame.add(cardPanel);
        mainFrame.setVisible(true);
    }

	private JPanel createEntryPanel(CardLayout cardLayout, JPanel cardPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        JButton loginButton = new JButton("Login");
        ImageIcon loginIcon = new ImageIcon("Images/login.jpg");
        Image img = loginIcon.getImage();
        Image resizedImg = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        loginIcon = new ImageIcon(resizedImg);
        loginButton.setIcon(loginIcon);
        loginButton.setHorizontalTextPosition(SwingConstants.CENTER);
        loginButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        
        loginButton.addActionListener(e -> cardLayout.show(cardPanel, "LOGIN"));

        JButton registerButton = new JButton("Register");
        ImageIcon registerIcon = new ImageIcon("Images/register.jpg");
        Image img1 = registerIcon.getImage();
        Image resizedImg1 = img1.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        registerIcon = new ImageIcon(resizedImg1);
        registerButton.setIcon(registerIcon);
        registerButton.setHorizontalTextPosition(SwingConstants.CENTER);
        registerButton.setVerticalTextPosition(SwingConstants.BOTTOM);

        registerButton.addActionListener(e -> cardLayout.show(cardPanel, "REGISTER"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 20, 0, 20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(loginButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(registerButton, gbc);

        return panel;
    }
	
    private JPanel createLoginPanel(CardLayout cardLayout, JPanel cardPanel) {
    	
    	JPanel panel = new JPanel(new BorderLayout(10,10));
	    
	    ImageIcon registerIcon = new ImageIcon("Images/login.jpg");
        Image img1 = registerIcon.getImage();
        Image resizedImg1 = img1.getScaledInstance(500, 700, Image.SCALE_SMOOTH);
        registerIcon = new ImageIcon(resizedImg1);
        
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(registerIcon);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 300, 10, 10));
        
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new GridLayout(6,1,1,1));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(300, 10, 300, 700));
        
        JPanel userNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel userNameLabel = new JLabel("First and Last Name:");
        JTextField userNameField = new JTextField(15);
        userNamePanel.add(userNameLabel);
        userNamePanel.add(userNameField);
        
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        
        JPanel buttons = new JPanel();

	    JButton submitButton = new JButton("Submit");
	    submitButton.setSize(50, 25);
	    
	    JButton returnButton = new JButton("Return");
	    returnButton.setSize(50, 25);
	    
	    returnButton.addActionListener(e -> {
	    	cardLayout.show(cardPanel, "ENTRY");
	    });
	    
	    buttons.add(submitButton);
	    buttons.add(returnButton);
	    
	    submitButton.addActionListener(e -> {
	        String userName = userNameField.getText().trim();
	        String password = new String(passwordField.getPassword());
	        UserType userType = UserType.STUDENT;
	        if (userName.isEmpty() || password.isEmpty()) {
	            JOptionPane.showMessageDialog(panel,
	                    "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        String student = userName + "," + password;
	        
	        Message textMsg = new Message(Type.LOGIN, Status.NULL, userType, student);
	        try {
				out.writeObject(textMsg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	        
	        Message loginResponse = null;
			try {
				loginResponse = (Message) in.readObject();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
	        
	        switch (loginResponse.getStatus()) {
	        
	        	case SUCCESS:
	        		StudentGUI.student = userName;
	        		if (loginResponse.getList() != null) {
	        		    enrolledCourseTitles = new ArrayList<>(loginResponse.getList());
	        		}
	        		JOptionPane.showMessageDialog(panel,"Welcome Back " + userName, "Hogwarts", JOptionPane.INFORMATION_MESSAGE);
	        		// displays based on student or admin for cardPanel
	        		if (loginResponse.getUserType() == UserType.ADMIN) {
	        			cardLayout.show(cardPanel, "ADMINAPP");
	        			break;
	        		}
	        		cardLayout.show(cardPanel, "STUDENTAPP");
	        		
	        		SwingUtilities.invokeLater(() -> {
	        		    JScrollPane updatedHomePage = createHomePagePanel();
	        		    pagesPanel.add(updatedHomePage, "HOME");
	        		});

	        		break;
	        		
	        	case FAILED:
	        		String message = loginResponse.getText();
	        		JOptionPane.showMessageDialog(panel, message, "Hogwarts", JOptionPane.ERROR_MESSAGE);
	        		break;
	        		
	        	default:
                    System.out.println("Unknown message type received.");
                    break;
	        	
	        }

	    });
	    
	    detailPanel.add(userNamePanel);
	    detailPanel.add(passwordPanel);
	    detailPanel.add(buttons);
	    
	    panel.add(detailPanel, BorderLayout.EAST);
        panel.add(imageLabel, BorderLayout.WEST);
    	
		return panel;
	}
    
    private JPanel createRegisterPanel(CardLayout cardLayout, JPanel cardPanel) {
    	
	    JPanel panel = new JPanel(new BorderLayout(10,10));
	    
	    ImageIcon registerIcon = new ImageIcon("Images/register.jpg");
        Image img1 = registerIcon.getImage();
        Image resizedImg1 = img1.getScaledInstance(500, 700, Image.SCALE_SMOOTH);
        registerIcon = new ImageIcon(resizedImg1);
        
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(registerIcon);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 300, 10, 10));
        
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new GridLayout(6,1,1,1));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(300, 10, 300, 700));
        
        JPanel firstNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField(15);
        firstNamePanel.add(firstNameLabel);
        firstNamePanel.add(firstNameField);
        
        JPanel lastNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField(15);
        lastNamePanel.add(lastNameLabel);
        lastNamePanel.add(lastNameField);
        
        JPanel numberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel numberLabel = new JLabel("Phone Number:");
        JTextField numberField = new JTextField(15);
        numberPanel.add(numberLabel);
        numberPanel.add(numberField);
        
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel passwordLabel = new JLabel("Passowrd:");
        JPasswordField passwordField = new JPasswordField(15);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        
        JPanel confirmPasswordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPasswordField = new JPasswordField(15);
        confirmPasswordPanel.add(confirmPasswordLabel);
        confirmPasswordPanel.add(confirmPasswordField);
        
        JPanel buttons = new JPanel();

	    JButton submitButton = new JButton("Submit");
	    submitButton.setSize(50, 25);
	    
	    JButton returnButton = new JButton("Return");
	    returnButton.setSize(50, 25);
	    
	    returnButton.addActionListener(e -> {
	    	cardLayout.show(cardPanel, "ENTRY");
	    });
	    
	    buttons.add(submitButton);
	    buttons.add(returnButton);
	    		

	    submitButton.addActionListener(e -> {
	        String firstName = firstNameField.getText().trim();
	        String lastName = lastNameField.getText().trim();
	        String phone = numberField.getText().trim();
	        String password = new String(passwordField.getPassword());
	        String confirmPassword = new String(confirmPasswordField.getPassword());

	        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
	            JOptionPane.showMessageDialog(panel,
	                    "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        if (!password.equals(confirmPassword)) {
	            JOptionPane.showMessageDialog(panel,
	                    "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        String student = firstName + " " + lastName + "," + password + "," + phone;
	        
	        Message textMsg = new Message(Type.REGISTER, Status.NULL, student);
	        try {
				out.writeObject(textMsg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	        
	        Message registerResponse = null;
			try {
				registerResponse = (Message) in.readObject();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
	        
	        switch (registerResponse.getStatus()) {
	        
	        	case SUCCESS:
	        		StudentGUI.student = firstName  + " " + lastName;
	        		JOptionPane.showMessageDialog(panel,
	    	                "Registration successful!\nWelcome to Hogwarts, " + firstName + " " + lastName,
	    	                "Success", JOptionPane.INFORMATION_MESSAGE);
	    	        
	    	        cardLayout.show(cardPanel, "STUDENTAPP");
	        		break;
	        		
	        	case FAILED:
	        		String message = registerResponse.getText();
	        		JOptionPane.showMessageDialog(panel, message, "Hogwarts", JOptionPane.ERROR_MESSAGE);
	        		break;
	        		
	        	default:
                    System.out.println("Unknown message type received.");
                    break;
	        	
	        }	        
	    });

	    detailPanel.add(firstNamePanel);
	    detailPanel.add(lastNamePanel);
	    detailPanel.add(numberPanel);
	    detailPanel.add(passwordPanel);
	    detailPanel.add(confirmPasswordPanel);
	    detailPanel.add(buttons);
	    
	    panel.add(detailPanel, BorderLayout.EAST);
        panel.add(imageLabel, BorderLayout.WEST);

	    return panel;
    }
    
    private JPanel createStudentAppPanel(CardLayout cardLayout, JPanel cardPanel) {
        JPanel appContainer = new JPanel(new BorderLayout());
        
        JPanel sidePanel = new JPanel(new GridLayout(0, 1, 0, 0));
        
        Font buttonFont = new Font("Arial", Font.PLAIN, 18);
        
        JButton viewCourseButton = new JButton("HOME");
        viewCourseButton.setPreferredSize(new Dimension(500, 40));
        viewCourseButton.setFont(buttonFont);
        sidePanel.add(viewCourseButton);
        
        JButton viewProfileButton = new JButton("PROFILE");
        viewProfileButton.setPreferredSize(new Dimension(500, 40));
        viewProfileButton.setFont(buttonFont);
        sidePanel.add(viewProfileButton);
        
        JButton viewCourseCatalogButton = new JButton("COURSE CATALOG");
        viewCourseCatalogButton.setPreferredSize(new Dimension(500, 40));
        viewCourseCatalogButton.setFont(buttonFont);
        sidePanel.add(viewCourseCatalogButton);
        
        JButton seeHoldsButton = new JButton("HOLDS");
        seeHoldsButton.setPreferredSize(new Dimension(500, 40));
        seeHoldsButton.setFont(buttonFont);
        sidePanel.add(seeHoldsButton);
        
        JButton seeBalanceButton = new JButton("BALANCE");
        seeBalanceButton.setPreferredSize(new Dimension(500, 40));
        seeBalanceButton.setFont(buttonFont);
        sidePanel.add(seeBalanceButton);
        
        JButton logoutButton = new JButton("LOGOUT");
        logoutButton.setPreferredSize(new Dimension(500, 40));
        logoutButton.setFont(buttonFont);
        sidePanel.add(logoutButton);
        
        CardLayout pagesLayout = new CardLayout();
        pagesPanel = new JPanel(pagesLayout);

        JPanel homePage = new JPanel();
        JPanel profilePage = new JPanel();
        JScrollPane courseCatalogPage = createCourseCatalogPagePanel();
        JPanel holdPage = createHoldPagePanel();
        JPanel balancePage = createBalancePagePanel();

        pagesPanel.add(homePage, "HOME");
        pagesPanel.add(profilePage, "PROFILE");
        pagesPanel.add(courseCatalogPage, "COURSE CATALOG");
        pagesPanel.add(holdPage, "HOLD");
        pagesPanel.add(balancePage, "BALANCE");
        
        pagesLayout.show(pagesPanel, "HOME");

        viewCourseButton.addActionListener(e -> pagesLayout.show(pagesPanel, "HOME"));
        viewProfileButton.addActionListener(e -> {
        	SwingUtilities.invokeLater(() -> {
    		    JPanel updatedHomePage = createProfilePagePanel();
    		    pagesPanel.add(updatedHomePage, "PROFILE");
    		});
        	pagesLayout.show(pagesPanel, "PROFILE");
        	});
        viewCourseCatalogButton.addActionListener(e -> pagesLayout.show(pagesPanel, "COURSE CATALOG"));
        seeHoldsButton.addActionListener(e -> pagesLayout.show(pagesPanel, "HOLD"));
        seeBalanceButton.addActionListener(e -> pagesLayout.show(pagesPanel, "BALANCE"));
        logoutButton.addActionListener(e -> {
        	int result = JOptionPane.showConfirmDialog(
    	            mainFrame,
    	            "Are you sure you want to exit?",
    	            "Exit Confirmation",
    	            JOptionPane.YES_NO_OPTION
    	        );

    	        if (result == JOptionPane.YES_OPTION) {
    	            try {
    	                out.writeObject(new Message(Type.LOGOUT, Status.NULL, ""));
    	                out.close();
    	                in.close();
    	                socket.close();
    	            } catch (IOException ex) {
    	                ex.printStackTrace();
    	            }

    	            System.exit(0);
    	        }
        });

        appContainer.add(sidePanel, BorderLayout.WEST);
        appContainer.add(pagesPanel, BorderLayout.CENTER);
        
        return appContainer;
    }
    
    private JPanel createBalancePagePanel() {
    	JPanel panel = new JPanel();
		return panel;
	}

	private JPanel createHoldPagePanel() {
		JPanel panel = new JPanel();
		return panel;
	}

	private JScrollPane createCourseCatalogPagePanel() {
		
		JPanel panel = new JPanel(new GridLayout(0,1,0,10));
		
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		Message textMsg = new Message(Type.GET_CATALOG, Status.NULL, "");
		
		try {
			out.writeObject(textMsg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Message catalogResponse = null;
		try {
			catalogResponse = (Message) in.readObject();
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
		
		ArrayList<String> list = catalogResponse.getList();
		
		for(String courseInfo: list) {
			
			String[] parts = courseInfo.split(",");
			String title = parts[0].trim();
			String description = parts[1].trim();
			String professor = parts[2].trim();
			String capacity = parts[3].trim();
			String units = parts[4].trim();
			String enrolled = parts[5].trim();
			String waitlisted = parts[6].trim();
			
			JButton course = new JButton(title.toUpperCase() + "           Capcity:" + enrolled + "/" + capacity + "            Units: " + units);
			
			course.addActionListener(e -> {
				showCourseDetails(title, description, professor, capacity, units, enrolled, waitlisted);
			});
			
			course.setPreferredSize(new Dimension(900, 70));
			
			panel.add(course);
		}
		
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		return scrollPane;
	}

	private void showCourseDetails(String title, String description, String professor, String capacity, String units, String enrolled, String waitlisted) {
	    JDialog dialog = new JDialog(mainFrame, title, true);
	    dialog.setSize(500, 450);
	    dialog.setLocationRelativeTo(mainFrame);
	    dialog.setLayout(new BorderLayout(10,10));

	    JPanel detailPanel = new JPanel();
	    detailPanel.setLayout(new GridLayout(3,1,5,5));
	    detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 50));

	    JLabel prof = new JLabel(professor.toUpperCase());
	    JLabel desc = new JLabel("DESCRIPTION: " + description);
	    JButton enroll = new JButton("ENROLL");

	    // Check if already enrolled and disable button
	    if (enrolledCourseTitles.contains(title)) {
	        enroll.setEnabled(false);
	    }

	    enroll.addActionListener(e -> {
	        Message enrollRequest = new Message(Type.ENROLL_COURSE_STUDENT, Status.NULL, title);
	        try {
	            out.writeObject(enrollRequest);
	            
	            Message response = null;
	            
	            try {
					response = (Message) in.readObject();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
	            
	            switch(response.getStatus()) {
	            	
	            case FAILED:
	            	String failMessage = response.getText();
	                JOptionPane.showMessageDialog(dialog, 
	                    "Enrollment failed: " + failMessage, 
	                    "Enrollment Error", 
	                    JOptionPane.ERROR_MESSAGE);
	            	break;
	            	
	            case SUCCESS:
	            	
	            	enrolledCourseTitles.add(title);
	                enroll.setEnabled(false);
	                JOptionPane.showMessageDialog(dialog, 
	                    response.getText(), 
	                    "Enrollment Success", 
	                    JOptionPane.INFORMATION_MESSAGE);
	                
	                enrolledCourseTitles.add(title);

		            enroll.setEnabled(false);

		            Timer timer = new Timer(500, event -> dialog.dispose());
		            timer.setRepeats(false);
		            timer.start();
	                
	            	break;
	            
	            }
	            

	        } catch (IOException ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(dialog, "Enrollment failed.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	        
	        SwingUtilities.invokeLater(() -> {
    		    JScrollPane updatedHomePage = createHomePagePanel();
    		    pagesPanel.add(updatedHomePage, "HOME");
    		});
	        
	        SwingUtilities.invokeLater(() -> {
            	
                pagesPanel.remove(pagesPanel.getComponent(2));

                JScrollPane updatedCatalogPage = createCourseCatalogPagePanel();
                pagesPanel.add(updatedCatalogPage, "COURSE CATALOG");

                CardLayout cl = (CardLayout) pagesPanel.getLayout();
                cl.show(pagesPanel, "COURSE CATALOG");
            });
	    });

	    detailPanel.add(prof);
	    detailPanel.add(desc);
	    detailPanel.add(enroll);

	    dialog.add(detailPanel);
	    dialog.setVisible(true);
	}


	private JPanel createProfilePagePanel() {
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
	    Message msg = new Message(Type.PROFILE, Status.NULL, "");
	    
	    try {
			out.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    Message response = null;
	    try {
	        response = (Message) in.readObject();
	    } catch (ClassNotFoundException | IOException e1) {
	        e1.printStackTrace();
	    }

	    if (response == null || response.getStatus() == Status.FAILED) {
	        JLabel errorLabel = new JLabel("Failed to load profile.");
	        errorLabel.setFont(new Font("Arial", Font.BOLD, 18));
	        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        panel.add(errorLabel);
	        return panel;
	    }

	    
	    String info = response.getText();
	    
	    String[] parts = info.split(",");
	    String name = parts[0];
	    String phone = parts[2];
	    // Title
	    JLabel titleLabel = new JLabel("MY PROFILE");
	    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
	    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panel.add(titleLabel);
	    panel.add(Box.createRigidArea(new Dimension(0, 20)));

	    // Student Name
	    JLabel nameLabel = new JLabel("Name: " + name);
	    nameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
	    nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panel.add(nameLabel);
	    panel.add(Box.createRigidArea(new Dimension(0, 10)));

	    // Placeholder phone and email (you can wire real data later)
	    JLabel phoneLabel = new JLabel("Phone: " + phone);
	    phoneLabel.setFont(new Font("Arial", Font.PLAIN, 18));
	    phoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panel.add(phoneLabel);
	    panel.add(Box.createRigidArea(new Dimension(0, 10)));

	    JLabel emailLabel = new JLabel("Email: [Not Available]");
	    emailLabel.setFont(new Font("Arial", Font.PLAIN, 18));
	    emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panel.add(emailLabel);
	    panel.add(Box.createRigidArea(new Dimension(0, 20)));

	    // Enrolled Courses header
	    JLabel coursesLabel = new JLabel("Enrolled Courses:");
	    coursesLabel.setFont(new Font("Arial", Font.BOLD, 20));
	    coursesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panel.add(coursesLabel);
	    panel.add(Box.createRigidArea(new Dimension(0, 10)));

	    // List of enrolled courses
	    if (enrolledCourseTitles.isEmpty()) {
	        JLabel noCoursesLabel = new JLabel("You are not enrolled in any courses.");
	        noCoursesLabel.setFont(new Font("Arial", Font.ITALIC, 16));
	        noCoursesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        panel.add(noCoursesLabel);
	    } else {
	        for (String courseTitle : enrolledCourseTitles) {
	            JLabel courseLabel = new JLabel("- " + courseTitle);
	            courseLabel.setFont(new Font("Arial", Font.PLAIN, 16));
	            courseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	            panel.add(courseLabel);
	            panel.add(Box.createRigidArea(new Dimension(0, 5)));
	        }
	    }

	    return panel;
	}


	private JScrollPane createHomePagePanel() {
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	    // Add the "MY SCHEDULE" label at the top
	    JLabel headerLabel = new JLabel("MY SCHEDULE");
	    headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
	    headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panel.add(headerLabel);
	    panel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing below the label

	    Message textMsg = new Message(Type.LIST_COURSES, Status.NULL, "");

	    try {
	        out.writeObject(textMsg);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    Message listResponse = null;
	    try {
	        listResponse = (Message) in.readObject();
	    } catch (ClassNotFoundException | IOException e1) {
	        e1.printStackTrace();
	    }

	    ArrayList<String> list = listResponse.getList();

	    for (String courseInfo : list) {

	        String[] parts = courseInfo.split(",");
	        String title = parts[0].trim();
	        String description = parts[1].trim();
	        String professor = parts[2].trim();
	        String capacity = parts[3].trim();
	        String units = parts[4].trim();
	        String enrolled = parts[5].trim();
	        String waitlisted = parts[6].trim();

	        JButton course = new JButton(title.toUpperCase() + "           Capacity:" + enrolled + "/" + capacity + "            Units: " + units);

	        course.addActionListener(e -> {
	            showListDetails(title, description, professor, capacity, units, enrolled, waitlisted);
	        });

	        course.setMaximumSize(new Dimension(900, 70));
	        panel.add(course);
	        panel.add(Box.createRigidArea(new Dimension(0, 5))); // spacing between buttons
	    }

	    JScrollPane scrollPane = new JScrollPane(panel);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	    return scrollPane;
	}


	private void showListDetails(String title, String description, String professor, String capacity, String units, String enrolled, String waitlisted) {
	    JDialog dialog = new JDialog(mainFrame, title, true);
	    dialog.setSize(500, 450);
	    dialog.setLocationRelativeTo(mainFrame);
	    dialog.setLayout(new BorderLayout(10,10));

	    JPanel detailPanel = new JPanel();
	    detailPanel.setLayout(new GridLayout(3,1,5,5));
	    detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 50));

	    JLabel prof = new JLabel(professor.toUpperCase());
	    JLabel desc = new JLabel("DESCRIPTION: " + description);
	    JButton drop = new JButton("DROP");

	    drop.addActionListener(e -> {
	        Message dropRequest = new Message(Type.DROP_COURSE, Status.NULL, title);
	        try {
	            out.writeObject(dropRequest);
	            
	            enrolledCourseTitles.remove(title);

	            JOptionPane.showMessageDialog(dialog, "Dropped " + title, "Drop", JOptionPane.INFORMATION_MESSAGE);
	            Timer timer = new Timer(500, event -> dialog.dispose());
	            timer.setRepeats(false);
	            timer.start();

	            SwingUtilities.invokeLater(() -> {
	            	
	                pagesPanel.remove(pagesPanel.getComponent(0));

	                JScrollPane updatedHomePage = createHomePagePanel();
	                pagesPanel.add(updatedHomePage, "HOME");

	                CardLayout cl = (CardLayout) pagesPanel.getLayout();
	                cl.show(pagesPanel, "HOME");
	            });

	        } catch (IOException ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(dialog, "Drop failed.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	        
	        SwingUtilities.invokeLater(() -> {
            	
                pagesPanel.remove(pagesPanel.getComponent(2));

                JScrollPane updatedCatalogPage = createCourseCatalogPagePanel();
                pagesPanel.add(updatedCatalogPage, "COURSE CATALOG");
            });
	        
	    });

	    detailPanel.add(prof);
	    detailPanel.add(desc);
	    detailPanel.add(drop);

	    dialog.add(detailPanel);
	    dialog.setVisible(true);
	}


	private JPanel CreateAdminAppPanel(CardLayout cardLayout, JPanel cardPanel) {
    	/*
    	 * actions:
    	 * 		create course
    	 * 		remove course
    	 * 		drop student from course
    	 * 		enroll student in course
    	 * 		add hold to student
    	 * 		remove hold from student
    	 * 		view all students
    	 * 		generate report
    	 */
    	JPanel appContainer = new JPanel(new BorderLayout(10, 10));

    	JPanel operationsPanel = new JPanel();
    	operationsPanel.setLayout(new GridLayout(8, 1, 1, 1));
        Font buttonFont = new Font("Arial", Font.PLAIN, 18);

    	JButton createCourseButton = new JButton("Create Course");
    	createCourseButton.setFont(buttonFont);
    	createCourseButton.setPreferredSize(new Dimension(500, 40));
    	
    	JButton removeCourseButton = new JButton("Remove Course");
    	removeCourseButton.setFont(buttonFont);
    	removeCourseButton.setPreferredSize(new Dimension(500, 40));
    	
    	JButton dropStudentFromCourseButton = new JButton("Drop Student From Course");
    	dropStudentFromCourseButton.setFont(buttonFont);
    	dropStudentFromCourseButton.setPreferredSize(new Dimension(500, 40));
    	
    	JButton enrollStudentToCourseButton = new JButton("Enroll Student To Course");
    	enrollStudentToCourseButton.setFont(buttonFont);
    	enrollStudentToCourseButton.setPreferredSize(new Dimension(500, 40));
    	
    	JButton addHoldButton = new JButton("Add Student Hold");
    	addHoldButton.setFont(buttonFont);
    	addHoldButton.setPreferredSize(new Dimension(500, 40));
    	
    	JButton removeHoldButton = new JButton("Remove Student Hold");
    	removeHoldButton.setFont(buttonFont);
    	removeHoldButton.setPreferredSize(new Dimension(500, 40));
    	
    	JButton viewAllStudents = new JButton("View All Students");
    	viewAllStudents.setFont(buttonFont);
    	viewAllStudents.setPreferredSize(new Dimension(500, 40));
    	
    	JButton generateReportButton = new JButton("Generate Report");
    	generateReportButton.setFont(buttonFont);
    	generateReportButton.setPreferredSize(new Dimension(500, 40));
    	
    	operationsPanel.add(createCourseButton, BorderLayout.WEST);
    	operationsPanel.add(removeCourseButton, BorderLayout.WEST);
    	operationsPanel.add(dropStudentFromCourseButton, BorderLayout.WEST);
    	operationsPanel.add(enrollStudentToCourseButton, BorderLayout.WEST);
    	operationsPanel.add(addHoldButton, BorderLayout.WEST);
    	operationsPanel.add(removeHoldButton, BorderLayout.WEST);
    	operationsPanel.add(viewAllStudents, BorderLayout.WEST);
    	operationsPanel.add(generateReportButton, BorderLayout.WEST);
    	
    	CardLayout pagesLayout = new CardLayout();
        JPanel pagesPanel = new JPanel(pagesLayout);

        JPanel createCoursePage = createCoursePanel();
        JPanel removeCoursepage = removeCoursePanel();
        JPanel dropStudentPage = dropStudentPanel();
        JPanel enrollStudentPage = enrollStudentPanel();
        JPanel addHoldPage = addHoldPanel();
        JPanel removeHoldPage = removeHoldPanel();
        JPanel viewStudentsPage = viewStudentsPanel();
        JPanel reportPage = reportPanel();

        pagesPanel.add(createCoursePage, "CREATE COURSE");
        pagesPanel.add(removeCoursepage, "REMOVE COURSE");
        pagesPanel.add(dropStudentPage, "DROP STUDENT");
        pagesPanel.add(enrollStudentPage, "ENROLL STUDENT");
        pagesPanel.add(addHoldPage, "ADD HOLD");
        pagesPanel.add(removeHoldPage, "REMOVE HOLD");
        pagesPanel.add(viewStudentsPage, "VIEW STUDENTS");
        pagesPanel.add(reportPage, "REPORTS");
    	
        // action listeners
        createCourseButton.addActionListener(e -> pagesLayout.show(pagesPanel, "CREATE COURSE"));
        removeCourseButton.addActionListener(e -> pagesLayout.show(pagesPanel, "REMOVE COURSE"));
        dropStudentFromCourseButton.addActionListener(e -> pagesLayout.show(pagesPanel, "DROP STUDENT"));
        enrollStudentToCourseButton.addActionListener(e -> pagesLayout.show(pagesPanel, "ENROLL STUDENT"));
        addHoldButton.addActionListener(e -> pagesLayout.show(pagesPanel, "ADD HOLD"));
        removeHoldButton.addActionListener(e -> pagesLayout.show(pagesPanel, "REMOVE HOLD"));
        viewAllStudents.addActionListener(e -> pagesLayout.show(pagesPanel, "VIEW STUDENTS"));
        generateReportButton.addActionListener(e -> pagesLayout.show(pagesPanel, "REPORTS"));

    	appContainer.add(operationsPanel, BorderLayout.WEST);
    	appContainer.add(pagesPanel, BorderLayout.CENTER);
    	
    	return appContainer;
    }
	
	private JPanel createCoursePanel() {		
		JPanel panel = new JPanel();
		
		JPanel courseTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel courseTitleLabel = new JLabel("Course Title");
		JTextField courseTitle = new JTextField(15);
		courseTitlePanel.add(courseTitleLabel);
		courseTitlePanel.add(courseTitle);
		
		JPanel professorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel professorLabel = new JLabel("Professor name");
		JTextField professorName = new JTextField(15);
		professorPanel.add(professorLabel);
		professorPanel.add(professorName);
		
		JPanel capacityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel capacityLabel = new JLabel("Class Capacity");
		JTextField capacity = new JTextField(15);
		capacityPanel.add(capacityLabel);
		capacityPanel.add(capacity);
		
		JPanel unitsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel unitsLabel = new JLabel("Course Units");
		JTextField units = new JTextField(15);
		unitsPanel.add(unitsLabel);
		unitsPanel.add(units);
		
		JPanel submitPanel = new JPanel();
		JButton submit = new JButton("Submit Course");
		submitPanel.add(submit);
		
		panel.add(courseTitlePanel);
		panel.add(professorPanel);
		panel.add(capacityPanel);
		panel.add(unitsPanel);
		panel.add(submit);
		
		submit.addActionListener(e -> {
			// sends message to create course to server
			Message message = null;
			String title = courseTitle.getText();
			String professor = professorName.getText();
			String courseCapacity = capacity.getText();
			String courseUnits = units.getText();
			
			String courseText = title + "," + professor + ","
								+ courseCapacity + "," + courseUnits;
			
			message = new Message(Type.CREATE_COURSE, Status.NULL, courseText);
			try {
				out.writeObject(message);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			Message createCourseResponse = null;
			try {
				createCourseResponse = (Message) in.readObject();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
			String msg = message.getText();
			switch(createCourseResponse.getStatus()) {
			case SUCCESS:
        		JOptionPane.showMessageDialog(panel, msg, "Hogwarts", JOptionPane.INFORMATION_MESSAGE);
				break;
			case FAILED:
        		JOptionPane.showMessageDialog(panel, msg, "Hogwarts", JOptionPane.ERROR_MESSAGE);
				break;
			default:
				break;
			}
		});
		return panel;
	}
	
	private JPanel removeCoursePanel() {
		JPanel panel = new JPanel();
		
		JPanel courseTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel courseTitleLabel = new JLabel("Course Title");
		JTextField courseTitle = new JTextField(15);
		courseTitlePanel.add(courseTitleLabel);
		courseTitlePanel.add(courseTitle);
		
		JPanel submitPanel = new JPanel();
		JButton submit = new JButton("Submit Course");
		submitPanel.add(submit);
		
		panel.add(courseTitlePanel);
		panel.add(submit);
		
		submit.addActionListener(e -> {
			Message removeCourseResponse = null;
			try {
				removeCourseResponse = (Message) in.readObject();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
			String msg = removeCourseResponse.getText();
			switch(removeCourseResponse.getStatus()) {
			case SUCCESS:
				JOptionPane.showMessageDialog(panel, msg, "Hogwarts", JOptionPane.INFORMATION_MESSAGE);
				break;
			case FAILED:
				JOptionPane.showMessageDialog(panel, msg, "Hogwarts", JOptionPane.ERROR_MESSAGE);
				break;
			default:
				break;
			}
		});
		return panel;
	}
	
	private JPanel dropStudentPanel() {
		JPanel panel = new JPanel();
		
		JPanel studentNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel studentNameLabel = new JLabel("Student Name");
		JTextField studentName = new JTextField(15);
		studentNamePanel.add(studentNameLabel);
		studentNamePanel.add(studentName);
		
		JPanel submitPanel = new JPanel();
		JButton submit = new JButton("drop student btn");
		submitPanel.add(submit);
		
		panel.add(studentNamePanel);
		panel.add(studentNamePanel);
		panel.add(submitPanel);
		
		submit.addActionListener(e -> {
			Message removeStudentresponse = null;
			try {
				removeStudentresponse = (Message) in.readObject();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
			String msg = removeStudentresponse.getText();
			switch(removeStudentresponse.getStatus()) {
			case SUCCESS:
				JOptionPane.showMessageDialog(panel, msg, "Hogwarts", JOptionPane.INFORMATION_MESSAGE);
				break;
			case FAILED:
				JOptionPane.showMessageDialog(panel, msg, "Hogwarts", JOptionPane.ERROR_MESSAGE);
				break;
			default:
				break;
			}
		});
		return panel;
	}
	
	private JPanel enrollStudentPanel() {
		JPanel panel = new JPanel();
		JButton btn = new JButton("enroll student btn");
		panel.add(btn);
		return panel;
	}
	
	private JPanel addHoldPanel() {
		JPanel panel = new JPanel();
		JButton btn = new JButton("add hold btn");
		panel.add(btn);
		return panel;
	}
	
	private JPanel removeHoldPanel() {
		JPanel panel = new JPanel();
		JButton btn = new JButton("remove hold btn");
		panel.add(btn);
		return panel;
	}
	
	private JPanel viewStudentsPanel() {
		JPanel panel = new JPanel();
		JButton btn = new JButton("view students btn");
		panel.add(btn);
		return panel;
	}
	
	private JPanel reportPanel() {
		JPanel panel = new JPanel();
		JButton btn = new JButton("report btn");
		panel.add(btn);
		return panel;
	}
}