package VO;

import java.awt.*;

public class Point {
	private int startX, startY, lastX, lastY;
	private Color color;
	private String state;

	public Point(int startX, int startY, int lastX, int lastY, Color color, String state) {
		super();
		this.startX = startX;
		this.startY = startY;
		this.lastX = lastX;
		this.lastY = lastY;
		this.color = color;
		this.state = state;
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public int getLastX() {
		return lastX;
	}

	public void setLastX(int lastX) {
		this.lastX = lastX;
	}

	public int getLastY() {
		return lastY;
	}

	public void setLastY(int lastY) {
		this.lastY = lastY;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return startX + "#" + startY + "#" + lastX + "#" + lastY + "#" + color.getRed() + "#" + color.getGreen() + "#"
				+ color.getBlue() + "#" + state;
	}
}