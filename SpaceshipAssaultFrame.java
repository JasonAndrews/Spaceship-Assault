/*
 * The spaceship looks in the direction that the players mouse is. He can left click his mouse to fire bullets or press R to fire rockets.
 * Spaceship Assault is a game programmed in Java where the player controls a spaceship in space. The player can move using WASD or UP, DOWN, LEFT, RIGHT.
 
 * Downloads: http://www.mediafire.com/download/5i8v6b16wzv4fb0/Spaceship_Assault_Images.zip
 
 * @author: Jason Andrews.
 * @version 0.100: Added core parts of the game (movement, aiming, firing bullets). 
 * @version 0.200: Added rocket firing and explosions (animation using a sprite sheet) and added in a basic HUD (rocket timer and number of lives).
 * @version 0.300: Updated the HUD with number of lives, the bullet icon and bullet count.
 * @version 0.400: Added basic system for enemies; spawning, direction to travel and total health.
 * @version 0.450: Added enemy and bullet collision detection. Bullets will decrement an enemies health by 10 and will cause an explosion when 0 health.
 * @version 0.500: Added enemy and explosion collision detection, if an enemy is within range of the explosion, it will destroy the enemy. An exploding enemy ship will not destroy enemies aswell though.
 * @version 0.550: Added player and enemy bullet collision detection. 
 * @version 0.600: Added player shields. Shields take twice the damage from bullets. Updated the HUD to display the players health bar and shield health bar if they have an active shield. 
 * @version 0.650: Updated the enemy AI, they will freely move around and fire bullets every few seconds.
 * @version 0.700: Added player and enemy spaceship collision detection which will destroy the enemy space ship and remove a life from the player.
 * @version 0.725: Added 'sprinting' for the players spaceship. Pressing SHIFT will make him sprint and decrease his remaining sprint power left but it will auto regenerate when he isn't pressing sprint.
 * @version 0.730: Updated the HUD to display the amount of sprint a player has left. 
 * @version 0.750: Completely reworked the sprite used for the players spaceship. It now uses a sprite sheet that will now show particular sprites depending on what the player is doing. For example; moving around will now show fire coming from the exhausts.
 * @version 0.755: Adjusted the firing positions for rockets. They will now fire from the 'rocket pods' on the space ship.
 * @version 0.760: Changed the HUD icon for player lives which will also be used for when pickups are added. Also changed the look of the bullet (for now).
 * @version 0.800: Improved the AI system for enemy spaceships and also began implementing the first stages of the difficulty setting.
 * @version 0.825: Added 4 different pickups that will have a chance of dropping randomly on an enemies death. Extra Lifes, Bullets, Rockets and Shields. Added sprites for each of the pickup types and also changed the colour of the bullets.
 * @version 0.850: Implemented the player and pickup functionality. Players can now pickup the different pickups by hovering over them. Updated the HUD to display the new Health/Shield sprite and other sprites.
 * @version 0.875: Fixed a bug which allowed the players shield health go over its max value therefore painting a larger rectangle on the HUD. Added a second circle around the player if they have full shield health.
 * @version 0.880: Added in a new enemy spawn system, enemies now spawn off-screen and make their way down to the screen. Also added a message to the screen at the beginning of each level saying what the current level is. Added in temporary invulnerability for newly spawned enemies. 
 * 
 */

package com.jasonandrews.ocja.spaceshipassault;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SpaceShipAssaultFrame extends JFrame {
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 400;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SpaceShipAssaultFrame frame = new SpaceShipAssaultFrame();
					frame.setFocusable(true);
					frame.requestFocus();
					frame.setResizable(false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
	}

	/**
	 * Create the frame.
	 */
	public SpaceShipAssaultFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, FRAME_WIDTH, FRAME_HEIGHT);
		MainMenuPanel menuPanel = new MainMenuPanel(this);
		menuPanel.setFocusable(true);
		add(menuPanel);
		setContentPane(menuPanel);
		validate();
	}
}
