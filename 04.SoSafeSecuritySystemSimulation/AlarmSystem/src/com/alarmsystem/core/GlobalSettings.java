/**
 * This file defines the global constants used by other classes. 
 */
package com.alarmsystem.core;

import java.util.HashMap;
import java.util.Map;

import com.alarmsystem.bill.BillStrategy;
import com.alarmsystem.bill.BillStrategyDefault;

public class GlobalSettings {
	public enum SensorType {
		TEMPERATURE_SENSOR,
		DOOR_SENSOR,
	};
	
	public static final int ICON_WIDTH = 32;
	public static final int ICON_HEIGHT = 32;

	
	/**
	 * Predefine the possible bill policy classes, for the Bill Strategy.
	 * Note: Null Object pattern is not suitable here because we do need a default bill policy for the others.
	 */
	@SuppressWarnings("rawtypes")
	static private Map<String, Class> StrategyClasses = new HashMap<String, Class>();
	// If there are new policies, add them into the map so the system can automatically load them when start.
	static {
		StrategyClasses.put("Default Strategy", BillStrategyDefault.class);
		
	};
	static public BillStrategy getStrategy(String strategyName) {
		BillStrategy bp = null;
		try {
			bp = (BillStrategy)(StrategyClasses.get(strategyName).newInstance());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (null == bp)
			bp = new BillStrategyDefault();
		return bp;
	}
}
