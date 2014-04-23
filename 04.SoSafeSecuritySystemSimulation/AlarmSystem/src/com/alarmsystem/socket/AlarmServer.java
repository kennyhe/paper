package com.alarmsystem.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.net.ServerSocket;
import java.net.Socket;

public class AlarmServer implements Runnable {
	//	 @Override	  
	public void run() {
		sockets = new ArrayList<Socket>();
		try{
			serversocket = new ServerSocket(12345);
			//waiting for client connection
			Socket sock;
	 		while (true) {
	 			sock = serversocket.accept();
	 			ServerThread st = new ServerThread();
	 			st.setSocket(sock);
	 			sockets.add(sock);
				st.start();
			}
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}
	
	
	public void closeASocket (Socket sock) {
		sockets.remove(sock);
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			e.printStackTrace();
		}
		System.out.println("Server socket closed.");
	}
	
	
	ServerSocket serversocket;
	ArrayList<Socket> sockets;
}
