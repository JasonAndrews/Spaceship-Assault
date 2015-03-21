package com.jasonandrews.ocja.spaceshipassault;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RocketPickup extends Pickup {
	private static BufferedImage rocketPickupSprite;
	static {
		try {
			rocketPickupSprite = ImageIO.read(new File("images/pickup_rocket.png"));
		} catch (IOException ioe) {
			System.out.println("Could not find image - pickup_rocket.png");
		}
	}
	
	public RocketPickup(GamePanel gamePanel, double xPos, double yPos) {
		super(gamePanel, xPos, yPos, PICKUP_TYPE_ROCKET);
		this.setSpriteImage(rocketPickupSprite);
	}

}
