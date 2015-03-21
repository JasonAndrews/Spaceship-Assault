package com.jasonandrews.ocja.spaceshipassault;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ExtraLifePickup extends Pickup {
	
	private static BufferedImage extrasLifePickupSprite;
	
	static {
		try {
			extrasLifePickupSprite = ImageIO.read(new File("images/pickup_extra_life.png")); 
		} catch(IOException ioe) {
			System.out.println("Could not find image - pickup_extra_life.png");
		}
	}
	
	public ExtraLifePickup(GamePanel gamePanel, double xPos, double yPos) {
		super(gamePanel, xPos, yPos, PICKUP_TYPE_EXTRA_LIFE);
		this.setSpriteImage(extrasLifePickupSprite);
	}

}
