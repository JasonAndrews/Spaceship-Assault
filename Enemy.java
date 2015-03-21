package com.jasonandrews.ocja.spaceshipassault;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Enemy extends Sprite {

	private static BufferedImage enemyImage;
	static {
		try {
			enemyImage = ImageIO.read(new File("images/enemy_space_ship.png"));
		} catch (IOException ioe) {
			System.out.println("Could not find image - enemy_space_ship.png");
		}
	}
	
	private int health;
	private double targetX;
	private double targetY;
	private long bulletFireTime = 0L;
	private long positionSwitchTime = 0L;
	private int positionSwitchDelay;
	private boolean vulnerable;
	
	public Enemy(GamePanel gamePanel, double xPos, double yPos, double speed, double angle) {
		super(gamePanel, xPos, -200, 0, speed, 180.0, Color.BLACK); //Replaced Math.random() < 0.5 ? (Math.random() * 180) : (Math.random() * -180) with 180.0 (for the angle, after speed) as I want them to go directly downwards first..
		this.setHealth(30);
		this.setSpriteImage(enemyImage);
		//this.assignNewTargetPosition(false);
		this.targetX = xPos;
		this.targetY = -50;
		this.positionSwitchDelay = (int) (Math.random() * 10000) + 1000;
		//this.setTargetX(this.getGamePanel().getPlayer().getXPos());
		//this.setTargetY(this.getGamePanel().getPlayer().getYPos());
		this.bulletFireTime = System.currentTimeMillis() + ((long) Math.random() * 10000);
		this.setVulnerable(false);
	}

	@Override
	public void update() {
		double enemyAngle = this.getAngle();		
		this.setDX(this.getSpeed() * Math.cos(Math.toRadians(enemyAngle-90)));
		this.setDY(this.getSpeed() * Math.sin(Math.toRadians(enemyAngle-90)));
		
		//System.out.println("XPOS: " + this.getXPos() + " | YPOS: " + this.getYPos());
		
		double tempXPos = 0.0D, tempYPos = 0.0D;
		tempXPos = this.getXPos() + this.getDX();
		tempYPos = this.getYPos() + this.getDY();
		
		this.setDX(0.0);
		this.setDY(0.0);
		
		if(this.isVulnerable() && /*GamePanel.getDifficulty() == GAME_DIFFICULTY_NORMAL && */System.currentTimeMillis() - this.positionSwitchTime > this.positionSwitchDelay) this.assignNewTargetPosition(false);
		
		if(this.getHealth() <= 0) {
			this.getGamePanel().createExplosion(this.getXPos(), this.getYPos(), false);
			this.setVisible(false);
			return;
		}
		//System.out.println("temPX " + tempXPos + " tempy: " + tempYPos);
		if(tempXPos < 0 || tempXPos > PANEL_WIDTH || tempYPos < 0 || tempYPos > (PANEL_HEIGHT - 60)) {
			if(this.isVulnerable()) assignNewTargetPosition(true);
		}
		
		if(this.isVisible()) {
		
			this.setXPos(tempXPos);
			this.setYPos(tempYPos);
			if(tempXPos - 30 < 0 || tempXPos + 15 > PANEL_WIDTH || tempYPos - 10 < 0 || tempYPos + 100 > PANEL_HEIGHT) {
				
				if(this.isVulnerable()){
					//this.changeDirection();
					this.assignNewTargetPosition(true);
				}
			}
		}
		if(System.currentTimeMillis() - bulletFireTime > 5000) {
			this.getGamePanel().fireBullet(this.getFireXPos(), this.getFireYPos(), this.getAngle(), false);
			this.bulletFireTime = System.currentTimeMillis();
		}
		
		if(!this.isVulnerable() && this.getYPos() > 25) {
			System.out.println("Now vulnerable!");
			this.setVulnerable(true);
			this.assignNewTargetPosition(false);
		}
		
	}

	@Override
	public void draw(Graphics2D g) {
		
		if(this.isVisible()) {
			this.setSpriteImage(enemyImage);
			double rotationRequired = Math.toRadians(this.getAngle()); 
			this.setAngle(Math.toDegrees(rotationRequired));
			
			double locationX = this.getSpriteImage().getWidth() / 2;
			double locationY = this.getSpriteImage().getHeight() / 2;
			
			AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

			if(this.getSpriteImage() != null) {			
				g.drawImage(op.filter(this.getSpriteImage(), null), (int) this.getXPos(), (int) this.getYPos(), null);
				//g.setColor(Color.WHITE);
				//g.fillOval((int) (this.getXPos() - 3), (int) (this.getYPos() - 3), 2 * 3, 2 * 3);
				//g.setColor(Color.BLUE);
				//g.fillOval(this.getFireXPos() - 5, this.getFireYPos() - 5, 2 * 5, 2 * 5);
			} 
		}

	}

	//A method to completely flip the enemy so that it goes the opposite way.
	private void flipDirection() { //Currently unused.
		//assignNewTarget();
		/*
		//-90
		double finalAngle = 0.0D, currentAngle = this.getAngle();
		if(currentAngle <= 0) {
			finalAngle = Math.random() * 180;
		} else {
			finalAngle = Math.random() * -180;
		}
		
		this.setAngle(finalAngle);
		
		//while ((finalAngle )
			*/
	}
	
	//A method to change the direction of the enemy.
	private void changeDirection() { //Currently unused.
		
		/*
		double newAngle = 0.0D;
		for(;;) {
			//newAngle = this.getAngle() < 0 ? Math.random() * 90 : Math.random() * -90;
			//System.out.println();
			if(this.getAngle() < 0) {
				//System.out.println("1 - " + (this.getAngle() - newAngle));
				newAngle = Math.random() * 180;
				if(newAngle - this.getAngle() < -50) continue;
			} else {
				//System.out.println("2 - " + (newAngle - this.getAngle()));
				newAngle = Math.random() * -180;
				if(newAngle - this.getAngle() > -40) continue;
			}			
			break;
		}
		this.setAngle(newAngle);
		*/
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public double getTargetX() {
		return targetX;
	}

	public void setTargetX(double targetX) {
		this.targetX = targetX;
	}

	public double getTargetY() {
		return targetY;
	}

	public void setTargetY(double targetY) {
		this.targetY = targetY;
	}
	
	public void assignNewTargetPosition(boolean flipDirection) {
		
		double angleInDegrees = this.getAngle();
		double nextTargetX = 0.0D, nextTargetY = 0.0D;
		if(flipDirection){
			if(this.getAngle() < 0.0D) {
				nextTargetX = (Math.random() * 1000);
				nextTargetY = (Math.random() * 1000);
			} else {
				nextTargetX = (Math.random() * -1000);
				nextTargetY = (Math.random() * -1000);
			}
			//return;
		} else if(Math.random() < 0.50D) {
			nextTargetX = (Math.random() * 1000);
			nextTargetY = (Math.random() * 1000);
		} else {
			nextTargetX = (Math.random() * -1000);
			nextTargetY = (Math.random() * -1000);
		}
		
		//nextTargetX = (Math.random() * 1000);
		//nextTargetY = (Math.random() * 1000);
	
		this.targetX = nextTargetX;
		this.targetY = nextTargetY;
		angleInDegrees = Math.toDegrees(Math.atan2(this.targetX - (this.getXPos()/* + 25*/), (this.getYPos()/* + 25*/) - this.targetY));
		this.setAngle(angleInDegrees);
		if(!flipDirection) positionSwitchTime = System.currentTimeMillis();
	}

	public boolean isVulnerable() {
		return vulnerable;
	}

	public void setVulnerable(boolean vulnerable) {
		this.vulnerable = vulnerable;
	}
	
}
