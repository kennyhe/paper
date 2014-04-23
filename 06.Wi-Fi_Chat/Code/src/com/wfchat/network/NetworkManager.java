package com.wfchat.network;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Observer;
import android.util.Log;


/**
 * @brief this class encapsulates all the client server communications, include:
 * 		- Create a local server socket and run it in a new thread, and publish the local ip address and port#;
 * 		- Handle local incoming request, and create a thread for that request.
 * 		- Maintain all client connections to servers.
 * @author kenny
 *
 */
public class NetworkManager {

	/**
	 * Constructor which create the local server thread, and do some other initializations.
	 */
	public NetworkManager() {
		server = new Server();
		new Thread(server).start();
		Log.i("wfchat.network", "local port:" + server.getPort());
		clients = new Hashtable<String, Client>();
	}
	
	
	private Client addClient(String addr, String port) {
		
		String key = addr + "_" + port;
		if (clients.containsKey(key)) {
			return clients.get(key);
		}

		Client client = new Client();
		
		if (client.connect(addr, port)) {
			clients.put(key, client);
			return client;
		}
		
		return null;
	}
	   
	
	
	public boolean sendMsg(String addr, String port, String msg) {
		Client client  = addClient(addr, port);
		
		if (client == null) {
			Log.w("wfchat.network", "Can't connect to server: " + addr + ":" + port);
			return false;
		}
		
		if (! client.sendMsg(msg)) {
			clients.remove(client);
			client  = addClient(addr, port);
			
			if (client == null) {
				Log.w("wfchat.network", "Can't connect to server: " + addr + ":" + port);
				return false;
			}
			
			if (! client.sendMsg(msg)) {
				Log.w("wfchat.network", "Failed to send the message [" + msg + "]");
			}
		}
		
		return true;
	}
	
	
	public void closeClients() {
		Iterator<Client> it = clients.values().iterator();
		while (it.hasNext()) {
			it.next().closeSocket();
		}
	}
	
	
	public void addObserver(Observer o) {
		server.addObserver(o);
	}
	
	public String getServerPort() {
		return server.getPort();
	}
	
	public void quitLocalServer() {
		server.closeServer();
	}
    
    
	Server server;
	Map<String, Client> clients;
}
