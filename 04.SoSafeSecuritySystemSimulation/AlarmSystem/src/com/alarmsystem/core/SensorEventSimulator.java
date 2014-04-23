/**
 * Single pattern.
 * It simulates the current temperature value, door open/close event by a pseudo code generator.
 */
package com.alarmsystem.core;

import java.util.Random;

public class SensorEventSimulator {
	static private SensorEventSimulator instance = null;
	private Random rnd;
	
	private SensorEventSimulator() {
		rnd = new Random();
	}
	
	static public SensorEventSimulator getInstance() {
		if (instance == null)
			instance = new SensorEventSimulator();
		
		return instance;
	}
	
	public int getTemperature() {
		// Simulate the degree. Ranges from 0-127.
		return rnd.nextInt(128); 
	}
	
	public boolean isDoorTriggered() {
		// Simulate the door open/close trigger, with 27/128 probability triggered.
		return (rnd.nextInt(128) > 100);
	}
}