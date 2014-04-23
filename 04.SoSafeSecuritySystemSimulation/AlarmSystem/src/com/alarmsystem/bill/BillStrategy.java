/**
 * This is the strategy interface.
 */
package com.alarmsystem.bill;


public interface BillStrategy {
	String generateTxtDetails(Bill b);
	double getTotalCost(Bill b);
}
