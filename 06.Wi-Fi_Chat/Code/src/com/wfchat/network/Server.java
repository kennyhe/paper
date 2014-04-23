package com.wfchat.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import android.util.Log;


public class Server extends Observable implements Runnable {
	//	 @Override	  
	public void run() {
		sockets = new ArrayList<Socket>();
		try{
			serversocket = new ServerSocket(0);
			port = serversocket.getLocalPort();
			//waiting for client connection
			Socket sock;
	 		while (true) {
	 			sock = serversocket.accept();
	 			ServerThread st = new ServerThread();
	 			st.setSocket(sock);
	 			sockets.add(sock);
	 			st.setObservable(this);
				st.start();
			}
		} catch (IOException e) {
			Log.w("wfchat.network", "Server socket creating/accepting exception.", e);
		}
	}
	
	
	public void closeASocket (Socket sock) {
		sockets.remove(sock);
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.w("wfchat.network", "Error in closing a server side socket", e);
		}
	}
	
	
	public void closeServer() {
		try{
			Iterator<Socket> it = sockets.iterator();
			while (it.hasNext()) {
				it.next().close();
			}
			
	 		serversocket.close();
		} catch (IOException e) {
			Log.w("wfchat.network", "Error in closing all server side sockets.", e);
		}
		Log.i("wfchat.network", "Server socket closed.");
	}
	
	
	public String getPort() {
		while (port == 0)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.w("wfchat.network", "failed to sleep when wait for the server port", e);
			}
		return String.valueOf(port);
	}
	

	public void update(ReceivedMessage msg) {
        setChanged();
        notifyObservers(msg);
	}
	

	int port = 0;
	ServerSocket serversocket;
	ArrayList<Socket> sockets;
}
		  


class ServerThread extends Thread {
	public void run() {
		String line = "";
		boolean forceClosed;
		while (true) {
		    try {
				line = read.readLine();
				write.println("ACK");
				write.flush();
				if (line.equals("quitquit") || line.isEmpty()) {
					forceClosed = false;
		        	break;
				}
				ReceivedMessage msg = new ReceivedMessage();
				msg.client = client;
				msg.msg = line;
		        obs.update(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.w("wfchat.network", "Error in read/write from socket", e);
				forceClosed = true;
				break;
			}
		}
		if (! forceClosed)
			obs.closeASocket(sock);
	}
	
	public void setSocket(Socket sock) {
	    try {
	    	this.sock = sock;
	    	client = "" + sock.getInetAddress();
			read = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			write= new PrintWriter(sock.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.w("wfchat.network", "Error when open a socket for read/write", e);
		}
	}
	
	public void setObservable(Server ob) {
		this.obs = ob;
	}
	
	BufferedReader read;
	PrintWriter write;
	Socket sock;
	Server obs;
	String client;
}
