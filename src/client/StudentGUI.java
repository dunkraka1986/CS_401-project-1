package client;

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.Scanner;

import common.Message;
import common.Type;
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
    	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
    	mainFrame.setLocationRelativeTo(null);
    	
    	JPanel entryPanel = createEntryPanel();
        JPanel appPanel = createAppPanel();
    	
        mainFrame.add(entryPanel);
        
        mainFrame.setVisible(true);
    }

	private JPanel createEntryPanel() {
		
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

        
        JButton registerButton = new JButton("Register");
        ImageIcon registerIcon = new ImageIcon("Images/register.jpg");
        Image img1 = registerIcon.getImage();
        Image resizedImg1 = img1.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        registerIcon = new ImageIcon(resizedImg1);
        registerButton.setIcon(registerIcon);
        registerButton.setHorizontalTextPosition(SwingConstants.CENTER);
        registerButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        
        panel.add(loginButton);
        panel.add(registerButton);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 20, 0, 20); // top, left, bottom, right

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(loginButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(registerButton, gbc);
        
    	return panel;
    }
	
    private JPanel createAppPanel() {
		return null;
	}
}