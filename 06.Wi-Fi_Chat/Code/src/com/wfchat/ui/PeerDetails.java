package com.wfchat.ui;

public class PeerDetails{
private static PeerDetails _instance = null;

private String PeerName = "null";
private String PeerMACAddr = "null";
private String PeerIPAddr = "null";
private String FriendName = "null"; // use this global variable to pass the friend device name to me

private PeerDetails() {
}

public static PeerDetails getInstance() {
    if (_instance == null)
        _instance = new PeerDetails();
    return _instance;
}


public String getVariable(String someName) {
    if(someName == "PeerName")
    {
        return PeerName;
    }
    else if(someName == "PeerMACAddr")
    {
        return PeerMACAddr;
    }
    else if(someName == "PeerIPAddr")
    {
        return PeerIPAddr;
    }
    else if(someName == "FriendName")
    {
        return FriendName;
    }
    else
    {
        return null;
    }
}

public void setVariable(String someName, String someVariable) {
    if(someName == "PeerName")
    {
        PeerName = someVariable;
    }
    else if(someName == "PeerMACAddr")
    {
        PeerMACAddr = someVariable;
    }
    else if(someName == "PeerIPAddr")
    {
        PeerIPAddr = someVariable;
    }
    else if(someName == "FriendName")
    {
        FriendName = someVariable;
    }
}
}
