package com.wfchat.ui;

import java.util.ArrayList;
import com.wfchat.ui.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.wfchat.data.DataManager;
import com.wfchat.data.Message;


/**
 * MessageActivity is a main Activity to show a ListView containing Message items
 * 
 * @author Meenakshy, Kenny
 *
 */
public class MessageActivity extends Activity {
	/** Called when the activity is first created. */

	ArrayList<Message> messages;
	MessageAdapter adapter;
    ListView mainListView ;
	EditText text;
	DataManager dm = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);
		
		text = (EditText) this.findViewById(R.id.text);

		// Find the ListView resource. 
	    mainListView = (ListView) findViewById(R.id.list);
		
		this.setTitle("Chat with Soujanya");
		messages = new ArrayList<Message>();

		// Load last 10 chatting messages from the data base.
		
/*
		messages.add(new Message("Hello", false));
		messages.add(new Message("Hi!", true));
*/
		adapter = new MessageAdapter(WFChat.getContext(), messages);
		mainListView.setAdapter(adapter);
		addNewMessage(new Message(1, true, "mmm, well, using 9 patches png to show them."));
//		addNewMessage(new Message(dm.getCurrentFriend().getFid(), true, "mmm, well, using 9 patches png to show them."));
	}
	
	
	
	public void sendMessage(View v) {
		String newMessage = text.getText().toString().trim(); 
		if(newMessage.length() > 0)
		{
			text.setText("");
//			addNewMessage(new Message(dm.getCurrentFriend().getFid(), true, newMessage));
			addNewMessage(new Message(1, true, newMessage));
		}
	}
	
	
	public void addNewMessage(Message m) {
		messages.add(m);
		adapter.notifyDataSetChanged();
		mainListView.setSelection(messages.size()-1);
	}
}