package com.wfchat.data;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Hashtable;

import android.content.Context;
import android.util.Log;

import com.wfchat.network.NetworkManager;
import com.wfchat.network.ReceivedMessage;
import com.wfchat.ui.FriendsListActivity;
import com.wfchat.ui.MessageActivity;

// A singleton class for the whole project.
public class DataManager implements Observer {

	private static DataManager _instance = null;
	
	public static DataManager getInstance(Context context) {
		if (_instance == null) {
			if (context !=  null)
				_instance = new DataManager(context);
		}
		return _instance;
	}

	
	private DataManager(Context context) {
		network  = new NetworkManager();
		network.addObserver(this);
		db = new DatabaseHandler(context);
		// Initialize the friends list
		friends = db.getAllFriends();

		// Initialize the messages map
		msgs = new Hashtable<Friend, ArrayList<Message>>();
		if (! friends.isEmpty()) {
			Iterator<Friend> it  = friends.iterator();
			while (it.hasNext()) {
				Friend f = it.next();
				ArrayList<Message> ms = db.getLast10Messages(f.fid);
				msgs.put(f, ms);
			}
		}
	}

	
	// Handle the new messages from the Network interfaces.
	@Override
	public void update(Observable arg0, Object arg) {
		// TODO Auto-generated method stub
        if (arg instanceof ReceivedMessage) {
            ReceivedMessage resp = (ReceivedMessage) arg;
            Log.i("wfchat.network", "Received message from [" + resp.client + "]: " + resp.msg);
            
            // Save message to DB, and the Hash Map
            Friend f = getFriendByIP(resp.client);
            Message m = new Message(f.fid, false, resp.msg);
            saveMessage(f, m);
            
            if (f == curFriend) {
            	// If the incoming message is for the current message dialog, need to update the messages list.
            	//msgLA;
            }
        }
	}

	
	public void setListenActivity(FriendsListActivity fla) {
		friendsLA = fla;
	}
	
	public String getServerPort() {
		return network.getServerPort();
	}
	
	public Friend addActiveFriend(String dev_id, String ip, String port) {
		Friend f = db.getFriend(dev_id);
		if (f != null) {
			f.ip = ip;
			f.port = port;
			friends.add(f);
			ArrayList<Message> messages = new ArrayList<Message>();
			msgs.put(f, messages);
		}
		return f;
	}
	
	public Friend getFriendByIP(String ip) {
		Iterator<Friend> it = friends.iterator();
		while (it.hasNext()) {
			Friend f = it.next();
			if (f.ip.equals(ip)) 
				return f;
		}
		// Not found.
		return null;
	}
	
	public ArrayList<Friend> getFriendsList() {
		return friends;
	}
	
	public ArrayList<Message> getMessagesList(Friend f) {
		return msgs.get(f);
	}
	
	public void saveMessage(Friend f, Message m) {
        // Save message to DB.
        db.addMessage(m);
        
        // Update the message Hash Map
        ArrayList<Message> ms = msgs.get(f);
        ms.add(m);
	}
	
	public void setCurrentFriend(Friend f) {
		this.curFriend = f;
	}
	
	public Friend getCurrentFriend() {
		return curFriend;
	}

	
	MessageActivity 		msgLA 		= null;
	FriendsListActivity 	friendsLA 	= null;
	NetworkManager			network		= null;
	ArrayList<Friend>		friends;
	Map <Friend, ArrayList<Message>>		msgs;
	ArrayList<String>		friendNames;
	Friend 					curFriend;
	ArrayList<String>		curMessages;
	DatabaseHandler db;
}
