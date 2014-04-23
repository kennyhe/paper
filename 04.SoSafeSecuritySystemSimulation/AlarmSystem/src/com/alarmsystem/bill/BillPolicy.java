package com.alarmsystem.bill;

import com.alarmsystem.core.User;

public class BillPolicy {
	// Singleton
	private static BillPolicy billPolicy = new BillPolicy();
	
	// Just for preventing the public construction of Singleton
	private BillPolicy() {
	}
	
	public BillPolicy getInstance () {
		return billPolicy;
	}
	
	public static BillStrategy getStrategy(User user) {
		switch (user.getType()) {
			case NORMAL: return new BillStrategyDefault();
			default:  return new BillStrategyDefault();
		}
	}
}
