package com.alarmsystem.core;

import javax.swing.Icon;

import com.alarmsystem.core.GlobalSettings.SensorType;

public class DoorSensor extends Sensor {
	private static Icon ClosedDoor = null;
	private static Icon OpenedDoor = null;
	private static Icon Disabled = null;
	
	boolean doorClosed;
	static private final int CHECK_INTERVAL = 2000; // ms

	DoorSensor (Position p, User u, int id) {
		pos = p;
		user = u;
		doorClosed = true;
		sid = new StringBuffer("Door ").append(id).append("(").append(p.getX()).append(", ").append(p.getY()).append(")" ).toString();
	}
	

	@Override
	public SensorType getSensorType() {
		return SensorType.DOOR_SENSOR;
	}

	@Override
	public void run() {
		running = true;
		// Check the door sensor periodically, and trigger the event when necessary.
		while (running) {
			// Sleep an interval
			try {
				Thread.sleep(CHECK_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (SensorEventSimulator.getInstance().isDoorTriggered()) {
				doorClosed = ! doorClosed; // Open->Close, or Close->Open
				
				DoorAlarmEvent dae = new DoorAlarmEvent(this);
				if (doorClosed) {
					dae.setInfo("The door is closed.");
				} else {
					dae.setInfo("The door is opened.");
				}
			    setChanged();
			    notifyObservers(dae);
			}
		}
		// Since the removing sensor behavior is initialized from the SensorFactory, we need not notify the observer for this change.
		
		// Exit the current thread and wait it to die.
		// But the object should be put into a container to prevent being removed by garbage collection.
		// The reason is that sometimes the user need to check the bill history???
		// Thread.currentThread().join();
	}
	
	@Override
	public Icon getNormalIcon() {
		if (ClosedDoor == null)
			ClosedDoor = getIcon("door_closed.jpg");
		return ClosedDoor;
	}


	@Override
	public Icon getAlarmIcon() {
		if (OpenedDoor == null)
			OpenedDoor = getIcon("door_opened.jpg");
		return OpenedDoor;
	}


	@Override
	public Icon getDisabledIcon() {
		if (Disabled == null)
			Disabled = getIcon("door_disabled.jpg");
		return Disabled;
	}


	@Override
	public  synchronized boolean isAlarming() {
		if (! running)
			return false;
		
		if (doorClosed) {
			return false;
		} else {
			return true;
		}
	}

}
