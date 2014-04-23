/**
 * This is a concrete strategy which implements the BillStrategy interface.
 */
package com.alarmsystem.bill;

import com.alarmsystem.core.AlarmEvent;
import com.alarmsystem.core.GlobalSettings.SensorType;
import com.alarmsystem.core.Sensor;
import com.alarmsystem.core.SensorFactory;


public class BillStrategyDefault implements BillStrategy{
	private String txt = null;
	private double cost = -1.0;
	private Bill lastBill = null;
	private static double DOOR_SENSOR_COST_PER_MSECOND = 30000000.0 / (24*60*60*1000);
	private static double DOOR_SENSOR_COST_PER_ALARM = 1.0;
	private static double FIRE_SENSOR_COST_PER_MSECOND = 10000000.0 / (24*60*60*1000);
	private static double FIRE_SENSOR_COST_PER_ALARM = 2.0;
	
	@Override
	public String generateTxtDetails(Bill b) {
		if (lastBill == b)
			return txt;
		
		cost = 0.0;
		double tmp, sum;
		
		StringBuffer sb = new StringBuffer("==================== BILL ====================\n");
		sb.append("Start time: ").append(b.getStartTime()).append("\tEnd time: ").append(b.getEndTime()).append("\n");
		sb.append("1. DOOR SENSOR BILLING DETAILS\n");
		sb.append("1.1 Door sensor usage\n");
		double regularTimeCost = (b.getEndTime().getTime() - b.getStartTime().getTime()) * DOOR_SENSOR_COST_PER_MSECOND;
		sum = 0.0;
		for (Sensor s: SensorFactory.getInstance().getSensors(b.getUser())) {
			if (s.getSensorType() == SensorType.DOOR_SENSOR) {
				if (s.getCreateTime().after(b.getStartTime())) {
					sb.append("\t").append(s.getID()).append(" --- USING FROM ").append(s.getCreateTime());
					tmp = (b.getEndTime().getTime() - s.getCreateTime().getTime()) * DOOR_SENSOR_COST_PER_MSECOND;
				} else {
					sb.append("\t").append(s.getID()).append(" --- USING");
					tmp = regularTimeCost;
				}
				sb.append("\t\t$").append(tmp).append("\n");
				sum += tmp;
			}
		}
		sb.append("\tSUM: $").append(sum).append("\n");
		cost += sum;
		
		sb.append("1.2 Door sensor alarms cost\n");
		sum = 0.0;
		for (AlarmEvent ae: b.getEvents()) {
			if (ae.getSensor().getSensorType() == SensorType.DOOR_SENSOR) {
				sb.append("\t").append(ae.getSensor().getID()).append(" --- alert at ").append(ae.getTime());
				sb.append("\t\t$").append(DOOR_SENSOR_COST_PER_ALARM).append("\n");
				sum += DOOR_SENSOR_COST_PER_ALARM;
			}
		}
		sb.append("\tSUM: $").append(sum).append("\n");
		cost += sum;
		
		sb.append("2. FILE SENSOR BILLING DETAILS\n");
		sb.append("2.1 Fire sensor usage\n");
		regularTimeCost = (b.getEndTime().getTime() - b.getStartTime().getTime()) * FIRE_SENSOR_COST_PER_MSECOND;
		sum = 0.0;
		for (Sensor s: SensorFactory.getInstance().getSensors(b.getUser())) {
			if (s.getSensorType() == SensorType.TEMPERATURE_SENSOR) {
				if (s.getCreateTime().after(b.getStartTime())) {
					sb.append("\t").append(s.getID()).append(" --- USING FROM ").append(s.getCreateTime());
					tmp = (b.getEndTime().getTime() - s.getCreateTime().getTime()) * FIRE_SENSOR_COST_PER_MSECOND;
				} else {
					sb.append("\t").append(s.getID()).append(" --- USING");
					tmp = regularTimeCost;
				}
				sb.append("\t\t$").append(tmp).append("\n");
				sum += tmp;
			}
		}
		sb.append("\tSUM: $").append(sum).append("\n");
		cost += sum;
		
		sb.append("2.2 Fire sensor alarms cost\n");
		sum = 0.0;
		for (AlarmEvent ae: b.getEvents()) {
			if (ae.getSensor().getSensorType() == SensorType.TEMPERATURE_SENSOR) {
				sb.append("\t").append(ae.getSensor().getID()).append(" --- alert at ").append(ae.getTime());
				sb.append("\t\t$").append(FIRE_SENSOR_COST_PER_ALARM).append("\n");
				sum += FIRE_SENSOR_COST_PER_ALARM;
			}
		}
		sb.append("\tSUM: $").append(sum).append("\n");
		cost += sum;
		sb.append("Total bill: $").append(cost);
		
		
		txt = sb.toString();
		lastBill = b;
		return txt;
	}

	@Override
	public double getTotalCost(Bill b) {
		if (lastBill != b)
			generateTxtDetails(b);
		
		return cost;
	}

}
