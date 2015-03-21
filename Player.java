package com.jasonandrews.ocja.spaceshipassault;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;

public class Player extends Sprite {
	
	private static final int PLAYER_SPRITE_IMAGE_WIDTH = 60;
	private static final int PLAYER_SPRITE_IMAGE_HEIGHT = 42;
	private static final int PLAYER_SPRITE_IMAGE_WIDTH_FIX = 13;
	private static final int PLAYER_DEFAULT_HEALTH = 50;
	private static final int PLAYER_DEFAULT_SHIELD_HEALTH = 20;
	private static final int REMOVE_FIRE_FROM_GUN_TIME = 200; //After 200 milliseconds, the 'fire/smoke' from firing rockets/bullets will disappear.
	private static final int PLAYER_DEFAULT_SPRINT_TIME = 5 * 25; //5 for seconds and 25 for calls per second
	private static final double PLAYER_DEFAULT_SPRINT_REGEN_SPEED = 0.3D; //How fast their sprint will regenerate when they arent sprinting.
	private static final double PLAYER_DEFAULT_SPRINT_DECREASE_SPEED = 1.0D; //How fast their sprint will regenerate when they arent sprinting.
	
	private static final int PLAYER_MAXIMUM_LIVES = 5;
	private static final int PLAYER_MAXIMUM_HEALTH = 100;
	private static final int PLAYER_MAXIMUM_SHIELD_HEALTH = 50;
	private static final int PLAYER_MAXIMUM_BULLETS = 999;
	private static final int PLAYER_MAXIMUM_ROCKETS = 4;
	
	private static BufferedImage playerSpriteSheet = null;
	
	private double mouseXPos;
	private double mouseYPos;
	private Point mousePointer;
	private int health;
	private int shieldHealth;
	private int lives;
	private int rockets;
	private int bullets;
	
	private boolean flinched;
	private long flinchTimer;
	private double sprintLeft;
	private boolean sprinting;
	
	private boolean firingRocket;
	private long firedRocketTime;
	private boolean firingBullet;
	private long firedBulletTime;
	
	
		
	private static BufferedImage playerImage;// = ImageIO.read(new File("images/playerUp.png"));
	
	static {
		
		try {
			playerSpriteSheet = ImageIO.read(new File("images/player_space_ship_ss.png"));
		} catch (IOException ioe) {
			System.out.println("Could not find image - player_space_ship_ss.png");
		}
		
		try {
			playerImage = ImageIO.read(new File("images/player_space_ship.png"));
		} catch (IOException ioe) {
			System.out.println("Could not find image - player_space_ship.png");
		}
		
	}
	public Player(GamePanel gamePanel) {
		super(gamePanel, PANEL_WIDTH / 2, PANEL_HEIGHT - 90, 1, 3, 0.0, null);
		this.setLives(5);
		this.setHealth(PLAYER_DEFAULT_HEALTH);
		this.setShieldHealth(PLAYER_DEFAULT_SHIELD_HEALTH);
		this.setRockets(4);
		this.setBullets(120);
		this.setSprinting(false);
		this.setSprintLeft(PLAYER_DEFAULT_SPRINT_TIME);
	}
	
