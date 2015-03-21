package com.jasonandrews.ocja.spaceshipassault;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Color;

import javax.swing.JTextArea;
import java.awt.event.KeyAdapter;

public class MainMenuPanel extends JPanel implements Runnable, KeyListener{
	public static final int PANEL_WIDTH = 595;
	public static final int PANEL_HEIGHT= 375;
	
	private static BufferedImage backgroundImage;
	
	private SpaceShipAssaultFrame frame;
	private MainMenuPanel menuPanel;
	
	private Thread thread;
	
	private JButton startBtn = new JButton("Start");
	private JButton settingsBtn = new JButton("Settings");
	private JButton creditsBtn = new JButton("Credits");		
	private JButton exitBtn = new JButton("Exit");
	/**
	 * Create the panel.
	 */
	
	static {
		try {
			backgroundImage = ImageIO.read(new File("images/mainMenuBg.jpg"));
		} catch (IOException e) {
			System.out.println("Image not found!");
		}
	}
	
	public MainMenuPanel(final SpaceShipAssaultFrame frame) {
		//System.out.println("Main Menu !");
		menuPanel = this;
		this.frame = frame;
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(null);
		this.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		
		
		startBtn.setForeground(Color.WHITE);
		startBtn.setBackground(Color.BLACK);
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(menuPanel);
				frame.setContentPane(new GamePanel(frame, menuPanel));
				frame.validate();
			}
		});
		startBtn.setBounds(247, 98, 89, 23);
		this.add(startBtn);
		
		exitBtn.setForeground(Color.WHITE);
		exitBtn.setBackground(Color.BLACK);
		exitBtn.setBounds(247, 200, 89, 23);
		this.add(exitBtn);
		

		settingsBtn.setForeground(Color.WHITE);
		settingsBtn.setBackground(Color.BLACK);
		settingsBtn.setBounds(247, 132, 89, 23);
		this.add(settingsBtn);
		creditsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CreditsPanel creditsPanel = new CreditsPanel(frame, menuPanel);
				frame.remove(menuPanel);
				frame.setContentPane(creditsPanel);
				frame.validate();
			}
		});
		
		creditsBtn.setBackground(Color.BLACK);
		creditsBtn.setForeground(Color.WHITE);
		creditsBtn.setBounds(247, 166, 89, 23);
		this.add(creditsBtn);
		
		JLabel menuBackground = new JLabel(new ImageIcon(backgroundImage));				
		menuBackground.setSize(PANEL_WIDTH, PANEL_HEIGHT);
		add(menuBackground);
		
	}
	
	@Override 
	public void addNotify() {
		super.addNotify();
		if(null == thread) {
			thread = new Thread(this);
			thread.start();
		}
	}
	@Override
	public void run() {
		this.requestFocus();
		startBtn.requestFocus();
	}

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) { }

	@Override
	public void keyReleased(KeyEvent e) { }
}
