package com.wfchat.data;

public class Friend {

	public Friend() {
		// Empty constructor.
	}
	
	public Friend(int uid, String name, String did, String hwid, String gender, String status, String favor, int age) {
		this.name		= name;
		this.deviceID	= did;
		this.hwID 		= hwid;
		this.status		= status;
		this.gender		= gender;
		this.age		= age;
		this.favor		= favor;
		this.fid		= uid;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	
	public String getIP() {
		return ip;
	}
	
	public void setIP(String ip) {
		this.ip = ip;
	}
	
	public String getPort() {
		return port;
	}
	
	public void setPort(String port) {
		this.port = port;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String n) {
		this.name = n;
	}
	
	public String getDeviceID() {
		return deviceID;
	}
	
	public void setDeviceID(String devID) {
		this.deviceID = devID;
	}
	
	public String getHwID() {
		return hwID;
	}
	
	public void setHwID(String hwID) {
		this.hwID = hwID;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String st) {
		this.status = st;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gd) {
		this.gender = gd;
	}
	
	public String getFavor() {
		return favor;
	}
	
	public void setFavor(String fv) {
		this.favor = fv;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public int getFid() {
		return fid;
	}
	
	public void setFid(int fid) {
		this.fid = fid;
	}
	
	
	String ip;
	String port;
	String name;
	String deviceID;
	String hwID;
	String status;
	String gender;
	int 	age;
	String favor;
	int 	fid;
	boolean valid;	// If value is false, then this object is not valid.
}
