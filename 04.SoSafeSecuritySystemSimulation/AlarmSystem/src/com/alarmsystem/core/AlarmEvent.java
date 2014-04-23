package com.alarmsystem.core;

import java.util.Date;
import java.util.Calendar;

public class AlarmEvent {
	private Sensor sensor;
	private Date time;
	private String info;	// Additional information for this alarm event
	
	AlarmEvent(Sensor s) {
		this.sensor = s;
		time  = new Date(Calendar.getInstance().getTimeInMillis());
		info = "";
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public Sensor getSensor() {
		return sensor;
	}

	public Date getTime() {
		return time;
	}

	public String getInfo() {
		return info;
	}
	
	public void setInfo(String i) {
		this.info = i;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(info).append("\n");
		sb.append(sensor.getPos().toString()).append("\t").append(time.toString());
		return sb.toString();
	}
}
