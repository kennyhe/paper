package com.alarmsystem.core;

import java.util.Enumeration;
import java.util.Hashtable;
import com.alarmsystem.core.User.UserType;

public class UserFactory {
	private Hashtable<String, User> users = new Hashtable<String, User>();
	
	// Singleton.
	static private UserFactory instance = null;
	private UserFactory () {}
	public static synchronized UserFactory getInstance() {
		if (instance == null)
			instance = new UserFactory();
		
		return instance;
	}
	
	
	/**
	 * Create a new user with the given UID, Password, Map file name, and the user type. 
	 * @param uid User ID
	 * @param pass Password
	 * @param map The Map file name
	 * @param type The user type.
	 * @return If success, return null; else, give the error message.
	 */
	public String addUser(String uid, String pass, String map, UserType type) {
		// If the UID exists, simply return error message.
		if (users.containsKey(uid)) { 
			return "User ID already exists";
		}
		
		users.put(uid, new User(uid, pass, map, type));
		return null;
	}
	
	
	/**
	 * Validate user name and password 
	 * @param uid User ID
	 * @param pass Password
	 * @return The User object if validate successful; else null.
	 */
	public User validUser(String uid, String pass) {
		User u = users.get(uid);
		if (u == null)
			return null;
		
		if (u.getUID().equals(uid) && u.getPass().equals(pass)) 
			return u;
		else
			return null;
	}
	
	/**
	 * Get a collection of all user names.
	 * @return The names of all users in a collection.
	 */
	public Enumeration<String> getAllUsers() {
		return users.keys();
	}
	
	/**
	 * Get a user object by UID.
	 * @param uid the user ID
	 * @return The User object
	 */
	public User getUser(String uid) {
		return users.get(uid);
	}
	
	
	/**
	 * Check whether a user id already exists.
	 * @param uid The user ID to be tested.
	 * @return True if the user ID already exsits.
	 */
	public boolean userExist(String uid) {
		return users.containsKey(uid);
	}

}
