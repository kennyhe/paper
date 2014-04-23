package com.wfchat.ui;

import com.wfchat.data.DataManager;

import android.app.Application;
import android.content.Context;

public class WFChat extends Application {
	private static Context _context = null;
	
    public void onCreate(){
        super.onCreate();
        _context = getApplicationContext();
        @SuppressWarnings("unused")
		DataManager dm = DataManager.getInstance(_context);
    }

    static public Context getContext() {
    	return _context;
    }
}