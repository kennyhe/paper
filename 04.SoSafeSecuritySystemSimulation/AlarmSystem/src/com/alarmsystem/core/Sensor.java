package com.alarmsystem.core;

import java.util.Calendar;
import java.util.Date;
import java.util.Observable;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.alarmsystem.core.GlobalSettings.SensorType;

public abstract class Sensor extends Observable implements Runnable {
	protected Position pos;
	protected User user;
	protected String info = "";
	protected boolean running = true;
	protected String sid;
	protected final Date createTime = Calendar.getInstance().getTime();
	
	public Position getPos() {
		return pos;
	}

	public User getUser() {
		return user;
	}
	
	public String getInfo() {
		return info;
	}
	
	// Check the type of the sensor.
	public abstract SensorType getSensorType();

	// Stop run the sensor
	public void stop() {
		running = false;
	}
	
	public synchronized boolean isRunning() {
		return running;
	}
	
	public abstract boolean isAlarming(); 
	
	public abstract Icon getNormalIcon();
	public abstract Icon getAlarmIcon();
	public abstract Icon getDisabledIcon();

	protected ImageIcon getIcon(String name) {
		return new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images").getPath() + "/" + name);
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	
	public String getID() {
		return sid;
	}
}
