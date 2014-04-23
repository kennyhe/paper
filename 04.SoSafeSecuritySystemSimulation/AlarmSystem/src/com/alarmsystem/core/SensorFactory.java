package com.alarmsystem.core;

import java.util.Hashtable;
import java.util.Vector;
import com.alarmsystem.core.GlobalSettings.SensorType;

public class SensorFactory {
	private static SensorFactory inst = null;
	private static int idSeed = 1;
	private Hashtable<String, Vector<Sensor>> sensors = new Hashtable<String, Vector<Sensor>>();
	private AlarmEventManager aem = AlarmEventManager.getInstance();
	
	// private empty constructor, for singleton.
	private SensorFactory() {
	}
	
	public static synchronized SensorFactory getInstance () {
		if (null == inst)
			inst = new SensorFactory();
		
		return inst;
	}
	
	public synchronized Sensor addNewSensor(Position p, SensorType type, User u) {
		String uid = u.getUID();

		// Get the Sensor group for the current user.
		Vector<Sensor> vs = sensors.get(uid);
		if (null == vs) {
			vs = new Vector<Sensor>();
			sensors.put(uid, vs);
		}
		
		Sensor s;
		if (type == SensorType.DOOR_SENSOR) {
			s = new DoorSensor(p, u, idSeed++);
		} else if (type == SensorType.TEMPERATURE_SENSOR) {
			s = new TempSensor(p, u, idSeed++);
		} else {
			System.out.println("Unknown sensor type");
			return null;
		}
		
		vs.add(s);
		(new Thread(s)).start();
		s.addObserver(aem);
		return s;
	}
	
	public Vector<Sensor> getSensors(User u) {
		return sensors.get(u.getUID());
	}
	
	public Vector<Sensor> getSensors(String u) {
		return sensors.get(u);
	}
}
