package com.alarmsystem.core;

import java.util.Calendar;
import java.util.Vector;
import java.util.Date;

import com.alarmsystem.bill.Bill;
import com.alarmsystem.bill.BillStrategy;
import com.alarmsystem.bill.BillPolicy;
import com.alarmsystem.socket.ServerThread;

public class UserEvents {
	
	private User user;
	private Vector<ServerThread> sockets = new Vector<ServerThread>(); // For synchronization. No ArrayList.
	private Vector<AlarmEvent> unBilledEvents = new Vector<AlarmEvent>();
	private Vector<AlarmEvent> billedEvents = new Vector<AlarmEvent>();
	private Vector<Bill> bills	= new Vector<Bill>();
	private Date lastBillDate = null;
	private Bill bill = null;	 // A latest bill.
	BillStrategy strategy;
	
	public UserEvents(User user) {
		this.user = user;
		strategy = BillPolicy.getStrategy(user);
		lastBillDate = user.getCreateTime();
	}
	

	public synchronized Bill getBill() {
		// Generate the bill with the billing policy.
		bill = new Bill(user, lastBillDate);
		lastBillDate = Calendar.getInstance().getTime();
		bill.addEvents(unBilledEvents);
		
		bill.applyStrategy(strategy);
		
		// Move all Alarm Events from unBilledEvents to BilledEvents. 
		for (AlarmEvent ae : unBilledEvents) {
			billedEvents.add(ae);
		}
		unBilledEvents.clear();
		bills.add(bill);
		
		// Dispatch bill information to all the clients.
		dispatchMsgToSocket(bill.getTextDetail());
		
		return bill;
	}
	
	public Vector<Bill> getBills() {
		return bills;
	}

	
	public synchronized void addEvent(AlarmEvent e) {
		unBilledEvents.add(e);
		
		// Dispatch it to all user clients by socket.
		dispatchMsgToSocket(e.toString());
	}
	
	private synchronized void dispatchMsgToSocket(String msg) {
		for (ServerThread st: sockets) {
			st.writeMsg(msg);
		}
	}
	
	public synchronized void addSocket(ServerThread st) {
		sockets.add(st);
	}
}
