package com.alarmsystem.core;

public class TempAlarmEvent extends AlarmEvent {
	int temp;

	TempAlarmEvent(Sensor s) {
		super(s);
	}

	/**
	 * @return the temp
	 */
	public int getTemp() {
		return temp;
	}

	/**
	 * @param temp the temp to set
	 */
	public void setTemp(int temp) {
		this.temp = temp;
	}
	
}
