package com.wfchat.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import android.R;
import java.util.Random;
import java.util.Timer;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.wfchat.interfaces.Manage;
import com.wfchat.interfaces.Update;
import com.wfchat.network.*;
/**
 * This is an example of implementing an application service that runs locally
 * in the same process as the application.  The {@link LocalServiceController}
 * and {@link LocalServiceBinding} classes show how to interact with the
 * service.
 *
 * 
 * This is generally how background services should
 * interact with the user, rather than doing something more disruptive such as
 * calling startActivity().
 */
@SuppressWarnings("unused")
public abstract class IMService extends Service implements Manage, Update {

	
	public static final String TAKE_MESSAGE = "Take_Message";
	public static final String FRIEND_LIST_UPDATED = "Take Friend List";
	public ConnectivityManager conManager = null; 
	private final int UPDATE_TIME_PERIOD = 15000;
    private static final int LISTENING_PORT_NO = 8956;
	private String rawFriendList = new String();


	//SocketOperator socketOperator = new SocketOperator(this); define socket used here

	private final IBinder mBinder = new IMBinder();
	private String username;
	private String password;
	private String userKey;
	private boolean authenticatedUser = false;
	 // timer to take the updated data from server
	private Timer timer;
	
	private NotificationManager mNM;
	
	
	public class IMBinder extends Binder {
		public Manage getService() {
			return IMService.this;
		}
	}
	
	   
    @Override
    public void onCreate() 
    {   	
         mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
     //   showNotification();
    	conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    	
    	
    	// Timer is used to take the friendList info every UPDATE_TIME_PERIOD;
		timer = new Timer();   
		
		final Thread thread = new Thread()
		{
			@Override
			public void run() {			
				
			   
				Random random = new Random();
				int tryCount = 0;
				
				while ((10000 + random.nextInt(20000))  == 0 )
				{		
					tryCount++; 
					if (tryCount > 10)
					{
						// if it can't listen a port after trying 10 times, give up...
						break;
					}
					
				}
			}
		};	
		thread.start();
    
    }
		

/*
    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(R.string.local_service_started);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }
*/	

	public IBinder onBind(Intent intent) 
	{
		return mBinder;
	}




	/**
	 * Show a notification while this service is running.
	 * @param msg 
	 **/
    
	 

	public String getUsername() {		
		return username;
	}
	
		

	
	public void exit() 
	{
		timer.cancel();
		this.stopSelf();
	}
}
	
	
	
	
