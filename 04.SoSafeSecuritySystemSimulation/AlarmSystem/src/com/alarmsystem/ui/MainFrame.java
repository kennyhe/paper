package com.alarmsystem.ui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.WindowConstants;

import com.alarmsystem.bill.Bill;
import com.alarmsystem.core.*;

public class MainFrame extends JFrame implements Observer{
	
	private static final long serialVersionUID = -4517913762151679093L;
	private JLabel background = null;
	private	JPopupMenu	popupMenuNew;
	private int newX, newY; // The position of the new Sensor
	private User user;
	// The buttons are used for displaying the sensors.
	private Hashtable<Sensor, JButton> buttons = new Hashtable<Sensor, JButton>();
	
	// Singleton
	private static MainFrame instance = null;
	public static synchronized MainFrame getInstance() {
		if (instance == null) 
			instance = new MainFrame();
		
		return instance;
	}
    private MainFrame() {
        //Create and set up the window.
		super("Security System"); 
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    	setLayout(new BorderLayout());
    	background=new JLabel();
    	this.add(background);
    	background.setLayout(null);

    	// Create some menu items for the the creating sensor menu.
		JMenuItem menuNewDoorSensor = new JMenuItem( "New Door Sensor" );
		JMenuItem menuNewFireSensor = new JMenuItem( "New Fire Sensor" );
		popupMenuNew = new JPopupMenu();
		popupMenuNew.add( menuNewDoorSensor );
		popupMenuNew.add( menuNewFireSensor );
		background.add(popupMenuNew);

		// Action and mouse listener support
		enableEvents( AWTEvent.MOUSE_EVENT_MASK );
		menuNewDoorSensor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println(e);
				Sensor s = SensorFactory.getInstance().addNewSensor(new Position(newX, newY), 
						GlobalSettings.SensorType.DOOR_SENSOR, user);
				createSensorUI(s);
			}
			
		});
		menuNewFireSensor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//if (e.)
				Sensor s = SensorFactory.getInstance().addNewSensor(new Position(newX, newY), 
						GlobalSettings.SensorType.TEMPERATURE_SENSOR, user);
				createSensorUI(s);
			}
			
		});


		// Before close.
		this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	MainFrame.this.setVisible(false);
            	LoginFrame.getInstance().setVisible(true);
    		}
        });
	}

    // Handle the mouse click events, no matter right of left click.
	public void processMouseEvent( MouseEvent event ) {
		if(event.isPopupTrigger()) {
			popupMenuNew.show(event.getComponent(),event.getX(), event.getY());
			newX = event.getX() - 5;
			if (newX < 0) newX = 0;
			newY = event.getY() - 24;
			if (newY < 0) newY = 0;
		}

		//super.processMouseEvent( event );
	}
	
    public void setUser(User user) {
    	if (user == null)
    		return;
    	
    	this.user = user;
    	background.removeAll(); // Remove old components;
    	buttons.clear();
    	Icon img = new ImageIcon(user.getMapFilename());
    	background.setIcon(img);
    	Vector<Sensor> sensors = SensorFactory.getInstance().getSensors(user.getUID());
    	if (null != sensors) {
	    	for (Sensor s : sensors) {
	    		createSensorUI(s);
	    	}
    	}
    	
    	JButton btnShowBill = new JButton("Show Bill");
    	JButton btnShowAllBill = new JButton("Show All Bill");
    	background.add(btnShowBill);
    	background.add(btnShowAllBill);
    	btnShowBill.setBounds(10, 10, 120, 20);
    	btnShowBill.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String text;
				UserEvents ue = AlarmEventManager.getInstance().getUserEvents(MainFrame.this.user);
				if (ue == null) {
					text = "No bills now";
				} else {
					text = ue.getBill().getTextDetail();
				}
				JOptionPane.showMessageDialog(MainFrame.this, text);
			}
    	});
    	btnShowAllBill.setBounds(10, 40, 120, 20);
    	btnShowAllBill.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String text;
				UserEvents ue = AlarmEventManager.getInstance().getUserEvents(MainFrame.this.user);
				if (ue == null) {
					text = "No bills now";
				} else {
					StringBuffer sb = new StringBuffer("==================== All Bill History ====================\n\n");
					for (Bill b : ue.getBills()) {
						sb.append(b.getTextDetail()).append("\n\n");
					}
					text = sb.toString();
				}
				JOptionPane.showMessageDialog(MainFrame.this, text);
			}
    	});
    	
	    this.pack();
	    this.setSize(img.getIconWidth(), img.getIconHeight());
	    this.setVisible(true);
    }

    private synchronized void createSensorUI(Sensor s) {
		JButton b = new JButton();
		if (s.isRunning()) {
			b.setIcon(s.getNormalIcon());
		} else {
			b.setIcon(s.getDisabledIcon());
		}
		b.addActionListener(new ButtonActionListener(s, b));
    	// Draw sensors
		background.add(b);
		b.setBounds(s.getPos().getX(), s.getPos().getY(), GlobalSettings.ICON_WIDTH, GlobalSettings.ICON_HEIGHT);
		buttons.put(s, b);
		
		// Observe the sensor status change.
		s.addObserver(this);
    }
    
	@Override
	public void update(Observable arg0, Object arg1) {
		Sensor s = (Sensor) arg0;
		JButton b = buttons.get(s);
		if (s.isRunning()) {
			if (s.isAlarming())
				b.setIcon(s.getAlarmIcon());
			else
				b.setIcon(s.getNormalIcon());
		} else {
			b.setIcon(s.getDisabledIcon());
		}
	}

	class ButtonActionListener implements ActionListener {
		private Sensor s;
		private JButton b;
		ButtonActionListener(Sensor s, JButton b) {
			this.s = s;
			this.b = b;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// Toggle the running state
			if (s.isRunning()) {
				s.stop();
				b.setIcon(s.getDisabledIcon());
			} else {
				new Thread(s).start();
				b.setIcon(s.getNormalIcon());
			}
		}
	}
}
