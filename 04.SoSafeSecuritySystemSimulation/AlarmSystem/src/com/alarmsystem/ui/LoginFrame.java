package com.alarmsystem.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.alarmsystem.core.User;
import com.alarmsystem.socket.AlarmServer;

public class LoginFrame extends JFrame{
	
	private static final long serialVersionUID = -4854049874010636396L;
	private JLabel lblUser = new JLabel("User ID:");
	private JLabel lblPass = new JLabel("Password:");
	private JTextField txtUID = new JTextField();
	private JPasswordField txtPass = new JPasswordField();
	private JButton btnLogin = new JButton("Login");
	private JButton btnNewUser = new JButton("New User");

	private static LoginFrame instance = new LoginFrame();
	
	public static LoginFrame getInstance() {
		return instance;
	}
	
    /** Singleton */
    private LoginFrame() {
        initComponents();
    }
    
    private void initComponents() {
    	this.setSize(400, 300);
    	this.setLayout(new GridLayout(3,2));
    	this.add(lblUser);
    	this.add(txtUID);
    	this.add(lblPass);
    	this.add(txtPass);
    	this.add(btnLogin);
    	this.add(btnNewUser);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	btnLogin.addActionListener(new ActionListener() {
    		@SuppressWarnings("deprecation")
			@Override
    		public void actionPerformed(ActionEvent arg0) {
				User user = com.alarmsystem.core.UserFactory.getInstance().validUser(txtUID.getText(), txtPass.getText());
    			if (user != null) { // Valid success, go to the MainFrame.
        			txtUID.setText("");
        			txtPass.setText("");
    				LoginFrame.getInstance().setVisible(false);
    				// Launch the Mainframe.
    				MainFrame.getInstance().setUser(user);
    			} else { // Login failed
    				JOptionPane.showMessageDialog(LoginFrame.getInstance(), "Login failed: Password incorrect or user does not exist!");
    			}
    		}
    	});
    	
    	btnNewUser.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent arg0) {
				LoginFrame.getInstance().setVisible(false);
				// Launch the NewUserFrame.
				NewUserFrame.getInstance().setVisible(true);
    		}
    	});

    	this.setVisible(true);
    }
    
	public static void main(String[] args) {
		new Thread(new AlarmServer()).start();
		LoginFrame.getInstance();
	}
}
