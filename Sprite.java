package com.jasonandrews.ocja.spaceshipassault;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public abstract class Sprite implements GameConstants{
	public static final int SPRITE_DIRECTION_NONE = -1;
	public static final int SPRITE_DIRECTION_UP = 1;
	public static final int SPRITE_DIRECTION_DOWN = 2;
	public static final int SPRITE_DIRECTION_RIGHT = 3;
	public static final int SPRITE_DIRECTION_LEFT = 4;
	
	private GamePanel gamePanel;
	
	private double xPos;
	private double yPos;
	private int direction = SPRITE_DIRECTION_NONE;
	private double speed;
	private double angle;
	
	private Color color;
	
	private double dx;
	private double dy;
	
	private boolean goingUp;
	private boolean goingDown;
	private boolean goingRight;
	private boolean goingLeft;
	
	private BufferedImage spriteImage;
	private boolean visible;
	
	public Sprite(GamePanel gamePanel, double xPos, double yPos, int direction, double speed, double angle, Color color) {

		this.setGamePanel(gamePanel);
		this.xPos = xPos;
		this.yPos = yPos;
		this.direction = direction;
		this.speed = speed;
		this.angle = angle;
		this.color = color;
		this.setVisible(true);
	}
	
	public double getXPos() {
		return this.xPos;
	}
	public void setXPos(double xPos) {
		this.xPos = xPos;
	}
	
	public double getYPos() {
		return this.yPos;
	}
	public void setYPos(double yPos) {
		this.yPos = yPos;
	}
	
	public void setDX(double dx) { this.dx = dx; }
	public void setDY(double dy) { this.dy = dy; }
	public double getDX() { return this.dx; }
	public double getDY() { return this.dy; }
	
	
	public int getDirection() {
		return this.direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public double getSpeed() {
		return this.speed;
	}
	
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public double getAngle() {
		return this.angle;
	}
	
	public void setSpriteImage(BufferedImage spriteImage) {
		this.spriteImage = spriteImage;
	}
	public BufferedImage getSpriteImage() {
		return this.spriteImage;
	}
	
	public abstract void update();
	public abstract void draw(Graphics2D g);


	public boolean isGoingUp() {
		return goingUp;
	}
	public void setGoingUp(boolean goingUp) {
		this.goingUp = goingUp;
	}
	public boolean isGoingDown() {
		return goingDown;
	}
	public void setGoingDown(boolean goingDown) {
		this.goingDown = goingDown;
	}
	public boolean isGoingRight() {
		return goingRight;
	}
	public void setGoingRight(boolean goingRight) {
		this.goingRight = goingRight;
	}
	public boolean isGoingLeft() {
		return goingLeft;
	}
	public void setGoingLeft(boolean goingLeft) {
		this.goingLeft = goingLeft;
	}
	
	public boolean isMoving() {
		return (this.goingDown || this.goingUp || this.goingRight || this.goingLeft);
	}

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	public Point getCenterPoint() {
		return (new Point((int) (this.getXPos() + (this.getSpriteImage().getWidth()/2)), (int) (this.getYPos() + (this.getSpriteImage().getHeight()/2))));
	}
	
	public double assignNewTarget() {
		double mouseX = 0.0D, mouseY = 0.0D;
		double angleInDegrees = this.angle;
		Point mousePoint = gamePanel.getMousePosition();
		
		if(null != mousePoint) { //Checks that the mouse isn't null (off the frame).
			mouseX = mousePoint.getX();
			mouseY = mousePoint.getY();

			if(this instanceof Player) angleInDegrees = Math.toDegrees(Math.atan2(mouseX - (this.getXPos() + 25), (this.getYPos() + 25) - mouseY));
			else if(this instanceof Rocket) angleInDegrees = Math.toDegrees(Math.atan2(mouseX - (this.getXPos() + 15), (this.getYPos() + 15) - mouseY));
		}
		
		return angleInDegrees;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public double getArea() {
		return (this.spriteImage.getWidth() * this.spriteImage.getHeight());
	}
	
	public int getFireXPos() {
		return (int) (this.getXPos() + (this.getSpriteImage().getWidth() / 2));
	}
	public int getFireYPos() {
		return (int) (this.getYPos() + (this.getSpriteImage().getHeight() / 2));
	}
}
