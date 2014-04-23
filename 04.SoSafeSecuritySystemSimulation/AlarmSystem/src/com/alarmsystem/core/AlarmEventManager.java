package com.alarmsystem.core;
import java.util.Map;
import java.util.Hashtable;
import java.util.Observer;
import java.util.Observable;

import com.alarmsystem.socket.ServerThread;


public class AlarmEventManager 	implements Observer {

	// Singleton
	private static AlarmEventManager aem = new AlarmEventManager();
	/* Here we use Hashtable rather than HashMap for that Hashtable can be synchronized and safe to threads.
	 * This program needs to handle many event threads and network threads so we should keep it thread safe. */
	Map<String, UserEvents> events = new Hashtable<String, UserEvents>();
	
	// Just for preventing the public construction of Singleton
	private AlarmEventManager() {
	}
	
	public static synchronized AlarmEventManager getInstance () {
		return aem;
	}
	
	public void addEvent(AlarmEvent ae) {
		// Find the UserEvent it belongs to
		String uid = ae.getSensor().getUser().getUID();
		UserEvents ue = events.get(uid);
		if (ue == null) {
			ue = new UserEvents(ae.getSensor().getUser());
			events.put(uid, ue);
		}
		// Add it into the relevant UserEvent
		ue.addEvent(ae);
	}
	
	public void addSocket(ServerThread st) {
		// Find the UserEvent it belongs to
		String uid = st.getUser().getUID();
		UserEvents ue = events.get(uid);
		if (ue == null) {
			ue = new UserEvents(st.getUser());
			events.put(uid, ue);
		}
		// Add it into the relevant UserEvent
		ue.addSocket(st);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof AlarmEvent) {
			AlarmEvent ae = (AlarmEvent) arg;
			addEvent(ae);
		}
	}

	public UserEvents getUserEvents(User u) {
		return events.get(u.getUID());
	}
}
