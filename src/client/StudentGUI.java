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
	        boolean isAdmin = box.isSelected();
	        		
	        if (userName.isEmpty() || password.isEmpty()) {
	            JOptionPane.showMessageDialog(panel,
	                    "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        UserType userType = UserType.STUDENT;
	        if (isAdmin) {
	        	userType = UserType.ADMIN;
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
        
        appContainer.add(sidePanel, BorderLayout.WEST);
        
        
        
        return appContainer;
    }
    
    private JPanel CreateAdminAppPanel(CardLayout cardLayout, JPanel cardPanel) {
    	
    	JPanel appContainer = new JPanel(new BorderLayout(10, 10));
    	
    	
    	return appContainer;
    }
}