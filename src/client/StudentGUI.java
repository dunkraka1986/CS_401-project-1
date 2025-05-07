package client;

import java.awt.*;
import java.awt.event.*;
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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 220)); // parchment tone

        // 🏰 Title Banner
        JLabel titleLabel = new JLabel("Welcome to Hogwarts", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(102, 51, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 10, 30, 10));
        panel.add(titleLabel, BorderLayout.NORTH);

        // 🎓 Center Buttons Panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(panel.getBackground());

        JButton loginButton = createStyledImageButton("Login", "Images/login.jpg");
        JButton registerButton = createStyledImageButton("Register", "Images/register.jpg");

        loginButton.addActionListener(e -> cardLayout.show(cardPanel, "LOGIN"));
        registerButton.addActionListener(e -> cardLayout.show(cardPanel, "REGISTER"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 40, 0, 40);

        gbc.gridx = 0;
        buttonPanel.add(loginButton, gbc);
        gbc.gridx = 1;
        buttonPanel.add(registerButton, gbc);

        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }
    
    private JButton createStyledImageButton(String text, String imagePath) {
        JButton button = new JButton(text);

        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        button.setIcon(icon);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);

        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setBackground(new Color(222, 184, 135));
        button.setForeground(new Color(60, 30, 10));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 2));
        button.setPreferredSize(new Dimension(320, 350));

        return button;
    }

	
	private JPanel createLoginPanel(CardLayout cardLayout, JPanel cardPanel) {
	    JPanel panel = new JPanel(new BorderLayout(10, 10));
	    panel.setBackground(new Color(245, 245, 220)); // parchment background

	    // 🖼️ Left Image Panel with Hogwarts crest
	    ImageIcon loginIcon = new ImageIcon("Images/login.jpg");
	    Image img = loginIcon.getImage().getScaledInstance(400, 600, Image.SCALE_SMOOTH);
	    loginIcon = new ImageIcon(img);
	    JLabel imageLabel = new JLabel(loginIcon);
	    imageLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 10));
	    panel.add(imageLabel, BorderLayout.WEST);

	    // 📜 Form Area
	    JPanel detailPanel = new JPanel();
	    detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
	    detailPanel.setBackground(panel.getBackground());
	    detailPanel.setBorder(BorderFactory.createEmptyBorder(80, 30, 80, 100));

	    // Title
	    JLabel titleLabel = new JLabel("Welcome to Hogwarts");
	    titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
	    titleLabel.setForeground(new Color(102, 51, 0));
	    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    detailPanel.add(titleLabel);
	    detailPanel.add(Box.createVerticalStrut(30));

	    // Username field
	    JPanel userNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    userNamePanel.setBackground(panel.getBackground());
	    JLabel userNameLabel = new JLabel("Full Name:");
	    userNameLabel.setFont(new Font("Serif", Font.PLAIN, 18));
	    JTextField userNameField = new JTextField(18);
	    userNameField.setFont(new Font("Serif", Font.PLAIN, 16));
	    userNamePanel.add(userNameLabel);
	    userNamePanel.add(userNameField);

	    // Password field
	    JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    passwordPanel.setBackground(panel.getBackground());
	    JLabel passwordLabel = new JLabel("Password:");
	    passwordLabel.setFont(new Font("Serif", Font.PLAIN, 18));
	    JPasswordField passwordField = new JPasswordField(18);
	    passwordField.setFont(new Font("Serif", Font.PLAIN, 16));
	    passwordPanel.add(passwordLabel);
	    passwordPanel.add(passwordField);

	    // Buttons
	    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    buttons.setBackground(panel.getBackground());

	    JButton submitButton = new JButton("Login");
	    styleHogwartsButton(submitButton);

	    JButton returnButton = new JButton("Return");
	    styleHogwartsButton(returnButton);

	    returnButton.addActionListener(e -> cardLayout.show(cardPanel, "ENTRY"));

	    buttons.add(submitButton);
	    buttons.add(returnButton);

	    // Add components to detail panel
	    detailPanel.add(userNamePanel);
	    detailPanel.add(Box.createVerticalStrut(15));
	    detailPanel.add(passwordPanel);
	    detailPanel.add(Box.createVerticalStrut(30));
	    detailPanel.add(buttons);

	    panel.add(detailPanel, BorderLayout.CENTER);

	    // 🔐 Login Button Logic
	    submitButton.addActionListener(e -> {
	        String userName = userNameField.getText().trim();
	        String password = new String(passwordField.getPassword());

	        if (userName.isEmpty() || password.isEmpty()) {
	            JOptionPane.showMessageDialog(panel, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        String student = userName + "," + password;
	        Message textMsg = new Message(Type.LOGIN, Status.NULL, UserType.STUDENT, student);

	        try {
	            out.writeObject(textMsg);
	            Message loginResponse = (Message) in.readObject();

	            switch (loginResponse.getStatus()) {
	                case SUCCESS:
	                    StudentGUI.student = userName;
	                    if (loginResponse.getList() != null) {
	                        enrolledCourseTitles = new ArrayList<>(loginResponse.getList());
	                    }

	                    JOptionPane.showMessageDialog(panel, "Welcome back, " + userName + "!", "Hogwarts", JOptionPane.INFORMATION_MESSAGE);

	                    if (loginResponse.getUserType() == UserType.ADMIN) {
	                        cardLayout.show(cardPanel, "ADMINAPP");
	                    } else {
	                        cardLayout.show(cardPanel, "STUDENTAPP");
	                        SwingUtilities.invokeLater(() -> {
	                            JScrollPane updatedHomePage = createHomePagePanel();
	                            pagesPanel.add(updatedHomePage, "HOME");
	                        });
	                    }
	                    break;

	                case FAILED:
	                    JOptionPane.showMessageDialog(panel, loginResponse.getText(), "Hogwarts", JOptionPane.ERROR_MESSAGE);
	                    break;

	                default:
	                    System.out.println("Unknown message type received.");
	                    break;
	            }

	        } catch (Exception ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(panel, "An error occurred during login.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    });

	    return panel;
	}

	private void styleHogwartsButton(JButton button) {
	    button.setFont(new Font("Serif", Font.BOLD, 18));
	    button.setBackground(new Color(222, 184, 135)); // parchment beige
	    button.setForeground(new Color(60, 30, 10));
	    button.setFocusPainted(false);
	    button.setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 2));
	    button.setPreferredSize(new Dimension(120, 40));
	}

	
	private JPanel createRegisterPanel(CardLayout cardLayout, JPanel cardPanel) {
	    JPanel panel = new JPanel(new BorderLayout(10, 10));
	    panel.setBackground(new Color(245, 245, 220)); // parchment tone

	    // 🏰 Image on the left
	    ImageIcon registerIcon = new ImageIcon("Images/register.jpg");
	    Image img1 = registerIcon.getImage().getScaledInstance(400, 600, Image.SCALE_SMOOTH);
	    registerIcon = new ImageIcon(img1);
	    JLabel imageLabel = new JLabel(registerIcon);
	    imageLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 10));
	    panel.add(imageLabel, BorderLayout.WEST);

	    // 📜 Form Area
	    JPanel detailPanel = new JPanel();
	    detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
	    detailPanel.setBackground(panel.getBackground());
	    detailPanel.setBorder(BorderFactory.createEmptyBorder(60, 30, 60, 100));

	    // Title
	    JLabel titleLabel = new JLabel("Hogwarts Registration");
	    titleLabel.setFont(new Font("Serif", Font.BOLD, 26));
	    titleLabel.setForeground(new Color(102, 51, 0));
	    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    detailPanel.add(titleLabel);
	    detailPanel.add(Box.createVerticalStrut(30));

	    // Helper function to create form rows
	    detailPanel.add(createLabeledField("First Name:", 18, "Serif", true));
	    JTextField firstNameField = (JTextField) ((JPanel) detailPanel.getComponent(2)).getComponent(1);

	    detailPanel.add(createLabeledField("Last Name:", 18, "Serif", true));
	    JTextField lastNameField = (JTextField) ((JPanel) detailPanel.getComponent(3)).getComponent(1);

	    detailPanel.add(createLabeledField("Phone Number:", 18, "Serif", true));
	    JTextField numberField = (JTextField) ((JPanel) detailPanel.getComponent(4)).getComponent(1);

	    detailPanel.add(createLabeledField("Password:", 18, "Serif", false));
	    JPasswordField passwordField = (JPasswordField) ((JPanel) detailPanel.getComponent(5)).getComponent(1);

	    detailPanel.add(createLabeledField("Confirm Password:", 18, "Serif", false));
	    JPasswordField confirmPasswordField = (JPasswordField) ((JPanel) detailPanel.getComponent(6)).getComponent(1);

	    // Buttons
	    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    buttons.setBackground(panel.getBackground());

	    JButton submitButton = new JButton("Register");
	    styleHogwartsButton(submitButton);

	    JButton returnButton = new JButton("Return");
	    styleHogwartsButton(returnButton);

	    buttons.add(submitButton);
	    buttons.add(returnButton);

	    detailPanel.add(Box.createVerticalStrut(20));
	    detailPanel.add(buttons);

	    returnButton.addActionListener(e -> cardLayout.show(cardPanel, "ENTRY"));

	    // ✉️ Submit logic
	    submitButton.addActionListener(e -> {
	        String firstName = firstNameField.getText().trim();
	        String lastName = lastNameField.getText().trim();
	        String phone = numberField.getText().trim();
	        String password = new String(passwordField.getPassword());
	        String confirmPassword = new String(confirmPasswordField.getPassword());

	        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
	            JOptionPane.showMessageDialog(panel, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        if (!password.equals(confirmPassword)) {
	            JOptionPane.showMessageDialog(panel, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        String student = firstName + " " + lastName + "," + password + "," + phone;
	        Message textMsg = new Message(Type.REGISTER, Status.NULL, student);

	        try {
	            out.writeObject(textMsg);
	            Message registerResponse = (Message) in.readObject();

	            switch (registerResponse.getStatus()) {
	                case SUCCESS:
	                    StudentGUI.student = firstName + " " + lastName;
	                    JOptionPane.showMessageDialog(panel,
	                            "Registration successful!\nWelcome to Hogwarts, " + StudentGUI.student,
	                            "Success", JOptionPane.INFORMATION_MESSAGE);
	                    cardLayout.show(cardPanel, "STUDENTAPP");
	                    break;
	                case FAILED:
	                    JOptionPane.showMessageDialog(panel, registerResponse.getText(), "Hogwarts", JOptionPane.ERROR_MESSAGE);
	                    break;
	                default:
	                    System.out.println("Unknown message type received.");
	                    break;
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(panel, "An error occurred during registration.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    });

	    panel.add(detailPanel, BorderLayout.CENTER);
	    return panel;
	}

	
	private JPanel createLabeledField(String label, int fontSize, String fontName, boolean isTextField) {
	    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    panel.setBackground(new Color(245, 245, 220));

	    JLabel jLabel = new JLabel(label);
	    jLabel.setFont(new Font(fontName, Font.PLAIN, fontSize));
	    JTextField field = isTextField ? new JTextField(15) : new JPasswordField(15);
	    field.setFont(new Font(fontName, Font.PLAIN, 16));

	    panel.add(jLabel);
	    panel.add(field);
	    return panel;
	}

    
    private JPanel createStudentAppPanel(CardLayout cardLayout, JPanel cardPanel) {
        JPanel appContainer = new JPanel(new BorderLayout());

        // 🎨 Themed Sidebar with wider width
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(new Color(139, 69, 19)); // deep wood tone
        sidePanel.setPreferredSize(new Dimension(250, 0)); // widened sidebar
        sidePanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        // 🏰 Hogwarts Logo
        ImageIcon logoIcon = new ImageIcon("Images/hogwarts.jpg");
        Image img = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(img);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidePanel.add(logoLabel);
        sidePanel.add(Box.createVerticalStrut(30));

        // 🪄 Big Fancy Buttons
        JButton viewCourseButton = createThemedSidebarButton("HOME");
        JButton viewProfileButton = createThemedSidebarButton("PROFILE");
        JButton viewCourseCatalogButton = createThemedSidebarButton("COURSE CATALOG");
        JButton seeHoldsButton = createThemedSidebarButton("HOLDS");
        JButton seeBalanceButton = createThemedSidebarButton("BALANCE");
        JButton logoutButton = createThemedSidebarButton("LOGOUT");

        sidePanel.add(viewCourseButton);
        sidePanel.add(Box.createVerticalStrut(15));
        sidePanel.add(viewProfileButton);
        sidePanel.add(Box.createVerticalStrut(15));
        sidePanel.add(viewCourseCatalogButton);
        sidePanel.add(Box.createVerticalStrut(15));
        sidePanel.add(seeHoldsButton);
        sidePanel.add(Box.createVerticalStrut(15));
        sidePanel.add(seeBalanceButton);
        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(logoutButton);

        // 📄 Pages Panel
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

        // 📌 Button Actions
        viewCourseButton.addActionListener(e -> pagesLayout.show(pagesPanel, "HOME"));

        viewProfileButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                JPanel updatedProfile = createProfilePagePanel();
                pagesPanel.add(updatedProfile, "PROFILE");
                pagesLayout.show(pagesPanel, "PROFILE");
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

    
    private JButton createThemedSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setFont(new Font("Serif", Font.BOLD, 16));
        button.setBackground(new Color(222, 184, 135)); // parchment beige
        button.setForeground(new Color(60, 30, 10));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 2));
        return button;
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
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
	    panel.setBackground(new Color(245, 245, 220)); // parchment tone

	    JPanel bannerPanel = new JPanel();
	    bannerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	    bannerPanel.setBackground(panel.getBackground());

	    ImageIcon logoIcon = new ImageIcon("Images/hogwarts.png");
	    Image img = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
	    logoIcon = new ImageIcon(img);
	    JLabel logoLabel = new JLabel(logoIcon);

	    JLabel titleLabel = new JLabel("Course Catalog");
	    titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
	    titleLabel.setForeground(new Color(102, 51, 0)); // deep brown

	    bannerPanel.add(logoLabel);
	    bannerPanel.add(Box.createRigidArea(new Dimension(15, 0)));
	    bannerPanel.add(titleLabel);

	    panel.add(bannerPanel);
	    panel.add(Box.createRigidArea(new Dimension(0, 20)));

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

	    Font titleFont = new Font("Serif", Font.BOLD, 18);
	    Font detailFont = new Font("Serif", Font.PLAIN, 16);

	    for (String courseInfo : list) {
	        String[] parts = courseInfo.split(",");
	        String title = parts[0].trim();
	        String description = parts[1].trim();
	        String professor = parts[2].trim();
	        String capacity = parts[3].trim();
	        String units = parts[4].trim();
	        String enrolled = parts[5].trim();
	        String waitlisted = parts[6].trim();

	        JPanel coursePanel = new JPanel(new BorderLayout(10, 10));
	        coursePanel.setBackground(new Color(255, 253, 208));
	        coursePanel.setBorder(BorderFactory.createCompoundBorder(
	                BorderFactory.createLineBorder(new Color(160, 82, 45), 2),
	                BorderFactory.createEmptyBorder(10, 15, 10, 15)
	        ));
	        coursePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
	        coursePanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

	        coursePanel.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                showCourseDetails(title, description, professor, capacity, units, enrolled, waitlisted);
	            }

	            @Override
	            public void mouseEntered(MouseEvent e) {
	                coursePanel.setBackground(new Color(255, 245, 180)); // hover effect
	            }

	            @Override
	            public void mouseExited(MouseEvent e) {
	                coursePanel.setBackground(new Color(255, 253, 208)); // revert
	            }
	        });

	        JLabel courseTitleLabel = new JLabel(title.toUpperCase());
	        courseTitleLabel.setFont(titleFont);
	        courseTitleLabel.setForeground(new Color(102, 51, 0));

	        JLabel detailsLabel = new JLabel("Professor: " + professor + " | Capacity: " + enrolled + "/" + capacity + " | Units: " + units);
	        detailsLabel.setFont(detailFont);

	        JPanel textPanel = new JPanel();
	        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
	        textPanel.setBackground(coursePanel.getBackground());
	        textPanel.add(courseTitleLabel);
	        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
	        textPanel.add(detailsLabel);

	        coursePanel.add(textPanel, BorderLayout.CENTER);
	        panel.add(coursePanel);
	        panel.add(Box.createRigidArea(new Dimension(0, 15)));
	    }


	    JScrollPane scrollPane = new JScrollPane(panel);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    scrollPane.getVerticalScrollBar().setUnitIncrement(16);

	    return scrollPane;
	}


	private void showCourseDetails(String title, String description, String professor, String capacity, String units, String enrolled, String waitlisted) {
	    JDialog dialog = new JDialog(mainFrame, title, true);
	    dialog.setSize(500, 450);
	    dialog.setLocationRelativeTo(mainFrame);
	    dialog.setLayout(new BorderLayout(10,10));
	    dialog.getContentPane().setBackground(new Color(245, 245, 220)); // parchment background

	    // -------------------------------
	    // 🏰 Top Banner with Logo + Title
	    // -------------------------------
	    JPanel bannerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    bannerPanel.setBackground(dialog.getContentPane().getBackground());

	    ImageIcon logoIcon = new ImageIcon("Images/hogwarts.png");
	    Image img = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
	    logoIcon = new ImageIcon(img);
	    JLabel logoLabel = new JLabel(logoIcon);

	    JLabel titleLabel = new JLabel(title.toUpperCase());
	    titleLabel.setFont(new Font("Serif", Font.BOLD, 22));
	    titleLabel.setForeground(new Color(102, 51, 0)); // deep brown

	    bannerPanel.add(logoLabel);
	    bannerPanel.add(Box.createRigidArea(new Dimension(10, 0)));
	    bannerPanel.add(titleLabel);

	    dialog.add(bannerPanel, BorderLayout.NORTH);

	    // -------------------------------
	    // 📘 Course Info Section
	    // -------------------------------
	    JPanel detailPanel = new JPanel();
	    detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
	    detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
	    detailPanel.setBackground(dialog.getContentPane().getBackground());

	    JLabel profLabel = new JLabel("Professor: " + professor);
	    profLabel.setFont(new Font("Serif", Font.PLAIN, 18));
	    profLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    JLabel descLabel = new JLabel("<html><body style='width:400px;'>Description: " + description + "</body></html>");
	    descLabel.setFont(new Font("Serif", Font.PLAIN, 16));
	    descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    JLabel capLabel = new JLabel("Capacity: " + enrolled + "/" + capacity);
	    capLabel.setFont(new Font("Serif", Font.PLAIN, 16));
	    capLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    JLabel unitLabel = new JLabel("Units: " + units);
	    unitLabel.setFont(new Font("Serif", Font.PLAIN, 16));
	    unitLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    JButton enroll = new JButton("ENROLL");
	    enroll.setFont(new Font("Serif", Font.BOLD, 16));
	    enroll.setBackground(new Color(160, 82, 45));
	    enroll.setForeground(Color.WHITE);
	    enroll.setFocusPainted(false);
	    enroll.setAlignmentX(Component.CENTER_ALIGNMENT);
	    enroll.setMaximumSize(new Dimension(150, 40));

	    if (enrolledCourseTitles.contains(title)) {
	        enroll.setEnabled(false);
	    }

	    enroll.addActionListener(e -> {
	        Message enrollRequest = new Message(Type.ENROLL_COURSE_STUDENT, Status.NULL, title);
	        try {
	            out.writeObject(enrollRequest);
	            Message response = (Message) in.readObject();

	            switch(response.getStatus()) {
	                case FAILED:
	                    JOptionPane.showMessageDialog(dialog, response.getText(), "Enrollment Error", JOptionPane.ERROR_MESSAGE);
	                    break;
	                case SUCCESS:
	                    enrolledCourseTitles.add(title);
	                    enroll.setEnabled(false);
	                    JOptionPane.showMessageDialog(dialog, response.getText(), "Enrollment Success", JOptionPane.INFORMATION_MESSAGE);
	                    dialog.dispose();
	                    break;
					default:
						break;
	            }

	        } catch (IOException | ClassNotFoundException ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(dialog, "Enrollment failed.", "Error", JOptionPane.ERROR_MESSAGE);
	        }

	        SwingUtilities.invokeLater(() -> pagesPanel.add(createHomePagePanel(), "HOME"));
	        SwingUtilities.invokeLater(() -> refreshPage("COURSE CATALOG", createCourseCatalogPagePanel()));
	    });

	    // Add components to detailPanel
	    detailPanel.add(profLabel);
	    detailPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	    detailPanel.add(descLabel);
	    detailPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	    detailPanel.add(capLabel);
	    detailPanel.add(unitLabel);
	    detailPanel.add(Box.createRigidArea(new Dimension(0, 20)));
	    detailPanel.add(enroll);

	    dialog.add(detailPanel, BorderLayout.CENTER);
	    dialog.setVisible(true);
	}



	private JPanel createProfilePagePanel() {
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
	    panel.setBackground(new Color(245, 245, 220)); // Light parchment background

	    // Hogwarts logo (assume "Images/hogwarts_logo.png" exists)
	    ImageIcon logoIcon = new ImageIcon("Images/hogwarts.jpg");
	    Image logo = logoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
	    JLabel logoLabel = new JLabel(new ImageIcon(logo));
	    logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panel.add(logoLabel);

	    // Styled title
	    JLabel titleLabel = new JLabel("My Profile");
	    titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
	    titleLabel.setForeground(new Color(102, 51, 0)); // Dark gold/brown
	    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panel.add(titleLabel);
	    panel.add(Box.createRigidArea(new Dimension(0, 20)));

	    // Retrieve profile data
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
	        errorLabel.setForeground(Color.RED);
	        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        panel.add(errorLabel);
	        return panel;
	    }

	    String[] parts = response.getText().split(",");
	    String name = parts[0];
	    String phone = parts[2];

	    // Info panel
	    JPanel infoPanel = new JPanel();
	    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
	    infoPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
	    infoPanel.setBackground(new Color(250, 250, 235));

	    infoPanel.add(makeProfileLine("Name: ", name));
	    infoPanel.add(makeProfileLine("Phone: ", phone));
	    infoPanel.add(makeProfileLine("Email: ", "[Not Available]"));
	    panel.add(infoPanel);
	    panel.add(Box.createRigidArea(new Dimension(0, 20)));

	    // Enrolled courses
	    JPanel coursePanel = new JPanel();
	    coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
	    coursePanel.setBorder(BorderFactory.createTitledBorder("Enrolled Courses"));
	    coursePanel.setBackground(new Color(250, 250, 235));

	    if (enrolledCourseTitles.isEmpty()) {
	        JLabel noCoursesLabel = new JLabel("You are not enrolled in any courses.");
	        noCoursesLabel.setFont(new Font("Arial", Font.ITALIC, 16));
	        noCoursesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        coursePanel.add(noCoursesLabel);
	    } else {
	        for (String course : enrolledCourseTitles) {
	            JLabel courseLabel = new JLabel("- " + course);
	            courseLabel.setFont(new Font("Arial", Font.PLAIN, 16));
	            coursePanel.add(courseLabel);
	            coursePanel.add(Box.createRigidArea(new Dimension(0, 5)));
	        }
	    }

	    panel.add(coursePanel);

	    return panel;
	}

	// Helper to create nicely formatted lines
	private JPanel makeProfileLine(String label, String value) {
	    JPanel line = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    line.setOpaque(false);
	    JLabel l1 = new JLabel(label);
	    l1.setFont(new Font("Arial", Font.BOLD, 16));
	    JLabel l2 = new JLabel(value);
	    l2.setFont(new Font("Arial", Font.PLAIN, 16));
	    line.add(l1);
	    line.add(l2);
	    return line;
	}



	private JScrollPane createHomePagePanel() {
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
	    panel.setBackground(new Color(245, 245, 220)); // parchment tone

	    JPanel bannerPanel = new JPanel();
	    bannerPanel.setLayout(new BoxLayout(bannerPanel, BoxLayout.X_AXIS));
	    bannerPanel.setBackground(panel.getBackground());

	    JLabel titleLabel = new JLabel("My Schedule");
	    titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
	    titleLabel.setForeground(new Color(102, 51, 0));

	    bannerPanel.add(Box.createHorizontalGlue());
	    bannerPanel.add(titleLabel);
	    bannerPanel.add(Box.createHorizontalGlue());
	    bannerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // limit vertical size

	    panel.add(bannerPanel);
	    panel.add(Box.createVerticalStrut(20));


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

	    Font titleFont = new Font("Serif", Font.BOLD, 18);
	    Font detailFont = new Font("Serif", Font.PLAIN, 16);

	    for (String courseInfo : list) {
	        String[] parts = courseInfo.split(",");
	        String title = parts[0].trim();
	        String description = parts[1].trim();
	        String professor = parts[2].trim();
	        String capacity = parts[3].trim();
	        String units = parts[4].trim();
	        String enrolled = parts[5].trim();
	        String waitlisted = parts[6].trim();

	        JPanel coursePanel = new JPanel(new BorderLayout(10, 10));
	        coursePanel.setBackground(new Color(255, 253, 208)); // lighter parchment
	        coursePanel.setBorder(BorderFactory.createCompoundBorder(
	            BorderFactory.createLineBorder(new Color(160, 82, 45), 2),
	            BorderFactory.createEmptyBorder(10, 15, 10, 15)
	        ));

	        JLabel courseTitleLabel = new JLabel(title.toUpperCase());
	        courseTitleLabel.setFont(titleFont);
	        courseTitleLabel.setForeground(new Color(102, 51, 0));

	        JLabel detailsLabel = new JLabel("Professor: " + professor + " | Enrolled: " + enrolled + "/" + capacity + " | Units: " + units);
	        detailsLabel.setFont(detailFont);

	        JPanel textPanel = new JPanel();
	        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
	        textPanel.setBackground(coursePanel.getBackground());
	        textPanel.add(courseTitleLabel);
	        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
	        textPanel.add(detailsLabel);

	        coursePanel.add(textPanel, BorderLayout.CENTER);

	        coursePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

	        // Make the whole course panel clickable
	        coursePanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	        coursePanel.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                showListDetails(title, description, professor, capacity, units, enrolled, waitlisted);
	            }
	        });

	        panel.add(coursePanel);
	        panel.add(Box.createRigidArea(new Dimension(0, 15)));
	    }

	    JScrollPane scrollPane = new JScrollPane(panel);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    scrollPane.getVerticalScrollBar().setUnitIncrement(16);

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
	            JScrollPane updatedCatalogPage = createCourseCatalogPagePanel();
	            refreshPage("COURSE CATALOG", updatedCatalogPage);
	        }); 
	    });

	    detailPanel.add(prof);
	    detailPanel.add(desc);
	    detailPanel.add(drop);

	    dialog.add(detailPanel);
	    dialog.setVisible(true);
	}
	
	private void refreshPage(String pageName, Component newPageContent) {
	    CardLayout layout = (CardLayout) pagesPanel.getLayout();

	    Component[] components = pagesPanel.getComponents();
	    for (Component comp : components) {
	        if (pageName.equals(comp.getName())) {
	            pagesPanel.remove(comp);
	            break;
	        }
	    }

	    newPageContent.setName(pageName);
	    pagesPanel.add(newPageContent, pageName);
	    pagesPanel.revalidate();
	    pagesPanel.repaint();
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
		
		JPanel reportDataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ArrayList<JLabel> data = new ArrayList<JLabel>();
		
		JPanel generatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton generateButton = new JButton("Generate Report");
		generateButton.setPreferredSize(new Dimension(500, 500));
		generatePanel.add(generateButton);
		
		generateButton.addActionListener(e -> {
			Message reportResponse = null;
			try {
				reportResponse = (Message) in.readObject();
			} catch(ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
			String[] reportFileData = reportResponse.getText().split(",");
			
			for (int i = 0; i < reportFileData.length; i++) {
				JLabel newData = new JLabel(reportFileData[i]);
				data.add(newData);
				reportDataPanel.add(data.get(i));
			}
		});
		
		panel.add(reportDataPanel);
		panel.add(generatePanel);
		
		return panel;
	}
}