package com.alarmsystem.core;

import java.util.Calendar;
import java.util.Date;

public class User {
	public enum UserType {
		NORMAL,
		PREMIUM,
		VIP,
	};
	
	private String uid;
	private String pass;
	private String mapFileName;
	private UserType type;
	private Date createTime;

	public User(String u, String p, String m, UserType t) {
		this.uid = u;
		this.pass = p;
		this.mapFileName = m;
		this.type = t;
		this.createTime = Calendar.getInstance().getTime();
	}
	
	public String getUID() {
		return uid;
	}
	
	public String getPass() {
		return pass;
	}
	
	public String getMapFilename() {
		return mapFileName;
	}
	
	public UserType getType() {
		return type;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
}