	public void update() {
		if(isGoingUp()) {
			this.setDY(-this.getSpeed());
		}
		if(isGoingDown()) {
			this.setDY(this.getSpeed());
		}
		
		if(isGoingRight()) {
			this.setDX(this.getSpeed());
		}
		if(isGoingLeft()) {
			this.setDX(-this.getSpeed());
		}
		double x = this.getXPos();
		double y = this.getYPos();
		
		
		this.setXPos(x += this.getDX());
		this.setYPos(y += this.getDY());
		
		if(this.isMoving()) {
			
			if(this.firingBullet || (this.getFiredBulletTime() != 0 && System.currentTimeMillis() - this.getFiredBulletTime() < REMOVE_FIRE_FROM_GUN_TIME)) {
				this.setSpriteImage(playerImage = playerSpriteSheet.getSubimage((PLAYER_SPRITE_IMAGE_WIDTH * 1) - (PLAYER_SPRITE_IMAGE_WIDTH_FIX * 1), PLAYER_SPRITE_IMAGE_HEIGHT * 1, PLAYER_SPRITE_IMAGE_WIDTH, PLAYER_SPRITE_IMAGE_HEIGHT));
			}
			else if(this.firingRocket || (this.getFiredRocketTime() != 0 && System.currentTimeMillis() - this.getFiredRocketTime() < REMOVE_FIRE_FROM_GUN_TIME)) {
				this.setSpriteImage(playerImage = playerSpriteSheet.getSubimage((PLAYER_SPRITE_IMAGE_WIDTH * 2) - (PLAYER_SPRITE_IMAGE_WIDTH_FIX * 2), PLAYER_SPRITE_IMAGE_HEIGHT * 1, PLAYER_SPRITE_IMAGE_WIDTH, PLAYER_SPRITE_IMAGE_HEIGHT));				
			}
			else {
				this.setSpriteImage(playerImage = playerSpriteSheet.getSubimage(PLAYER_SPRITE_IMAGE_WIDTH * 0, PLAYER_SPRITE_IMAGE_HEIGHT * 1, PLAYER_SPRITE_IMAGE_WIDTH, PLAYER_SPRITE_IMAGE_HEIGHT));
			}
			
		} else {
			if(this.firingBullet || (this.getFiredBulletTime() != 0 && System.currentTimeMillis() - this.getFiredBulletTime() < REMOVE_FIRE_FROM_GUN_TIME)) {
					this.setSpriteImage(playerImage = playerSpriteSheet.getSubimage((PLAYER_SPRITE_IMAGE_WIDTH * 1) - (PLAYER_SPRITE_IMAGE_WIDTH_FIX * 1), PLAYER_SPRITE_IMAGE_HEIGHT * 0, PLAYER_SPRITE_IMAGE_WIDTH, PLAYER_SPRITE_IMAGE_HEIGHT));
			}
			else if(this.firingRocket || (this.getFiredRocketTime() != 0 && System.currentTimeMillis() - this.getFiredRocketTime() < REMOVE_FIRE_FROM_GUN_TIME)) {
				this.setSpriteImage(playerImage = playerSpriteSheet.getSubimage((PLAYER_SPRITE_IMAGE_WIDTH * 2) - (PLAYER_SPRITE_IMAGE_WIDTH_FIX * 2), PLAYER_SPRITE_IMAGE_HEIGHT * 0, PLAYER_SPRITE_IMAGE_WIDTH, PLAYER_SPRITE_IMAGE_HEIGHT));
			}
			else {
				this.setSpriteImage(playerImage = playerSpriteSheet.getSubimage(PLAYER_SPRITE_IMAGE_WIDTH * 0, PLAYER_SPRITE_IMAGE_HEIGHT * 0, PLAYER_SPRITE_IMAGE_WIDTH, PLAYER_SPRITE_IMAGE_HEIGHT));
			}
		}
		
		this.setDX(0);
		this.setDY(0);
		
		if(this.getHealth() < 1) {
			this.setHealth(PLAYER_DEFAULT_HEALTH);
			this.lives--;
		}
		if(this.isSprinting()) this.setSprintLeft(this.getSprintLeft() - PLAYER_DEFAULT_SPRINT_DECREASE_SPEED);
		else this.setSprintLeft(this.getSprintLeft() + PLAYER_DEFAULT_SPRINT_REGEN_SPEED);
		
		if(this.getSprintLeft() == 0) this.setSprinting(false);
		
	}
	
