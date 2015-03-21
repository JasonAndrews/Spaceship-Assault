package com.jasonandrews.ocja.spaceshipassault;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;

public class CreditsPanel extends JPanel implements Runnable, KeyListener{
	public static final int PANEL_WIDTH = 595;
	public static final int PANEL_HEIGHT= 375;
	
	private SpaceShipAssaultFrame frame;
	private MainMenuPanel mainMenuPanel;
	private CreditsPanel creditsPanel;
	private static BufferedImage backgroundImage;
	private static BufferedImage creditsImage;
	private static Graphics2D g;
	private Thread thread;
	
	private boolean running;
	/**
	 * Create the panel.
	 */
	public CreditsPanel(final SpaceShipAssaultFrame frame, final MainMenuPanel mainMenuPanel) {
		//System.out.println("Main Menu !");
		creditsPanel = this;
		this.mainMenuPanel = mainMenuPanel;
		this.frame = frame;
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(null);
		this.setDoubleBuffered(true);
		this.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		
		
		creditsImage = new BufferedImage(MainMenuPanel.PANEL_WIDTH, MainMenuPanel.PANEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) creditsImage.getGraphics();
		
		try {
			backgroundImage = ImageIO.read(new File("images/creditsBackground.jpg"));
		} catch (IOException e) {
			System.out.println("Image not found!");
		}
		
		
		JLabel menuBackground = new JLabel(new ImageIcon(backgroundImage));		
		
		menuBackground.setSize(PANEL_WIDTH, PANEL_HEIGHT);
		add(menuBackground);
		
		//run();
	}
	
	@Override
	public void addNotify() {
		System.out.println("Called");
		super.addNotify();
		if(null == thread) {
			thread = new Thread(this);
			thread.start();
		}
		addKeyListener(this);
	}
	@Override
	public void run() {
		//System.out.println("credits Called");
		running = true;
		//while(running) {
			repaint();
			
			this.render();
			this.draw();
			this.requestFocus();
		//}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode) {
			case KeyEvent.VK_ESCAPE: {
				frame.remove(this);
				frame.setContentPane(new MainMenuPanel(frame));
				frame.validate();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void render() {
		g.setColor(Color.WHITE);		

		g.setFont(new Font("Tekton Pro", Font.BOLD, 25));
		g.drawString("Space Ship Assault", 192, 68);
		
		g.drawString("Creator: Jason Andrews", 180, 120);
	}
	public void draw() {
		
		Graphics g2 = this.getGraphics();
		
		//System.out.println(g2 + " |  " + creditsImage);
		if(null != g2) {
			g2.drawImage(creditsImage, 0, 0, null);
			g2.dispose();
		}
	}
}
