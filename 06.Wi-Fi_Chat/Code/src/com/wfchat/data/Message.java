package com.wfchat.data;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Message is a Custom Object to encapsulate message information/fields
 * 
 * @author Kenny
 *
 */
public class Message {
	/**
	 * The content of the message
	 */
	String message;
	/**
	 * boolean to determine, who is sender of this message
	 */
	int send;
	
	/**
	 * The id of the client who send/receive this message
	 */
	int clientID;
	
	/**
	 * The message id
	 */
	int msgID;
	
	/**
	 * The time this message was sent/received
	 */
	Timestamp time;
	
	
	/**
	 * Whether the message has been read
	 */
	// TBD. No do now.
	
	
	/**
	 * Constructor to make a Message object
	 */
	public Message(int mid, int cid, int send, String message, long t) {
		super();
		this.message	= message;
		this.send		= send;
		this.clientID	= cid;
		this.msgID		= mid;
		this.time		= new Timestamp(t);
	}
	
	public Message(int cid, boolean isSend, String message) {
		super();
		this.message	= message;
		this.send		= isSend?1:0;
		this.clientID	= cid;
		this.msgID		= 0;
		this.time		= new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
	}

	public String getMessage() {
		return message;
	}
	
	public boolean isMine() {
		return (send == 1);
	}
	
	public int getSend() {
		return send;
	}
	
	public int getClientID() {
		return clientID;
	}
	
	public int getMsgID() {
		return msgID;
	}
	
	public Timestamp getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = Timestamp.valueOf(time);
	}
	
	public void setTime(Timestamp time) {
		this.time = time;
	}
}
