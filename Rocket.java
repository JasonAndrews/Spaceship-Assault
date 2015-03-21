package com.jasonandrews.ocja.spaceshipassault;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Rocket extends Sprite {

	private static BufferedImage missileImage;
	private double targetX;
	private double targetY;
	
	public Rocket(GamePanel gamePanel, double xPos, double yPos, double targetX, double targetY, double angle) {
		super(gamePanel, xPos, yPos, 1, 5, angle, Color.YELLOW);
		this.targetX = targetX;
		this.targetY = targetY;
		try {
			missileImage = ImageIO.read(new File("images/missile.png")); 
			this.setSpriteImage(missileImage);
		} catch ( IOException ioe) {
			System.out.println("Could not find image - missile.png");
		}
	}

	@Override
	public void update() {
		double missileAngle = this.getAngle();
		//if(angle >= 0.0 && angle <= 5.0) { this.setDX(this.getSpeed()); this.setDY(this.getSpeed()); }
		//else if(angle > 0.0 && angle <= 25.0) { this.setDX(2.0); this.setDY(-4.0); }
		
		
		this.setDX(this.getSpeed() * Math.cos(Math.toRadians(missileAngle-90)));
		this.setDY(this.getSpeed() * Math.sin(Math.toRadians(missileAngle-90)));
		
		this.setXPos(this.getXPos() + this.getDX());
		this.setYPos(this.getYPos() + this.getDY());
		
		double distanceOne = 0.0D, distanceTwo = 0.0D;
		
		distanceOne = (this.getXPos() + (this.getSpriteImage().getWidth()/2)) - this.targetX;//(this.getXPos() + this.getDX() - (this.getGamePanel().getMousePosition()).getX());
		distanceTwo = (this.getYPos() + (this.getSpriteImage().getHeight()/2)) - this.targetY;//(this.getYPos() + this.getDY() - (this.getGamePanel().getMousePosition()).getY());
		
		double finalDistance = Math.sqrt(distanceOne * distanceOne + distanceTwo * distanceTwo);
		//System.out.println("Final Distance from target: " + finalDistance);
		if(finalDistance < 36) {
			this.getGamePanel().createExplosion(targetX,targetY, true);
			this.setVisible(false);
		}		
		this.setXPos(this.getXPos() + this.getDX());
		this.setYPos(this.getYPos() + this.getDY());
		
		double tempXPos = 0.0D, tempYPos = 0.0D;
		tempXPos = this.getXPos();
		tempYPos = this.getYPos();
		if(tempXPos < 0 || tempXPos > MainMenuPanel.PANEL_WIDTH || tempYPos < 0 || tempYPos > MainMenuPanel.PANEL_HEIGHT) this.setVisible(false);
		this.setDX(0.0);
		this.setDY(0.0);

	}

	@Override
	public void draw(Graphics2D g) {
		

		double rotationRequired = Math.toRadians(this.getAngle()); //= Math.toRadians(45);
		this.setAngle(Math.toDegrees(rotationRequired));
	
		
		double locationX = this.getSpriteImage().getWidth() / 2;
		double locationY = this.getSpriteImage().getHeight() / 2;
		
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		if(this.getSpriteImage() != null) {			
			g.drawImage(op.filter(this.getSpriteImage(), null), (int) this.getXPos() -15, (int) this.getYPos()-15, null);
			//g.setColor(Color.BLUE);
			//g.fillOval((int) (this.getXPos() + this.getSpriteImage().getWidth()/2 -3), (int) (this.getYPos() + this.getSpriteImage().getHeight()/2 - 3), 2 * 3, 2 * 3);
			//g.setColor(Color.WHITE);
			//g.fillOval((int) (this.getXPos() - 2), (int) (this.getYPos() - 2), 2 * 2, 2 * 2);
			//g.setColor(Color.GREEN);
			//g.fillOval((int) (this.targetX - 2), (int) (this.targetY - 2), 2 * 2, 2 * 2);
		} 
	}

}
