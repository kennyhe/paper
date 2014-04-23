package com.wfchat.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import android.util.Log;

/**
 * @brief The client code. It create and maintain the client socket to the server, and send 
 * 		  the message to the server.
 * @author kenny
 *
 */
public class Client {
	/**
	 * @brief Create a client socket to the server. 
	 * @param addr The hostname or ip address of the server to be connected
	 * @param port The port number of the server to be connected
	 * @return true if connected. false if connection failed
	 */
	public boolean connect(String addr, String port) {
        try {
           // Connect to Chat Server
           socket = new Socket(addr, Integer.parseInt(port));
           in = new BufferedReader(
               new InputStreamReader(socket.getInputStream()));
           out = new PrintWriter(
               new OutputStreamWriter(socket.getOutputStream()));
			Log.v("wfchat.network", "Connected to server " + addr + ":" + port);
        } catch (IOException ioe) {
			Log.w("wfchat.network", "Can not establish connection to " + addr + ":" + port, ioe);
			return false;
        }
        return true;
	}
 

	/**
	 * Send a message to the server connected. Before doing this, the client must be connected first.
	 * @param msg The content of the message to be send.
	 * @return true if send and get the "ACK" from the server side. false if not.
	 */
	public boolean sendMsg(String msg) {
        try {
        	out.println(msg);
        	out.flush();
        	Log.v("wfchat.network", "Message flushed out.");
        	
        	// Read messages from the server and print them
            String message;
            message=in.readLine();
            if (message.equals("ACK")) {
            	Log.v("wfchat.network", "Message successfully delivered");
            } else {
            	return false;
            }
            
        } catch (IOException ioe) {
        	Log.w("wfchat.network", "Connection to server broken.", ioe);
			return false;
        }
 		
		return true;
	}
	
	
	/**
	 * @brief close the client socket. Must do if before app exit.
	 */
	public void closeSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.w("wfchat.network", "Failed to close client socket", e);
		}
	}
	

	private BufferedReader in = null;
    private PrintWriter out = null;
    private Socket  socket = null;
	
}
