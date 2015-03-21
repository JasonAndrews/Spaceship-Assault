package com.jasonandrews.ocja.spaceshipassault;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bullet extends Sprite {

	private static int nextBulletId = 0;
	private static BufferedImage bulletSprite;
	
	static {
		try {
			bulletSprite = ImageIO.read(new File("images/bullet.png"));
		} catch(IOException ioe) {
			System.out.println("Bullet: Could not find image - bullet.png");
		}
	}
	
	private double radius;
	private boolean playerBullet;
	private int id;
	
	public Bullet(GamePanel gamePanel, double xPos, double yPos, int direction, double speed, double angle, boolean playerBullet) {
		super(gamePanel, xPos, yPos, direction, 6.5, angle, Color.GRAY);
		
		this.radius = 5;
		this.setPlayerBullet(playerBullet);
		this.setId(++nextBulletId);
		this.setSpriteImage(bulletSprite);
	}

	@Override
	public void update() {
		
		double bulletAngle = this.getAngle();
		
		this.setDX(this.getSpeed() * Math.cos(Math.toRadians(bulletAngle-90)));
		this.setDY(this.getSpeed() * Math.sin(Math.toRadians(bulletAngle-90)));
		
		this.setXPos(this.getXPos() + this.getDX());
		this.setYPos(this.getYPos() + this.getDY());
		
		double tempXPos = 0.0D, tempYPos = 0.0D;
		tempXPos = this.getXPos();
		tempYPos = this.getYPos();
		if(tempXPos < 0 || tempXPos > PANEL_WIDTH || tempYPos < 0 || tempYPos > PANEL_HEIGHT) this.setVisible(false);
		this.setDX(0.0);
		this.setDY(0.0);
	}

	@Override
	public void draw(Graphics2D g) {
		if(!this.isVisible()) return;
		
		
		double rotationRequired = Math.toRadians(this.getAngle()); //= Math.toRadians(45);
		this.setAngle(Math.toDegrees(rotationRequired));
		
		double locationX = this.getSpriteImage().getWidth() / 2;
		double locationY = this.getSpriteImage().getHeight() / 2;
		
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		
		//g.setColor(new Color(128, 128, 128));
		//g.fillRect((int)this.getXPos(), (int) this.getYPos(), 5, 10);
		
		g.drawImage(op.filter(this.getSpriteImage(), null), (int) this.getXPos() -15, (int) this.getYPos()-15, null);
		//g.fillOval((int) (this.getXPos() - radius), (int) (this.getYPos() - radius), (int) (2 * this.radius), (int) (2 * this.radius));
		//g.setColor(Color.GRAY);
		//g.setStroke(new BasicStroke(1));
		//g.drawOval((int) (this.getXPos() - radius), (int) (this.getYPos() - radius), (int) (2 * this.radius), (int) (2 * this.radius));
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isPlayerBullet() {
		return playerBullet;
	}

	public void setPlayerBullet(boolean playerBullet) {
		this.playerBullet = playerBullet;
	}

}
