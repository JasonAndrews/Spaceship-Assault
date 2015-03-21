package com.jasonandrews.ocja.spaceshipassault;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Explosion extends Sprite {

	private static final int SPRITE_SHEET_SUB_IMAGE_WIDTH = 150;
	private static final int SPRITE_SHEET_SUB_IMAGE_HEIGHT = 115;
	private static BufferedImage explosionImage;
	private BufferedImage currentAnimationImage;
	private int currentFrame;
	private int currentColumn;
	private int currentRow;
	
	private boolean damageEnemy;
	
	static {
		try {
			explosionImage = ImageIO.read(new File("images/explosions.png"));
			//currentAnimationImage = explosionImage.getSubimage(0, 0, SPRITE_SHEET_SUB_IMAGE_WIDTH, SPRITE_SHEET_SUB_IMAGE_HEIGHT);
			//this.setSpriteImage(explosionImage);
		} catch (IOException ioe) {
			System.out.println("Could not find image - explosions.png");
		}
	}
	
	public Explosion(GamePanel gamePanel, double xPos, double yPos, boolean damageEnemy) {
		super(gamePanel, (xPos - (SPRITE_SHEET_SUB_IMAGE_WIDTH/2)), (yPos - (SPRITE_SHEET_SUB_IMAGE_HEIGHT/2)), 0, 0, 0, Color.BLACK);
		this.setDamageEnemy(damageEnemy);
	}

	@Override
	public void update() {
		
		//try {
		
		if(currentRow == 5 && currentColumn == 4) { this.setVisible(false); return; }
		else {
			if((currentColumn - 4) == 0) { currentRow++; currentColumn = 0; } //Checking if the current row is completed.
			
			//Avoding the RasterFormatException. (y + height is outside the raster, etc.)
			if(( (currentColumn * SPRITE_SHEET_SUB_IMAGE_WIDTH) +SPRITE_SHEET_SUB_IMAGE_WIDTH) > explosionImage.getWidth()) { this.setVisible(false); return; } 
			if(( (currentRow * SPRITE_SHEET_SUB_IMAGE_HEIGHT) + SPRITE_SHEET_SUB_IMAGE_HEIGHT) > explosionImage.getHeight()) { this.setVisible(false); return; }
			
			currentAnimationImage = explosionImage.getSubimage(currentColumn * SPRITE_SHEET_SUB_IMAGE_WIDTH, currentRow * SPRITE_SHEET_SUB_IMAGE_HEIGHT, SPRITE_SHEET_SUB_IMAGE_WIDTH - 10, SPRITE_SHEET_SUB_IMAGE_HEIGHT);

		}			
		currentColumn++;
		
	}

	@Override
	public void draw(Graphics2D g) {
		if(this.isVisible()) {
			g.drawImage(currentAnimationImage, (int) this.getXPos(), (int) this.getYPos(), null);
			//g.setColor(Color.WHITE);
			//g.fillOval((int)this.getXPos()- 2, (int)this.getYPos() - 2, 2 * 2, 2 * 2);
		}
	}

	
	public BufferedImage getCurrentAnimationImage() {
		return this.currentAnimationImage;
	}
	public double getArea() {
		return (currentAnimationImage.getWidth() * currentAnimationImage.getHeight());
	}

	public boolean isDamageEnemy() {
		return damageEnemy;
	}

	public void setDamageEnemy(boolean damageEnemy) {
		this.damageEnemy = damageEnemy;
	}

}
