package com.jasonandrews.ocja.spaceshipassault;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.event.MouseAdapter;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class GamePanel extends JPanel implements GameConstants, Runnable, KeyListener{
	
	private static final int FPS = 30;
	private static final int MISSILE_FIRE_WAIT_TIME = 4000;
	private static BufferedImage hudIconLife;
	private static BufferedImage hudIconRocket;
	//private static BufferedImage hudIconBullet;
	private static BufferedImage hudIconHealth;
	private static BufferedImage hudIconShield;
	static {
		try {
			hudIconLife = ImageIO.read(new File("images/hud_icon_life.png"));
		} catch (IOException ioe) {
			System.out.println("GamePanel: Could not find image - hud_icon_life.png");
		}
		try {
			hudIconRocket = ImageIO.read(new File("images/hud_icon_rocket.png"));
		} catch (IOException ioe) {
			System.out.println("GamePanel: Could not find image - hud_icon_rocket.png");
		}
		try {
			hudIconHealth = ImageIO.read(new File("images/hud_icon_health.png"));
		} catch(IOException ioe) {
			System.out.println("GamePanel: Could not find image - hud_icon_health.png");
		}
		try {
			hudIconShield = ImageIO.read(new File("images/pickup_shield.png"));
		} catch(IOException ioe) {
			System.out.println("GamePanel: Could not find image - pickup_shield.png");
		}
		//try {
			//hudIconBullet = ImageIO.read(new File("images/"))
		//}
	}
	
	private SpaceShipAssaultFrame frame; 
	private GamePanel gamePanel;
	private MainMenuPanel menuPanel;
	private static int difficulty;
	
	private JButton resumeBtn = new JButton("Resume");
	private JButton mainMenuBtn = new JButton("Main Menu");
	private JLabel pauseTextLb = new JLabel("");
	private Thread thread;
	private BufferedImage image;
	private Graphics2D g;
	
	private ArrayList<Laser> laserList;
	private ArrayList<Bullet> bulletList;
	private ArrayList<Rocket> rocketList;
	private ArrayList<Explosion> explosionList;
	private ArrayList<Enemy> enemyList;
	private ArrayList<Pickup> pickupList;
	
	private boolean running;
	
	private Player player;
	
	private boolean paused;
	private boolean gameOver;
	private boolean pauseMenuDrawn;
	
	private boolean missileReady;
	private long missileFireTime = 0L;
	
	private long levelEndTime = 0L;
	private int currentLevel = 0;
	private long nextLevelTime;
	
	/**
	 * Create the panel.
	 */
	public GamePanel(final SpaceShipAssaultFrame frame, final MainMenuPanel menuPanel) {
		setBackground(Color.BLACK);
		gamePanel = this;
		this.frame = frame;
		this.menuPanel = menuPanel;
		this.setFocusable(true);
		this.setDoubleBuffered(true);
		this.setBounds(0, 0, MainMenuPanel.PANEL_WIDTH, MainMenuPanel.PANEL_HEIGHT);
		this.setLayout(null);
		this.missileReady = true;
		
		resumeBtn.setBackground(Color.WHITE);
		
		resumeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gamePanel.paused = false;
				resumeBtn.setVisible(false);
				mainMenuBtn.setVisible(false);
				gamePanel.requestFocus();
			}
		});
		resumeBtn.setVisible(false);
		resumeBtn.setBounds(253, 127, 104, 23);
		add(resumeBtn);
		mainMenuBtn.setBackground(Color.WHITE);
		
		mainMenuBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				running = false;
				frame.setContentPane(menuPanel);
				frame.validate();
			}
		});
		
		mainMenuBtn.setBounds(253, 161, 104, 23);
		mainMenuBtn.setVisible(false);
		add(mainMenuBtn);		
		pauseTextLb.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		pauseTextLb.setForeground(Color.WHITE);
		pauseTextLb.setBounds(253, 83, 104, 33);
		pauseTextLb.setVisible(false);
		add(pauseTextLb);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Point p = gamePanel.getMousePosition();
				//System.out.println("X: " + p.getX() + " | Y: " + p.getY());
				if(e.getButton() != MouseEvent.BUTTON1) return; //Only shoots bullets if they clicked the left mouse button (BUTTON1).
				if(player.getBullets() > 0) {
					
					fireBullet(player.getFireXPos(), player.getFireYPos(), player.getAngle(), true);
				}
			}
		});
				
		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) { }

			@Override
			public void mouseDragged(MouseEvent e) { }
		});
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		if(null == this.thread) {
			thread = new Thread(this);
			thread.start();
		}
		addKeyListener(this);
		//System.out.println("addNotify");
	}
	
	@Override
	public void run() {
		running = true;
		this.requestFocus();
		
		player = new Player(gamePanel);
		laserList = new ArrayList<Laser>();
		bulletList = new ArrayList<Bullet>();
		rocketList = new ArrayList<Rocket>();
		explosionList = new ArrayList<Explosion>();
		enemyList = new ArrayList<Enemy>();
		pickupList = new ArrayList<Pickup>();
		
		image = new BufferedImage(MainMenuPanel.PANEL_WIDTH, MainMenuPanel.PANEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) image.getGraphics();
		
		long beforeTime, sleepTime, timeDifference;
		
		beforeTime = System.currentTimeMillis();
		
		while(!gameOver) {
			
			gameUpdate();
			gameRender();
			gameDraw();
			
			timeDifference = System.currentTimeMillis() - beforeTime;
			sleepTime = FPS - timeDifference;
			
			try {
				if(sleepTime > -1) Thread.sleep(sleepTime);
			} catch(InterruptedException e) {
				System.out.println("ERROR: The thread was interrupted!");
			}
			//System.out.println("Sleep time is: " + sleepTime);
			beforeTime = System.currentTimeMillis();
		}
		gameOver(g);
	}
	@Override
	public void keyPressed(KeyEvent e) {
		//System.out.println("Key pressed");
		//System.out.println("Key pressed");
		int keyCode = e.getKeyCode();
		
		switch(keyCode) {
			case KeyEvent.VK_ESCAPE: {
				//if(!this.isPaused()) {
					//System.out.println("Is Paused");
					this.togglePauseMenu(!paused);
			///	} else {
					//this.togglePauseMenu(false);
				//}
				break;
			}
			case KeyEvent.VK_W: { }
			case KeyEvent.VK_UP: {
				player.setGoingUp(true);
				break;
			}
			case KeyEvent.VK_S: { }
			case KeyEvent.VK_DOWN: {
				player.setGoingDown(true);
				break;
			}
			case KeyEvent.VK_D: { }
			case KeyEvent.VK_RIGHT: {
				player.setGoingRight(true);
				break;
			}
			case KeyEvent.VK_A: { }
			case KeyEvent.VK_LEFT: {
				player.setGoingLeft(true);
				break;
			}
			case KeyEvent.VK_SHIFT: {
				player.setSprinting(true);
			}
			
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		switch(keyCode) {
			case KeyEvent.VK_W: { }
			case KeyEvent.VK_UP: {
				player.setGoingUp(false);
				break;
			}
			case KeyEvent.VK_S: { }
			case KeyEvent.VK_DOWN: {
				player.setGoingDown(false);
				break;
			}
			case KeyEvent.VK_D: { }
			case KeyEvent.VK_RIGHT: {
				player.setGoingRight(false);
				break;
			}
			case KeyEvent.VK_A: { }
			case KeyEvent.VK_LEFT: {
				player.setGoingLeft(false);
				break;
			}
			case KeyEvent.VK_R: {
				this.fireRocket();				
				break;
			}
			case KeyEvent.VK_SPACE: {
				enemyList.add(new Enemy(this, 50, -15, 3, 5));
				break;
			}
			case KeyEvent.VK_SHIFT: {
				player.setSprinting(false);
			}
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	private void gameUpdate() {
		if(!this.isPaused()) {
			for(Laser laser : laserList) {
				laser.update();
			}
			
			//System.out.println(pickupList.size());
			
			if(enemyList.size() == 0) {
				
				this.startNextLevel();
			}
			
			if(missileFireTime == 0L || System.currentTimeMillis() - missileFireTime > MISSILE_FIRE_WAIT_TIME) { missileReady = true; }
			
			double distanceX, distanceY, finalDistance;
			distanceX = distanceY = finalDistance = 0.0D;
			
			player.assignNewTarget();
			player.update();
			
			//Check if the players flinch (protection) time is up.
			if(player.isFlinched()) {
				if(System.currentTimeMillis() - player.getFlinchTimer() > 5000) player.setFlinched(false);
			}
			
			Enemy enemy = null;			
			if(this.enemyList.size() > 0) {
				for(int i = (enemyList.size() - 1); i >= 0; --i) {
					enemy = enemyList.get(i);
					
					if(enemy != null && enemy.isVisible()) { 
						enemy.update();
						
						//Player - Enemy spaceship collision detection.
						distanceX = (enemy.getFireXPos() - player.getFireXPos());
						distanceY = (enemy.getFireYPos() - player.getFireYPos());
						finalDistance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
						//System.out.println("Final Distance: " + finalDistance);
						if(finalDistance < 30) {
							//if(player.getFlinchTimer() == 0 || System.currentTimeMillis() - player.getFlinchTimer() > 5000) {
							if(!player.isFlinched()) {
								enemy.setHealth(0);
								player.setFlinched(true);//flinched
								player.setLives(player.getLives() - 1);
								//player.setFlinchTimer(System.currentTimeMillis());
							}
						}
						
						for(Bullet tempBullet : bulletList) {
							if(tempBullet == null || !tempBullet.isPlayerBullet()) continue;
							distanceX = (enemy.getFireXPos() - tempBullet.getXPos());
							distanceY = (enemy.getFireYPos() - tempBullet.getYPos());
							finalDistance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));

							if(finalDistance < (enemy.getArea()/100)) {
								tempBullet.setVisible(false);
								if(enemy.isVulnerable()) enemy.setHealth(enemy.getHealth() - 10);
							}
						}
						
						distanceX = (enemy.getXPos() - enemy.getTargetX());
						distanceY = (enemy.getYPos() - enemy.getTargetY());
						finalDistance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
						if(finalDistance < 16) {
							enemy.assignNewTarget();
							//System.out.println("Near target");
						}
					}
					else { 
						this.createRandomPickup(enemy);//Spawn a pickup (may not always spawn a pickup).
						
						enemyList.remove(i);
						if(enemyList.size() == 0) levelEndTime = System.currentTimeMillis();
					}					
				}
			}
			
			
			Bullet bullet = null;
			if(this.bulletList.size() > 0) {
				for(int i = (bulletList.size() - 1); i >= 0; --i) {
					bullet = bulletList.get(i);
					if(bullet != null && bullet.isVisible()) { 
						bullet.update();
						
						if(bullet == null || bullet.isPlayerBullet()) continue;
						
						distanceX = (player.getFireXPos() - bullet.getXPos());
						distanceY = (player.getFireYPos() - bullet.getYPos());
						finalDistance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
						//System.out.println("Final Distance: " + finalDistance);
						if(finalDistance < (player.getArea()/100)) {
							
							if(player.getShieldHealth() > 0) {
								player.setShieldHealth(player.getShieldHealth() - 20);
							} else player.setHealth(player.getHealth() - 10);
							bullet.setVisible(false);
							bulletList.remove(i);
							//System.out.println("Player took a bullet!");
						}
					}
					else { bulletList.remove(i); }
				}
			}
			
			Rocket rocket = null;
			if(this.rocketList.size() > 0) {
				for(int i = (rocketList.size() - 1); i >= 0; --i) {
					rocket = rocketList.get(i);
					
					if(rocket != null && rocket.isVisible()) { rocket.update(); }
					else { rocketList.remove(i); }
					Bullet b = null;
					for(int j = (bulletList.size() - 1); j >= 0; --j) {
						distanceX = distanceY = finalDistance = 0.0D;
						b = bulletList.get(j);
						distanceX = (rocket.getXPos() - b.getXPos());
						distanceY = (rocket.getYPos() - b.getYPos());
						finalDistance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
						//System.out.println("Distance: " + finalDistance);
						if(finalDistance < 10) {
							b.setVisible(false);
							bulletList.remove(j);
						}
					}
				}
			}
			
			Explosion explosion = null;
			if(this.explosionList.size() > 0) {
				
				//Loops through all the explosions.
				for(int i = (explosionList.size() - 1); i >= 0; --i) {
					explosion = explosionList.get(i);
					
					//If the explosion is active, then go inside the code block.
					if(explosion != null && explosion.isVisible()) { 
						explosion.update(); 
						
						//Loop through all the bullets and destroy them if they are in range of the explosion.
						for(Bullet tempBullet: bulletList) {
							if(null == tempBullet || !tempBullet.isVisible()) continue; //Skip this iteration if the bullet isn't active anymore.
							
							distanceX = ( (explosion.getXPos() + (explosion.getCurrentAnimationImage().getWidth()/2)) - tempBullet.getXPos());
							distanceY = ( (explosion.getYPos() + (explosion.getCurrentAnimationImage().getHeight()/2)) - tempBullet.getYPos());
							finalDistance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
							if(finalDistance < 50) {
								tempBullet.setVisible(false);
							}
						}
						
						if(!explosion.isDamageEnemy()) continue; //check if this explosion should destroy enemy ships.
						
						//Loop through all the enemies and destroy them if they are in range of the explosion.
						for(Enemy tempEnemy: enemyList) {
							if(null == tempEnemy || !tempEnemy.isVisible()) continue;
							
							distanceX = ( (explosion.getXPos() + (explosion.getCurrentAnimationImage().getWidth()/2)) - tempEnemy.getXPos());
							distanceY = ( (explosion.getYPos() + (explosion.getCurrentAnimationImage().getHeight()/2)) - tempEnemy.getYPos());
							finalDistance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
							//System.out.println("Distance: " + finalDistance);
							if(finalDistance < 65) {
								if(tempEnemy.isVulnerable()) tempEnemy.setHealth(0);
							}
							
						}
					}
					else { explosionList.remove(i); } //If it isn't, just remove the explosion from the ArrayList.
				}
			}
			
			Pickup pickup = null;
			if(pickupList.size() > 0) {
				for(int i = (pickupList.size() - 1); i >= 0; --i) {
			
					pickup = pickupList.get(i);
					
					if(pickup != null && pickup.isVisible()) {
						pickup.update();
						
						distanceX = player.getFireXPos() - (pickup.getXPos() + (pickup.getSpriteImage().getWidth()/2));
						distanceY = player.getFireYPos() - (pickup.getYPos() + (pickup.getSpriteImage().getHeight()/2));
						finalDistance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
						//System.out.println("Distance: " + finalDistance);
						if(finalDistance < 20) {
							player.invokePickupBonus(pickup);
							pickup.setVisible(false);
						}
						
					} else {
						pickupList.remove(i);
					}
				}
			}
			
			player.setFiredBullet(false);
			player.setFiredRocket(false);
			
			if(System.currentTimeMillis() - player.getFiredBulletTime() > 999) player.setFiredBulletTime();
			if(System.currentTimeMillis() - player.getFiredRocketTime() > 999) player.setFiredRocketTime();
		}
	}
	private void gameRender() {
		
		//Draw the background.
		if(!this.isPaused()) {
			
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
			//g.setColor(Color.BLACK);
			//g.fillRect(0, 0, MainMenuPanel.PANEL_WIDTH, MainMenuPanel.PANEL_HEIGHT);
			
			//Next level message
			g.setColor(Color.WHITE);
			if(System.currentTimeMillis() - this.nextLevelTime < 3000) {
				g.setFont(new Font("Bauhaus 93", Font.BOLD, 40));
				g.drawString("Level " + this.currentLevel, (PANEL_WIDTH /2 - 40), (PANEL_HEIGHT / 2)); 
			}
			
			Pickup pickup = null;
			if(pickupList.size() > 0) {
				for(int i = (pickupList.size() - 1); i >= 0; --i) {
					pickup = pickupList.get(i);
					
					if(pickup == null) continue;
					if(pickup.isVisible()) { pickup.draw(g); }
				}
			}
			
			player.draw(g);
			
			Laser laser = null;
			if(this.laserList.size() > 0) {
				for(int i = (laserList.size() - 1); i >= 0; --i) {
					laser = laserList.get(i);
					if(laser.isVisible()) { laser.draw(g); }
					else { laserList.remove(i); }
				}
			}
			Bullet bullet = null;
			if(this.bulletList.size() > 0) {
				for(int i = (bulletList.size() - 1); i >= 0; --i) {
					bullet = bulletList.get(i);
					if(bullet == null) continue;
					if(bullet.isVisible()) { bullet.draw(g); }
				}
			}
			
			Rocket missile = null;
			if(this.rocketList.size() > 0) {

				for(int i = (rocketList.size() - 1); i >= 0; --i) {
					missile = rocketList.get(i);
					if(missile == null) continue;
					if(missile.isVisible()) { missile.draw(g); }
					
				}
			}
			Explosion explosion = null;
			if(this.explosionList.size() > 0) {
				for(int i = (explosionList.size() - 1); i >= 0; --i) {
					explosion = explosionList.get(i);
					if(explosion == null) continue;
					if(explosion.isVisible()) { explosion.draw(g); }
				}
			}

			Enemy enemy = null;
			if(this.enemyList.size() > 0) {
				for(int i = (enemyList.size() - 1); i >= 0; --i) {
					enemy = enemyList.get(i);
					if(enemy == null) continue;
					if(enemy.isVisible()) { enemy.draw(g); }
				}
			}
						
			//HUD (Health, timers, etc.)
			g.setColor(new Color(219, 219, 219));
			g.fillRect(0, MainMenuPanel.PANEL_HEIGHT - 35, MainMenuPanel.PANEL_WIDTH, 60);
			//Missile wait time progress bar
			int remainingLives = player.getLives(), count = 0;
			while(count < remainingLives) {				
				g.drawImage(hudIconLife, ((MainMenuPanel.PANEL_WIDTH / 2) + ( 5 + (hudIconLife.getWidth() * count))) - 100, MainMenuPanel.PANEL_HEIGHT - 32, null);
				++count;
			}
			
			//Health Bar
			g.setColor(Color.RED);
			g.fillRect(438, 347, 100, 20);
			
			if(player.getShieldHealth() < 1) {
				g.setColor(Color.GREEN);
				g.fillRect(438, 347, (player.getHealth() * 2), 20);
				g.drawImage(hudIconHealth,  410,  344, null);
			}
			else {

				g.setColor(Color.BLUE);
				g.fillRect(438, 347, (player.getShieldHealth() * 2), 20);
				g.drawImage(hudIconShield,  415,  344, null);
			}
			g.setColor(Color.DARK_GRAY);
			g.drawRect(438, 347, 100, 20);
			
			
			Color ammoColor = Color.BLACK;
			//Drawing the remaining rockets.
			g.drawImage(hudIconRocket, 0, MainMenuPanel.PANEL_HEIGHT - 37, null);
			g.setColor(Color.RED);
			g.setFont(new Font("Courier", Font.BOLD, 15));
			
			if(player.getRockets() == 0) ammoColor = Color.RED;
			g.setColor(ammoColor);
			g.drawString(""+player.getRockets(),35, MainMenuPanel.PANEL_HEIGHT - 10);
			
			if(!missileReady) { 
			
				g.setColor(Color.white);
				g.fillRect(50, MainMenuPanel.PANEL_HEIGHT - 25, 100, 20);
				g.setColor(Color.RED);
				g.fillRect(50, MainMenuPanel.PANEL_HEIGHT - 25, 100 - (int)this.getMissileTime(), 20);
				
				//g.setColor(new Color(166, 3, 14));
				g.setColor(Color.DARK_GRAY);
				g.drawRect(50, MainMenuPanel.PANEL_HEIGHT - 25, 100, 20);
			} else {
				String rocketStatusStr = "Ready!";
				if(player.getRockets() == 0) rocketStatusStr = "Empty!";
				g.setColor(Color.RED);
				g.setFont(new Font("Courier", Font.BOLD, 15));
				g.drawString(rocketStatusStr,70, MainMenuPanel.PANEL_HEIGHT - 10);
			}
			
			g.setColor(new Color(128, 128, 128));
			g.fillOval(160, MainMenuPanel.PANEL_HEIGHT - 20, 2 * 6, 2 * 6);
			//g.drawImage(null, 160, MainMenuPanel.PANEL_HEIGHT - 20, null);
			
			if(player.getBullets() > 0) ammoColor = Color.BLACK;
			g.setColor(ammoColor);
			g.drawString(""+player.getBullets(), 175, MainMenuPanel.PANEL_HEIGHT - 10);
			
			g.setColor(Color.WHITE);
			g.fillRect((MainMenuPanel.PANEL_WIDTH /2 - ((int) player.getSprintLeft()/2)), MainMenuPanel.PANEL_HEIGHT - 41, (int)(player.getSprintLeft()), 5);
			
		} else {
			if(!this.pauseMenuDrawn) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, MainMenuPanel.PANEL_WIDTH, MainMenuPanel.PANEL_HEIGHT);
				togglePauseMenu(false);
				this.pauseMenuDrawn = true;
			}
		}
	}
	private void gameDraw() {
		if(this.running) {
			if(!this.isPaused()) {
				Graphics g2 = this.getGraphics();
				g2.drawImage(image, 0, 0, this);
				g2.dispose();
				
			}
		}
	}
	
	private void gameOver(Graphics2D g) {
		
	}
	
	private void togglePauseMenu(boolean paused) {
		this.paused = paused;
		
		if(paused) {
			//System.out.println("Should show");
			mainMenuBtn.setVisible(true);
			resumeBtn.setVisible(true);
			pauseTextLb.setVisible(true);
			pauseTextLb.setFont(new Font("Courier", Font.PLAIN, 25));
			pauseTextLb.setText("Paused");
			this.pauseMenuDrawn = true;
		} else {
			//System.out.println("Should hide");
			mainMenuBtn.setVisible(false);
			resumeBtn.setVisible(false);
			pauseTextLb.setVisible(false);
			pauseTextLb.setFont(null);
			pauseTextLb.setText(null);
		}
	}
	
	public boolean isPaused() {
		return this.paused;
	}
	
	public void fireBullet(double startX, double startY, double angle, boolean playerBullet) {
		//Point mousePoint = gamePanel.getMousePosition();
				
		//startX = (int) player.getFireXPos();
		//startY = (int) player.getFireYPos();
		bulletList.add(new Bullet(gamePanel, startX, startY, player.getDirection(), 4, angle, playerBullet));
		
		//If the player shot the bullet, then take one away from their ammo.
		if(playerBullet) {
			player.setBullets(player.getBullets() - 1);
			player.setFiredBullet(true);
		}
	}
	
	private void fireRocket() {
		if(player.getRockets() == 0) return;		
		Point mousePoint = this.getMousePosition();
		
		if(mousePoint != null && missileReady) {
			double xDistance = 0.0D, yDistance = 0.0D;
			xDistance = player.getFireXPos() - mousePoint.getX();
			yDistance = player.getFireYPos() - mousePoint.getY();
			double distanceFromPlayer = Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));
			
			if(distanceFromPlayer < 80) return;
			
			rocketList.add(new Rocket(gamePanel, player.getRocketFireXPos(), player.getRocketFireYPos(), mousePoint.getX(),mousePoint.getY(), player.getAngle()));
			missileFireTime = System.currentTimeMillis();
			missileReady = false;
			player.setRockets(player.getRockets() - 1);
			
			player.setFiredRocket(true);
		}
	}
	
	public void createExplosion(double xPos, double yPos, boolean doEnemyDamage) {
		explosionList.add(new Explosion(this, xPos, yPos, doEnemyDamage));
	}
	
	private void createRandomPickup(Enemy enemy) {
		int chance = (int) (Math.random() * 10);
		switch(chance) {
			case 1: { pickupList.add(new ExtraLifePickup(this, enemy.getXPos(), enemy.getYPos())); break; }
			case 2: { pickupList.add(new BulletPickup(this, enemy.getXPos(), enemy.getYPos())); break; }
			case 3: { pickupList.add(new RocketPickup(this, enemy.getXPos(), enemy.getYPos())); break; }
			case 4: { pickupList.add(new ShieldPickup(this, enemy.getXPos(), enemy.getYPos())); break; }
		}	
	}
	
	private void startNextLevel() {
		if(System.currentTimeMillis() - levelEndTime < 3500) return;
		this.currentLevel++;
		int enemiesToSpawn = (this.currentLevel * 2) + ((int) Math.random() * 10);
		enemiesToSpawn = (enemiesToSpawn < 2) ? 2 : enemiesToSpawn;
		
		int counter = 0;
		while(counter != enemiesToSpawn) {
			enemyList.add(new Enemy(this, (Math.random() * PANEL_WIDTH), -15, 3, 180));//Math.random() < 0.5D ? (Math.random() * 180) : (Math.random() * -180)));
			counter++;
		}	
		nextLevelTime = System.currentTimeMillis();
		
		
	}
	
	private long getMissileTime() {		
		return ((System.currentTimeMillis() - this.missileFireTime) /10) / (MISSILE_FIRE_WAIT_TIME / 1000);
	}
	
	public Player getPlayer() {
		return this.player;
	}

	public static int getDifficulty() {
		return difficulty;
	}

	public static void setDifficulty(int difficulty) {
		GamePanel.difficulty = difficulty;
	}
	
	
}
