package com.wfchat.data;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "wfchat";
 
    // Friends table name
    private static final String TABLE_FRIENDS = "friends";
 
    // Friends Table Columns names
    private static final String KEY_ID 		= "fid";
    private static final String KEY_NAME 	= "name";
    private static final String KEY_DEV_ID 	= "deviceID";
    private static final String KEY_HW_ID	= "hwID";
    private static final String KEY_GENDER	= "gender";
    private static final String KEY_STATUS	= "status";
    private static final String KEY_FAVOR	= "favor";
    private static final String KEY_AGE		= "age";
    
 
    
    // Messages table name
    private static final String TABLE_MESSAGES = "messages";
 
    // Messages Table Columns names
    private static final String KEY_MSG_ID 	= "msgid";
    private static final String KEY_CLIENT 	= "clientid";
    private static final String KEY_ISSENT 	= "isSend";
    private static final String KEY_MESSAGE	= "message";
//    private static final String KEY_TIME	= "time";
    private static final String KEY_TIME_INT= "time_int";
    
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    
    @Override
	public void onCreate(SQLiteDatabase db) {
        String CREATE_FRIENDS_TABLE = "CREATE TABLE " + TABLE_FRIENDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DEV_ID + " TEXT," + KEY_HW_ID + " TEXT,"
                + KEY_GENDER + " TEXT," + KEY_STATUS + " TEXT,"
                + KEY_FAVOR + " TEXT," + KEY_AGE + " INTEGER)";
        db.execSQL(CREATE_FRIENDS_TABLE);		

        String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_MESSAGES + "("
                + KEY_MSG_ID + " INTEGER PRIMARY KEY," + KEY_CLIENT + " INTEGER,"
                + KEY_ISSENT + " INTEGER," + KEY_MESSAGE + " TEXT,"
                /*+ KEY_TIME + " TEXT,"*/ + KEY_TIME_INT + " INTEGER)";
        db.execSQL(CREATE_MESSAGES_TABLE);		
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);

		// Create tables again
		onCreate(db);
	}

	
	// The operations on the friend. 
	// Adding new friend
	public void addFriend(Friend friend) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    ContentValues values = new ContentValues();
	    values.put(KEY_NAME, friend.getName());
	    values.put(KEY_DEV_ID, friend.getDeviceID());
	    values.put(KEY_HW_ID, friend.getHwID());
	    values.put(KEY_STATUS, friend.getStatus());
	    values.put(KEY_GENDER, friend.getGender());
	    values.put(KEY_FAVOR, friend.getFavor());
	    values.put(KEY_AGE, friend.getAge());
	 
	    // Inserting Row
	    db.insert(TABLE_FRIENDS, null, values);
	    db.close(); // Closing database connection
	}
	 
	// Getting single friend
	public Friend getFriend(int fid) {
	    SQLiteDatabase db = this.getReadableDatabase();
		   
	    Cursor cursor = db.query(TABLE_FRIENDS, new String[] { KEY_ID, KEY_NAME, KEY_DEV_ID, 
	            KEY_HW_ID, KEY_GENDER, KEY_STATUS, KEY_FAVOR, KEY_AGE }, KEY_ID + "=?",
	            new String[] { String.valueOf(fid) }, null, null, null, null);
	    if (cursor == null) {
	    	db.close();
	    	return null;
	    }
	    
	    cursor.moveToFirst();
	    Friend f = new Friend(cursor.getInt(0), cursor.getString(1), 
	            cursor.getString(2), cursor.getString(3), cursor.getString(4),
        		cursor.getString(5), cursor.getString(6), cursor.getInt(7));
	    cursor.close();
	    db.close();
        // return friend
	    return f;
	}
	 
	// Getting friend by DEV_ID or HW_ID
	public Friend getFriend(String dev) {
	    SQLiteDatabase db = this.getReadableDatabase();
		   
	    Cursor cursor = db.query(TABLE_FRIENDS, new String[] { KEY_ID, KEY_NAME, KEY_DEV_ID, 
	            KEY_HW_ID, KEY_GENDER, KEY_STATUS, KEY_FAVOR, KEY_AGE }, KEY_DEV_ID + "=?",
	            new String[] { dev }, null, null, null, null);
	    if (cursor == null)
	    	cursor = db.query(TABLE_FRIENDS, new String[] { KEY_ID, KEY_NAME, KEY_DEV_ID, 
		            KEY_HW_ID, KEY_GENDER, KEY_STATUS, KEY_FAVOR, KEY_AGE }, KEY_HW_ID + "=?",
		            new String[] { dev }, null, null, null, null);
	    if (cursor == null) {
	    	db.close();
	    	return null;
	    }
	    
	    cursor.moveToFirst();
	    Friend f = new Friend(cursor.getInt(0), cursor.getString(1), 
	            cursor.getString(2), cursor.getString(3), cursor.getString(4),
       		cursor.getString(5), cursor.getString(6), cursor.getInt(7));
	    cursor.close();
	    db.close();
       // return friend
	    return f;
	}
	 
	// Getting All Friends
	public ArrayList<Friend> getAllFriends() {
	    ArrayList<Friend> friendList = new ArrayList<Friend>();
	    // Select All Query
	    String selectQuery = "SELECT " + KEY_ID + "," + KEY_NAME + "," + KEY_DEV_ID + "," + 
	            						KEY_HW_ID + "," + KEY_GENDER + "," + KEY_STATUS + "," +
	            						KEY_FAVOR + "," + KEY_AGE +
	    		            " FROM " + TABLE_FRIENDS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	    	    Friend f = new Friend(cursor.getInt(0), cursor.getString(1), 
	    	            cursor.getString(2), cursor.getString(3), cursor.getString(4),
	            		cursor.getString(5), cursor.getString(6), cursor.getInt(7));
	            // Adding message to list
	            friendList.add(f);
	        } while (cursor.moveToNext());
	    }	    
	    cursor.close();
	    db.close();
	 
	    // return messages list
	    return friendList;
	}
	 
	// Getting friends Count
	public int getFriendsCount() {
	    // Select All Query
	    String selectQuery = "SELECT count(*) FROM " + TABLE_FRIENDS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    cursor.moveToFirst();
	    int count = cursor.getInt(0);
	    cursor.close();
	    db.close();
	    
	    return count;
	}
	
	// Updating single friend
	public int updateFriend(Friend f) {
		return 0;
	}
	 
	// Deleting single friend
	public void deleteFriend(Friend f) {
		
	}


	
	
	
	// The operations on the message. 
	// Adding new message
	public void addMessage(Message message) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    ContentValues values = new ContentValues();
	    values.put(KEY_CLIENT, message.getClientID());
	    values.put(KEY_ISSENT, message.getSend());
	    values.put(KEY_MESSAGE, message.getMessage());