	public void draw(Graphics2D g) {
		//System.out.println("Players Direction: " + this.direction);
		
		
		// Rotation information
		this.setSpriteImage(playerImage);
		double rotationRequired = Math.toRadians(this.assignNewTarget()); 
		this.setAngle(Math.toDegrees(rotationRequired));
		
		// Drawing the rotated image at the required drawing locations
		double locationX = this.getSpriteImage().getWidth() / 2;
		double locationY = this.getSpriteImage().getHeight() / 2;
		
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		//System.out.println("Should draw at: " + (int) xPos + " and " + (int) yPos);
		if(this.getSpriteImage() != null) {			
			g.drawImage(op.filter(this.getSpriteImage(), null), (int) this.getXPos(), (int) this.getYPos(), null);
			//g.setColor(Color.WHITE);
			//g.fillOval((int) (this.getXPos() - 3), (int) (this.getYPos() - 3), 2 * 3, 2 * 3);
			//g.setColor(Color.BLUE);
			//g.fillOval(this.getFireXPos() - 5, this.getFireYPos() - 5, 2 * 5, 2 * 5);
			
			if(this.getShieldHealth() > 0) {
				//g.setStroke(new BasicStroke(1));
				g.setColor(Color.BLUE);
				g.drawOval(this.getFireXPos() - 30, this.getFireYPos() - 30, 2 * 30, 2 * 30);
				if(this.getShieldHealth() == PLAYER_MAXIMUM_SHIELD_HEALTH) g.drawOval(this.getFireXPos() - 25, this.getFireYPos() - 25, 2 * 25, 2 * 25);
			}
		} 
	}
	
	private double calculateAngle() {
		double xPos = this.getXPos();
		double yPos = this.getYPos();
		double spriteWidth = 0.0D;
		double spriteHeight = 0.0D;
		BufferedImage spriteImage = null;
		try {
			this.setSpriteImage(ImageIO.read(new File("images/playerUp.png")));
		} catch(IOException ex) {
			
		}
		mousePointer = this.getGamePanel().getMousePosition();
		if(null != mousePointer) {
			mouseXPos = mousePointer.getX();
			mouseYPos = mousePointer.getY();
			
			spriteImage = this.getSpriteImage();
			spriteWidth = spriteImage.getWidth();
			spriteHeight = spriteImage.getHeight();

			if((mouseXPos - xPos > spriteWidth) && (mouseXPos > xPos && mouseYPos < yPos)) {
				//Top Right.
				this.setDirection(SPRITE_DIRECTION_UP);//direction
				return 45.0D;
			} else if((xPos - mouseXPos > spriteWidth) && (mouseXPos < xPos && mouseYPos < yPos)) {
				//Top Left.
				this.setDirection(SPRITE_DIRECTION_UP);//direction
				return -45.0D;
			} else if((mouseYPos - yPos > spriteHeight) && (mouseXPos - xPos >= spriteWidth) && (mouseXPos > xPos && mouseYPos > yPos)) {
				//Bottom Right.
				this.setDirection(SPRITE_DIRECTION_DOWN);//direction
				return 135.0D;
			} else if((yPos - mouseYPos < spriteHeight) && (xPos - mouseXPos >= spriteWidth) && (mouseXPos < xPos && mouseYPos > yPos)) {
				//Bottom Left.
				if(mouseYPos - yPos < spriteHeight) {
					this.setDirection(SPRITE_DIRECTION_LEFT);//direction
					return -90.0D;
				}
				else {
					this.setDirection(SPRITE_DIRECTION_DOWN);//direction
					return -135.0D;
				}
			} else {
				if(mouseXPos - xPos < spriteWidth && (mouseYPos < yPos)) {
					this.setDirection(SPRITE_DIRECTION_UP);//direction
					return 0.0D; //Up
				}
				else if(mouseXPos - xPos < spriteWidth && (mouseYPos > yPos)) {
					this.setDirection(SPRITE_DIRECTION_DOWN);//direction
					return 180.0D; //Down
				}
				else if(mouseYPos - yPos < spriteHeight && (mouseXPos < xPos)) {
					this.setDirection(SPRITE_DIRECTION_LEFT);
					return -90.0D; //Left
				}
				else if(mouseYPos - yPos < spriteHeight && (mouseXPos > xPos)) {
					this.setDirection(SPRITE_DIRECTION_RIGHT);
					return 90.0D; //Right
				}
				else return this.getAngle();
			}
			
		} 
		return this.getAngle();
	}
	
