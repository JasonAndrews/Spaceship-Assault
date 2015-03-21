package com.jasonandrews.ocja.spaceshipassault;

import java.awt.Color;
import java.awt.Graphics2D;

public class Pickup extends Sprite {

	private int pickupType;
		
	public Pickup(GamePanel gamePanel, double xPos, double yPos, int pickupType) {
		super(gamePanel, xPos, yPos, 1, 2, 180, Color.BLACK);
		this.pickupType = pickupType;
	}

	@Override
	public void update() {
		this.setYPos(this.getYPos() + this.getSpeed());
		
		if(this.getCenterPoint().getX() < 0 || this.getCenterPoint().getX() > PANEL_WIDTH || this.getCenterPoint().getY() < 0 || this.getCenterPoint().getY() > PANEL_HEIGHT - 30) this.setVisible(false);		
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(this.getSpriteImage(), (int) this.getXPos(), (int) this.getYPos(), null);
	}

	public int getPickupType() {
		return pickupType;
	}

}
