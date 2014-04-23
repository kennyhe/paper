package com.wfchat.interfaces;

public interface Manage {
	
	public String getUsername();
	public boolean sendMessage(String username, String message);

	public String authenticateUser(String usernameText, String passwordText); 
	public void messageReceived(String message);
	public boolean isNetworkConnected();
	public boolean isUserAuthenticated();
	public String getLastRawFriendList();
	public void exit();
	public String signUpUser(String usernameText, String passwordText, String email);
	public String addNewFriendRequest(String friendUsername);
	public String sendFriendsReqsResponse(String approvedFriendNames,
			String discardedFriendNames);

	
}