//	    values.put(KEY_TIME, message.getTime().toString());
	    values.put(KEY_TIME_INT, message.getTime().getTime());
	 
	    // Inserting Row
	    db.insert(TABLE_MESSAGES, null, values);
	    db.close(); // Closing database connection
	}
	 
	// Getting single message
	public Message getMessage(int id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	   
	    Cursor cursor = db.query(TABLE_MESSAGES, new String[] { KEY_MSG_ID,
	    		KEY_CLIENT, KEY_ISSENT, KEY_MESSAGE/*, KEY_TIME*/, KEY_TIME_INT}, KEY_MSG_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor == null)
	    	return null;
	    
	    cursor.moveToFirst();
	    Message m = new Message(cursor.getInt(0), cursor.getInt(1), 
	            cursor.getInt(2), cursor.getString(3), /*cursor.getString(4),*/ cursor.getLong(4));
        // return message
	    return m;
	}
	 
	// Getting All Messages by a friend
	public ArrayList<Message> getAllMessages(int fid) {
	    ArrayList<Message> msgList = new ArrayList<Message>();
	    // Select All Query
	    String selectQuery = "SELECT " + KEY_MSG_ID + "," + KEY_CLIENT + "," + KEY_ISSENT + ","
	    		                       + KEY_MESSAGE + /*"," + KEY_TIME +*/ "," + KEY_TIME_INT +
	    		             "FROM " + TABLE_MESSAGES + 
	    				    " WHERE " + KEY_CLIENT + "=" + fid +
	    				    " ORDER BY " + KEY_TIME_INT;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	    	    Message m = new Message(cursor.getInt(0), cursor.getInt(1), 
	    	            cursor.getInt(2), cursor.getString(3), cursor.getLong(4));
	            // Adding message to list
	            msgList.add(m);
	        } while (cursor.moveToNext());
	    }
	    
	    // return messages list
	    return msgList;
	}
	 
	// Getting latest 10 Messages by a friend
	public ArrayList<Message> getLast10Messages(int fid) {
	    ArrayList<Message> msgList = new ArrayList<Message>();
	    // Select All Query, but in the reversed order. And only pick the last 10.
	    String selectQuery = "SELECT " + KEY_MSG_ID + "," + KEY_CLIENT + "," + KEY_ISSENT + ","
	    		                       + KEY_MESSAGE + "," + KEY_TIME_INT +
	    		             "FROM " + TABLE_MESSAGES + 
	    				    " WHERE " + KEY_CLIENT + "=" + fid +
	    				    " ORDER BY " + KEY_TIME_INT + " DESC";
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    
	    int count = 0;
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	    	    Message m = new Message(cursor.getInt(0), cursor.getInt(1), 
	    	            cursor.getInt(2), cursor.getString(3), cursor.getLong(4));
	            // Adding message to the head of the list
	            msgList.add(0, m);
	            count++;
	        } while (cursor.moveToNext() && (count < 10));
	    }	 
	    // return messages list
	    return msgList;
	}
	 
	// Getting messages Count
	public int getMessagesCount() {
	    // Select All Query
	    String selectQuery = "SELECT count(*) FROM " + TABLE_MESSAGES;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    cursor.moveToFirst();
	    return cursor.getInt(0);
	}
	
	// Delete all messages of a friend
	public void deleteAllMessages(int fid) {

	}
	 
	// Deleting single message
	public void deleteMessage(Message f) {
		
	}
}
