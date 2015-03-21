package com.jasonandrews.ocja.spaceshipassault;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Laser {
	
	private BufferedImage image;
	private boolean visible;
	
	private int startX;
	private int startY;
	
	private int endX;
	private int endY;
	private int calibre;
	
	private Color color;
	
	private long fireTime = 0L;
	public Laser(int calibre, int startX, int startY, int endX, int endY) {
		this.startX = startX;
		this.startY = startY;
		
		this.endX = (endX + (int) (Math.random() * 15));
		this.endY = (endY + (int) (Math.random() * 15));
		this.calibre = calibre;
		color = Color.BLUE;
		
		this.visible = true;
		
		fireTime = System.currentTimeMillis();
	}
	
	public void update() {
		if(System.currentTimeMillis() - fireTime > 30) {
			this.setVisible(false);
		}
	}
	public void draw(Graphics2D g) {
		g.setColor(this.color);
		g.setStroke(new BasicStroke(calibre));
		g.drawLine(this.startX, this.startY, this.endX, this.endY);
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public boolean isVisible() {
		return this.visible;
	}
}
