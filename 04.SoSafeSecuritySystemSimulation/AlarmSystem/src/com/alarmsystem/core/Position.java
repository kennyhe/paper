/**
 * The position for the alarm sensors.
 */
package com.alarmsystem.core;

public class Position {
	int x = 0, y = 0;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return new StringBuffer("Position: (x:").append(x).append(", ").append(y).append(")").toString();
	}
	
}