	public int getLives() {
		return lives;
	}
	public void setLives(int lives) {
		if(lives > PLAYER_MAXIMUM_LIVES) lives = PLAYER_MAXIMUM_LIVES;
		if(lives < 0) lives = 0;
		this.lives = lives;
	}
	public int getRockets() {
		return rockets;
	}
	public void setRockets(int rockets) {
		if(rockets > PLAYER_MAXIMUM_ROCKETS) rockets = PLAYER_MAXIMUM_ROCKETS;
		if(rockets < 0) rockets = 0;
		this.rockets = rockets;
	}
	public int getBullets() {
		return bullets;
	}
	public void setBullets(int bullets) {
		if(bullets > 999) bullets = PLAYER_MAXIMUM_BULLETS;
		if(bullets < 0) bullets = 0;
		this.bullets = bullets;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		if(health > PLAYER_MAXIMUM_HEALTH) health = PLAYER_MAXIMUM_HEALTH;
		if(health < 0) health = 0;
		this.health = health;
	}
	public int getShieldHealth() {
		return shieldHealth;
	}
	public void setShieldHealth(int shieldHealth) {
		if(shieldHealth > PLAYER_MAXIMUM_SHIELD_HEALTH) shieldHealth = PLAYER_MAXIMUM_SHIELD_HEALTH;
		if(shieldHealth < 0) shieldHealth = 0;
		this.shieldHealth = shieldHealth;
	}
	public long getFlinchTimer() {
		return flinchTimer;
	}
	private void setFlinchTimer(long flinchTimer) {
		this.flinchTimer = flinchTimer;
	}
	public boolean isFlinched() {
		return flinched;
	}
	public void setFlinched(boolean flinched) {
		this.flinched = flinched;
		this.setFlinchTimer(System.currentTimeMillis());
		this.setShieldHealth(0);
		this.setHealth(PLAYER_DEFAULT_HEALTH);
	}
	public double getSprintLeft() {
		return sprintLeft;
	}
	public void setSprintLeft(double sprintLeft) {
		if(sprintLeft > PLAYER_DEFAULT_SPRINT_TIME) sprintLeft = PLAYER_DEFAULT_SPRINT_TIME;
		if(sprintLeft < 0.0D) sprintLeft = 0.0D;
		this.sprintLeft = sprintLeft;
	}
	public boolean isSprinting() {
		return sprinting;
	}
	public void setSprinting(boolean sprinting) {
		if(this.getSprintLeft() < 0) return;
		
		this.sprinting = sprinting;
		if(sprinting) this.setSpeed(5);
		else this.setSpeed(3);
	}
	
	public void setFiredRocket(boolean firingRocket) {
		this.firingRocket = firingRocket;
		if(firingRocket) this.firedRocketTime = System.currentTimeMillis();
	}
	public boolean isFiringRocket() {
		return this.firingRocket;
	}
	
	public void setFiredBullet(boolean firingBullet) {
		this.firingBullet = firingBullet;
		if(firingBullet) this.firedBulletTime = System.currentTimeMillis();
	}
	public boolean isFiringBullet() {
		return this.firingBullet;
	}
	
	public void setFiredBulletTime() {
		this.firedBulletTime = 0L;
	}
	public long getFiredBulletTime() {
		return this.firedBulletTime;
	}
	
	public void setFiredRocketTime() {
		this.firedRocketTime = 0L;
	}
	public long getFiredRocketTime() {
		return this.firedRocketTime;
	}
	
	public double getRocketFireXPos() {
		return (this.getRockets() % 2 == 0) ? (this.getFireXPos() - 20) : (this.getFireXPos() + 10);
	}
	public double getRocketFireYPos() {
		return this.getFireYPos();//(this.getRockets() % 2 == 0) ? (this.getFireYPos() - 10) : (this.getFireYPos() + 10);
		//double yPos = 0.0D;
		//return yPos;
	}
	
	public void invokePickupBonus(Pickup pickup) {
		switch(pickup.getPickupType()) {
			case PICKUP_TYPE_EXTRA_LIFE: { this.setLives(this.getLives() + 1); break; }
			case PICKUP_TYPE_BULLET: { this.setBullets(this.getBullets() + 50); break; }
			case PICKUP_TYPE_ROCKET: { this.setRockets(this.getRockets() + 1); break; }
			case PICKUP_TYPE_SHIELD: { this.setShieldHealth(this.getShieldHealth() + PLAYER_DEFAULT_SHIELD_HEALTH);break; }
		}
	}
}	

