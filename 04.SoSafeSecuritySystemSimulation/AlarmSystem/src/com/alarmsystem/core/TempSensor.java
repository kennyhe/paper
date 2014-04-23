package com.alarmsystem.core;

import javax.swing.Icon;

import com.alarmsystem.core.GlobalSettings.SensorType;

public class TempSensor extends Sensor {
	private static Icon RegularTemp = null;
	private static Icon HighTemp = null;
	private static Icon Disabled = null;
	
	private int temp = 0;
	static private final int HIGH_LIMIT = 100;	// F.
	static private final int CHECK_INTERVAL = 1000; // ms
	private int lastTemp = 0;

	TempSensor (Position p, User u, int id) {
		pos = p;
		user = u;
		sid = new StringBuffer("Fire ").append(id).append("(").append(p.getX()).append(", ").append(p.getY()).append(")" ).toString();
	}
	
	
	/**
	 * Get the alarm ON/OFF change.
	 * @return True if the alarm is changed. If turned on or off, then return true; if keep last status, return no.
	 */
	private synchronized boolean testAlarmChange() {
		lastTemp = temp;
		// Get a pseudo temperature Ferrahet. In real world, the temperature will be retrieved from the real sensor. 
		temp = SensorEventSimulator.getInstance().getTemperature();
		return ((temp >= HIGH_LIMIT) && (lastTemp < HIGH_LIMIT))
				|| ((temp < HIGH_LIMIT) && (lastTemp >= HIGH_LIMIT));
	}

	@Override
	public SensorType getSensorType() {
		return SensorType.TEMPERATURE_SENSOR;
	}

	@Override
	public void run() {
		running = true;
		// Retrieve the temperature from the sensor periodically, and trigger the event when necessary.
		while (running) {
			// Sleep an interval
			try {
				Thread.sleep(CHECK_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (testAlarmChange()) { // Generate a temperature alarm to the listeners.
				TempAlarmEvent ae = new TempAlarmEvent(this);
				if (temp < HIGH_LIMIT) {
					ae.setInfo("Temperature alert disappeared.");
				} else {
					ae.setInfo("High temperature alert! Temperature is: " + temp);
				}
				ae.setTemp(temp);
			    setChanged();
			    notifyObservers(ae);
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
		if (RegularTemp == null)
			RegularTemp = getIcon("temp.jpg");
		return RegularTemp;
	}


	@Override
	public Icon getAlarmIcon() {
		if (HighTemp == null)
			HighTemp = getIcon("temp_high.jpg");
		return HighTemp;
	}


	@Override
	public Icon getDisabledIcon() {
		if (Disabled == null)
			Disabled = getIcon("temp_disabled.jpg");
		return Disabled;
	}


	@Override
	public synchronized boolean  isAlarming() {
		if (! running)
			return false;
		
		if (temp < HIGH_LIMIT) {
			return false;
		} else {
			return true;
		}
	}
	
	
}
