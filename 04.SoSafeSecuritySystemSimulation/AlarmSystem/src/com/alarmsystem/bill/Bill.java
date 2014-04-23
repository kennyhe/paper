package com.alarmsystem.bill;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.alarmsystem.core.*;


public class Bill {
	private User user;
	private Date startTime, endTime;
	private Vector<AlarmEvent> events = new Vector<AlarmEvent>();
	private String txt = null;
	private double cost = -1.0;
	
	public Bill(User user, Date start) {
		this.user = user;
		this.startTime = start;
		this.endTime = Calendar.getInstance().getTime();
	}
	
	public User getUser() {
		return user;
	}

	/**
	 * @return the startTime
	 */
	public final Date getStartTime() {
		return startTime;
	}

	/**
	 * @return the endTime
	 */
	public final Date getEndTime() {
		return endTime;
	}

	/**
	 * @return the events
	 */
	public final Vector<AlarmEvent> getEvents() {
		return events;
	}

	/**
	 * @param events the events to set
	 */
	public final void addEvents(Vector<AlarmEvent> events) {
		this.events.clear();
		for (AlarmEvent e : events) {
			this.events.add(e);
		}
	}

	/**
	 * @return The text details of the bill
	 */
	public final String getTextDetail() {
		if (txt == null) {
			// Generate text version of the bill
			applyStrategy(BillPolicy.getStrategy(user));
		}
		
		return txt;
	}
	

	/**
	 * @return The total cost of the bill
	 */
	public final double getCostSum() {
		if (cost < 0.0) {
			// Generate text version of the bill
			applyStrategy(BillPolicy.getStrategy(user));
		}
		
		return cost;
	}
	/**
	 * Use the bill policy to generate bill.
	 * @param p
	 */
	public void applyStrategy(BillStrategy p) {
		cost = p.getTotalCost(this);
		txt = p.generateTxtDetails(this);
	}
}
