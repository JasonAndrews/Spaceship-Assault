package com.jasonandrews.ocja.spaceshipassault;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ShieldPickup extends Pickup {

	private static BufferedImage shieldPickupSprite;
	static {
		try {
			shieldPickupSprite = ImageIO.read(new File("images/pickup_shield.png"));
		} catch (IOException ioe) {
			System.out.println("Could not find image - pickup_shield.png");
		}
	}
	
	public ShieldPickup(GamePanel gamePanel, double xPos, double yPos) {
		super(gamePanel, xPos, yPos, PICKUP_TYPE_SHIELD);
		this.setSpriteImage(shieldPickupSprite);
	}

}
