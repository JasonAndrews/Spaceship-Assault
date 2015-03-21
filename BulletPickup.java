package com.jasonandrews.ocja.spaceshipassault;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BulletPickup extends Pickup {
	
	private static BufferedImage bulletPickupSprite;
	
	static {
		try {
			bulletPickupSprite = ImageIO.read(new File("images/pickup_bullet.png"));
		} catch (IOException ioe) {
			System.out.println("Could not find image - pickup_bullet.png");
		}
	}

	public BulletPickup(GamePanel gamePanel, double xPos, double yPos) {
		super(gamePanel, xPos, yPos, PICKUP_TYPE_BULLET);
		this.setSpriteImage(bulletPickupSprite);
	}

}