/*

package com.jasonandrews.ocja.spaceshipassault;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;

public class Player extends Sprite {
	
	private static final int PLAYER_DEFAULT_HEALTH = 50;
	private static final int PLAYER_DEFAULT_SHIELD_HEALTH = 20;
	
	private double mouseXPos;
	private double mouseYPos;
	private Point mousePointer;
	private int health;
	private int shieldHealth;
	private int lives;
	private int rockets;
	private int bullets;
	
	private boolean flinched;
	private long flinchTimer;
	
	private static BufferedImage playerUpImage;// = ImageIO.read(new File("images/playerUp.png"));
	
	static {
		try {
			playerUpImage = ImageIO.read(new File("images/player_space_ship.png"));
		} catch (IOException e) {
			System.out.println("Could not find image - aaa.png");
		}
	}
	public Player(GamePanel gamePanel) {
		super(gamePanel, PANEL_WIDTH / 2, PANEL_HEIGHT - 90, 1, 3, 0.0, null);
		this.setLives(5);
		this.setHealth(PLAYER_DEFAULT_HEALTH);
		this.setShieldHealth(PLAYER_DEFAULT_SHIELD_HEALTH);
		this.setRockets(3);
		this.setBullets(120);
	}
	public void update() {
		double x = this.getXPos();
		double y = this.getYPos();
		
		double middleX = this.getFireXPos();
		double middleY = this.getFireYPos();
		if(this.getHealth() < 1) {
			this.setHealth(PLAYER_DEFAULT_HEALTH);
			this.lives--;
		}
		
		if(isGoingUp()) {
			this.setDY(-this.getSpeed());
		}
		if(isGoingDown()) {
			this.setDY(this.getSpeed());
		}
		
		if(isGoingRight()) {
			this.setDX(this.getSpeed());
		}
		if(isGoingLeft()) {
			this.setDX(-this.getSpeed());
		}
		
		
		if(middleX + this.getDX() < 0) {
			this.setXPos(x + 1);
			return;
		}
		if(middleX + this.getDX() > PANEL_WIDTH - 40) {
			this.setXPos(x - 1);
			return;
		}
		if(middleY + this.getDY() < 0){
			this.setYPos(y + 1);
			return; 
		}
		if(middleY + this.getDY() > (PANEL_HEIGHT - 60)) {
			this.setYPos(y - 1);
			return;
		}
		
		this.setXPos(x += this.getDX());
		this.setYPos(y += this.getDY());
		
		
		this.setDX(0);
		this.setDY(0);
		
		
	}
	
	public void draw(Graphics2D g) {
		//System.out.println("Players Direction: " + this.direction);
		
		
		// Rotation information
		this.setSpriteImage(playerUpImage);
		//rotationRequired = Math.toRadians(this.calculateAngle()); //= Math.toRadians(45);
		double rotationRequired = Math.toRadians(this.assignNewTarget()); //= Math.toRadians(45);
		this.setAngle(Math.toDegrees(rotationRequired));
		//System.out.println(Math.toDegrees(rotationRequired));
		//System.out.println(this.getAngle());
		// Drawing the rotated image at the required drawing locations
		
		double locationX = this.getSpriteImage().getWidth() / 2;
		double locationY = this.getSpriteImage().getHeight() / 2;
		
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		//System.out.println("Should draw at: " + (int) xPos + " and " + (int) yPos);
		if(this.getSpriteImage() != null) {
			//System.out.println(playerImage);
			//g.drawImage(playerImage, (int) xPos, (int) yPos, null);
			//g.setColor(Color.GREEN);
			//g.drawRect( (int) xPos -2 , (int)yPos + 2, playerImage.getWidth(), playerImage.getHeight());
			
			
			g.drawImage(op.filter(this.getSpriteImage(), null), (int) this.getXPos(), (int) this.getYPos(), null);
			//g.setColor(Color.WHITE);
			//g.fillOval((int) (this.getXPos() - 3), (int) (this.getYPos() - 3), 2 * 3, 2 * 3);
			//g.setColor(Color.BLUE);
			//g.fillOval(this.getFireXPos() - 5, this.getFireYPos() - 5, 2 * 5, 2 * 5);
			
			if(this.getShieldHealth() > 0) {
				//g.setStroke(new BasicStroke(1));
				g.setColor(Color.BLUE);
				g.drawOval(this.getFireXPos() - 30, this.getFireYPos() - 30, 2 * 30, 2 * 30);
			}
		} 
	}
	
	private double calculateAngle() {
		double xPos = this.getXPos();
		double yPos = this.getYPos();
		double spriteWidth = 0.0D;
		double spriteHeight = 0.0D;
		BufferedImage spriteImage = null;
		try {
			this.setSpriteImage(ImageIO.read(new File("images/playerUp.png")));
		} catch(IOException ex) {
			
		}
		mousePointer = this.getGamePanel().getMousePosition();
		if(null != mousePointer) {
			mouseXPos = mousePointer.getX();
			mouseYPos = mousePointer.getY();
			
			spriteImage = this.getSpriteImage();
			spriteWidth = spriteImage.getWidth();
			spriteHeight = spriteImage.getHeight();

			if((mouseXPos - xPos > spriteWidth) && (mouseXPos > xPos && mouseYPos < yPos)) {
				//Top Right.
				this.setDirection(SPRITE_DIRECTION_UP);//direction
				return 45.0D;
			} else if((xPos - mouseXPos > spriteWidth) && (mouseXPos < xPos && mouseYPos < yPos)) {
				//Top Left.
				this.setDirection(SPRITE_DIRECTION_UP);//direction
				return -45.0D;
			} else if((mouseYPos - yPos > spriteHeight) && (mouseXPos - xPos >= spriteWidth) && (mouseXPos > xPos && mouseYPos > yPos)) {
				//Bottom Right.
				this.setDirection(SPRITE_DIRECTION_DOWN);//direction
				return 135.0D;
			} else if((yPos - mouseYPos < spriteHeight) && (xPos - mouseXPos >= spriteWidth) && (mouseXPos < xPos && mouseYPos > yPos)) {
				//Bottom Left.
				if(mouseYPos - yPos < spriteHeight) {
					this.setDirection(SPRITE_DIRECTION_LEFT);//direction
					return -90.0D;
				}
				else {
					this.setDirection(SPRITE_DIRECTION_DOWN);//direction
					return -135.0D;
				}
			} else {
				if(mouseXPos - xPos < spriteWidth && (mouseYPos < yPos)) {
					this.setDirection(SPRITE_DIRECTION_UP);//direction
					return 0.0D; //Up
				}
				else if(mouseXPos - xPos < spriteWidth && (mouseYPos > yPos)) {
					this.setDirection(SPRITE_DIRECTION_DOWN);//direction
					return 180.0D; //Down
				}
				else if(mouseYPos - yPos < spriteHeight && (mouseXPos < xPos)) {
					this.setDirection(SPRITE_DIRECTION_LEFT);
					return -90.0D; //Left
				}
				else if(mouseYPos - yPos < spriteHeight && (mouseXPos > xPos)) {
					this.setDirection(SPRITE_DIRECTION_RIGHT);
					return 90.0D; //Right
				}
				else return this.getAngle();
			}
			
		} 
		return this.getAngle();
	}
	
	public int getLives() {
		return lives;
	}
	public void setLives(int lives) {
		this.lives = lives;
	}
	public int getRockets() {
		return rockets;
	}
	public void setRockets(int rockets) {
		this.rockets = rockets;
	}
	public int getBullets() {
		return bullets;
	}
	public void setBullets(int bullets) {
		if(bullets > 999) bullets = 999;
		this.bullets = bullets;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public int getShieldHealth() {
		return shieldHealth;
	}
	public void setShieldHealth(int shieldHealth) {
		this.shieldHealth = shieldHealth;
	}
	public long getFlinchTimer() {
		return flinchTimer;
	}
	public void setFlinchTimer(long flinchTimer) {
		this.flinchTimer = flinchTimer;
	}
	public boolean isFlinched() {
		return flinched;
	}
	public void setFlinched(boolean flinched) {
		this.flinched = flinched;
		this.setFlinchTimer(System.currentTimeMillis());
		this.setShieldHealth(0);
		this.setHealth(PLAYER_DEFAULT_HEALTH);
	}
}
*/
