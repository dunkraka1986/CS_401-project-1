package client;

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.Scanner;

import common.Message;
import common.Type;
import common.UserType;
import common.Status;

public class StudentGUI {
	
	static JFrame mainFrame;
	static JPanel buttonPannel;
	static JPanel schedule;
	static JPanel courseCatalog;
	
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
        
        JPanel checkboxPanel = new JPanel();
        JCheckBox box = new JCheckBox("Are you an Admin");
        checkboxPanel.add(box);
        
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
	        		JOptionPane.showMessageDialog(panel,"Welcome Back " + userName, "Hogwarts", JOptionPane.INFORMATION_MESSAGE);
	        		// displays based on student or admin for cardPanel
	        		if (loginResponse.getUserType() == UserType.ADMIN) {
	        			cardLayout.show(cardPanel, "ADMINAPP");
	        			break;
	        		}
	        		cardLayout.show(cardPanel, "STUDENTAPP");
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
	    detailPanel.add(checkboxPanel);
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
        
        CardLayout pagesLayout = new CardLayout();
        JPanel pagesPanel = new JPanel(pagesLayout);

        JPanel homePage = createHomePagePanel();
        JPanel profilePage = createProfilePagePanel();
        JPanel courseCatalogPage = createCourseCatalogPagePanel();
        JPanel holdPage = createHoldPagePanel();
        JPanel balancePage = createBalancePagePanel();

        pagesPanel.add(homePage, "HOME");
        pagesPanel.add(profilePage, "PROFILE");
        pagesPanel.add(courseCatalogPage, "COURSE CATALOG");
        pagesPanel.add(holdPage, "HOLD");
        pagesPanel.add(balancePage, "BALANCE");
        

        viewCourseButton.addActionListener(e -> pagesLayout.show(pagesPanel, "HOME"));
        viewProfileButton.addActionListener(e -> pagesLayout.show(pagesPanel, "PROFILE"));
        viewCourseCatalogButton.addActionListener(e -> pagesLayout.show(pagesPanel, "COURSE CATALOG"));
        seeHoldsButton.addActionListener(e -> pagesLayout.show(pagesPanel, "HOLD"));
        seeBalanceButton.addActionListener(e -> pagesLayout.show(pagesPanel, "BALANCE"));

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

	private JPanel createCourseCatalogPagePanel() {
		
		JPanel panel = new JPanel();
		
		
		
		return panel;
	}

	private JPanel createProfilePagePanel() {
		JPanel panel = new JPanel();
		return panel;
	}

	private JPanel createHomePagePanel() {
		JPanel panel = new JPanel();
		return panel;
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