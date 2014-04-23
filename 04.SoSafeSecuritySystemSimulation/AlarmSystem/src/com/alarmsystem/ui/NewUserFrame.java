package com.alarmsystem.ui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.alarmsystem.core.UserFactory;
import com.alarmsystem.core.User.UserType;


public class NewUserFrame extends javax.swing.JFrame {
	private static final long serialVersionUID = -7967817655449222140L;
	private JLabel lblUID;
	private JLabel lblMapFileName;
	private JButton btnOpenMap;
	private JButton btnSubmit;
	private JLabel lblSelMap;
	private JComboBox<String> cmbUserType;
	private JLabel lblUserType;
	private JTextField txtPassword;
	private JLabel lblPass;
	private JTextField txtUID;
	private String fname;
	
	// Singleton.
	private static NewUserFrame instance = null;
	public static synchronized NewUserFrame getInstance() {
		if (instance == null) 
			instance = new NewUserFrame();
		return instance;
	}
	private NewUserFrame() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			{
				lblUID = new JLabel();
				getContentPane().add(lblUID);
				lblUID.setText("UserID:");
				lblUID.setBounds(18, 44, 82, 14);
			}
			{
				txtUID = new JTextField();
				getContentPane().add(txtUID);
				txtUID.setBounds(112, 37, 180, 21);
			}
			{
				lblPass = new JLabel();
				getContentPane().add(lblPass);
				lblPass.setText("Password:");
				lblPass.setBounds(18, 70, 82, 14);
			}
			{
				txtPassword = new JTextField();
				getContentPane().add(txtPassword);
				txtPassword.setBounds(112, 64, 180, 21);
			}
			{
				lblUserType = new JLabel();
				getContentPane().add(lblUserType);
				lblUserType.setText("UserType:");
				lblUserType.setBounds(18, 96, 82, 14);
			}
			{
				ComboBoxModel<String> cmbUserTypeModel = 
						new DefaultComboBoxModel<String>(
								new String[] { "Normal User", "Premium User", "VIP" });
				cmbUserType = new JComboBox<String>();
				getContentPane().add(cmbUserType);
				cmbUserType.setModel(cmbUserTypeModel);
				cmbUserType.setBounds(112, 93, 180, 21);
			}
			{
				lblSelMap = new JLabel();
				getContentPane().add(lblSelMap);
				lblSelMap.setText("Select a map file:");
				lblSelMap.setBounds(18, 127, 114, 14);
			}
			{
				btnSubmit = new JButton();
				getContentPane().add(btnSubmit);
				btnSubmit.setText("Create user");
				btnSubmit.setBounds(71, 189, 170, 21);
		    	
		    	btnSubmit.addActionListener(new ActionListener() {
		    		@Override
		    		public void actionPerformed(ActionEvent arg0) {
						// validate the data
		    			if ((fname == null) || fname.isEmpty()) {
		    				JOptionPane.showMessageDialog(NewUserFrame.getInstance(), "Please choose a map file!");
		    				return;
		    			}
		    			
		    			if (txtUID.getText().equals("") || txtPassword.getText().equals("")) {
		    				JOptionPane.showMessageDialog(NewUserFrame.getInstance(), "User name and password should not be empty!");
		    				return;
		    			}
		    			
		    			if (UserFactory.getInstance().userExist(txtUID.getText())) {
		    				JOptionPane.showMessageDialog(NewUserFrame.getInstance(), "User name already exists! Please choose another one");
		    				return;
		    			}
		    			
		    			// Try creating the user
		    			UserType type;
		    			switch (cmbUserType.getSelectedIndex()) {
		    				case 0: type = UserType.NORMAL; break;
		    				case 1: type = UserType.PREMIUM; break;
		    				case 2: type = UserType.VIP; break;
		    				default: type = UserType.NORMAL;
		    			}
		    			String result = UserFactory.getInstance().addUser(txtUID.getText(), txtPassword.getText(), fname, type);
		    			if (result == null) { // Save successful
 		    				NewUserFrame.this.setVisible(false);
		    				// ToDo: Launch the MainFrame and pass the user to it.
 		    				MainFrame.getInstance().setUser(UserFactory.getInstance().getUser(txtUID.getText()));
 		    				txtUID.setText("");
 		    				txtPassword.setText("");
		    		    	lblMapFileName.setText("");
		    		    	fname = null;
		    			} else {
		    				JOptionPane.showMessageDialog(NewUserFrame.getInstance(), result);
		    			}
		    		}
		    	});

			}
			{
				btnOpenMap = new JButton();
				getContentPane().add(btnOpenMap);
				btnOpenMap.setText("Open Map File");
				btnOpenMap.setBounds(132, 124, 160, 21);
		    	
		    	btnOpenMap.addActionListener(new ActionListener() {
		    		@Override
		    		public void actionPerformed(ActionEvent arg0) {
		    		    JFileChooser chooser = new JFileChooser();
		    		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		    		        "JPG & PNG Images", "jpg", "png");
		    		    chooser.setFileFilter(filter);
		    		    int returnVal = chooser.showOpenDialog(NewUserFrame.this);
		    		    if (returnVal == JFileChooser.APPROVE_OPTION) {
		    		        fname = chooser.getSelectedFile().getPath();
		    		        lblMapFileName.setText("Map file name: " + fname);
		    		    } else {
		    		    	fname = null;
		    		    	lblMapFileName.setText("");
		    		    }
		    		}
		    	});

			}
			{
				lblMapFileName = new JLabel();
				getContentPane().add(lblMapFileName);
				lblMapFileName.setBounds(50, 153, 205, 14);
			}
			
	    	this.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosing(WindowEvent e) {
	            	NewUserFrame.getInstance().setVisible(false);
	            	LoginFrame.getInstance().setVisible(true);
	    		}
            });

			pack();
			this.setSize(341, 263);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}

}
