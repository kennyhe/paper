package com.alarmsystem.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

import com.alarmsystem.core.AlarmEventManager;
import com.alarmsystem.core.User;
import com.alarmsystem.core.UserFactory;

public class ServerThread extends Thread {
	public void run() {
		try {
			String line = read.readLine();
			String keys[] = line.split("/");
			user = UserFactory.getInstance().validUser(keys[0], keys[1]);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (user == null) {
			try {
				write.println("Wrong user and password.");
				write.flush();
				sock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		} else {
			AlarmEventManager.getInstance().addSocket(this);
		}

		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			writeResult();
		}
	}
	
	public void setSocket(Socket sock) {
	    try {
	    	this.sock = sock;
			read = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			write= new PrintWriter(sock.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** Add message into write buffer */
	public void writeMsg(String s) {
		buf.add(s);
	}
	
	public User getUser() {
		return user;
	}
	
	/** write feedback */
	private synchronized void writeResult() {
		if (! buf.isEmpty()) {
			for (String s: buf)
				write.println(s);
			buf.clear();
		}
		write.flush();
	}
	
	BufferedReader read;
	PrintWriter write;
	Socket sock;
	User user = null;
	Vector<String> buf = new Vector<String>();
}