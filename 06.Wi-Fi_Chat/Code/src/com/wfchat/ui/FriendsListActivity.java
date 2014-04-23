package com.wfchat.ui;

import java.util.ArrayList;

import com.wfchat.ui.R;
import com.wfchat.data.DataManager;
import com.wfchat.data.Friend;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class FriendsListActivity extends Activity {
    
    private ListView mainListView ;
    private ArrayAdapter<Friend> listAdapter ;
    
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendslist);
      
    	//Initialize data
    	dm = DataManager.getInstance(null);
    	dm.setListenActivity(this);
    	
	    // Find the ListView resource. 
	    mainListView = (ListView) findViewById(R.id.friendslist);

        ArrayList<Friend> fl = dm.getFriendsList();
        if (fl.isEmpty()) {
        	fl = new ArrayList<Friend>();
        	Friend f = new Friend(0, "Click to scan for a new friend", null, null, null, "", null, 0);
        	fl.add(f);
        }
        
      
        // Create ArrayAdapter using the list.
        listAdapter = new FriendsListAdapter<Friend> (WFChat.getContext(), R.layout.friendlistitem, fl);
      
           
        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );
        
        
    }

    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)     {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.friendsmenu, menu);
        return true;
    }
    
    
   /**
    * Event Handling for Individual menu item selected
    * Identify single menu item by it's id
    * */
   @Override
   public boolean onOptionsItemSelected(MenuItem item)
   {
        
       switch (item.getItemId())
       {
       case R.id.itemFind:
    	   startActivity(new Intent(FriendsListActivity.this, com.wfchat.ui.WiFiDirectActivity.class));
           return true;

       case R.id.itemMessage:
    	   startActivity(new Intent(FriendsListActivity.this, com.wfchat.ui.MessageActivity.class));
           return true;

       case R.id.itemMySelf:
           Toast.makeText(this, "Code still not finished", Toast.LENGTH_SHORT).show();
           return true;
       default:
           return super.onOptionsItemSelected(item);
       }
   }    

   DataManager dm = null;
}
