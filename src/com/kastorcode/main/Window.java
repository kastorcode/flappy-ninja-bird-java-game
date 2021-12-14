package com.kastorcode.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;


public class Window extends Canvas implements ActionListener {
	private static final long serialVersionUID = 1L;

	public boolean fullScreen = true;

	public static final int WIDTH = 1280, HEIGHT = 720, SCALE = 1;

	public static JFrame frame;


	public Window () {
		frame = new JFrame("Flappy Ninja Bird");
		frame.add(this);
		frame.setResizable(false);
		createMenu();
		toggleFullScreen();

		Image icon = null;

		try {
			icon = ImageIO.read(getClass().getResource("/images/icon.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image cursorImage = toolkit.getImage(getClass().getResource("/images/cursor.png"));
		Cursor cursor = toolkit.createCustomCursor(cursorImage, new Point(0, 0), "img");

		frame.setIconImage(icon);
		frame.setCursor(cursor);
		frame.setAlwaysOnTop(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	public void createMenu () {
		UIManager.put("MenuBar.background", Color.BLACK);

		UIManager.put("Menu.borderPainted", false);
		UIManager.put("Menu.foreground", new Color(227, 76, 15));
		UIManager.put("Menu.selectionBackground", new Color(227, 76, 15));
		UIManager.put("Menu.selectionForeground", new Color(255, 255, 255));

		UIManager.put("RadioButtonMenuItem.borderPainted", false);
		UIManager.put("RadioButtonMenuItem.background", Color.BLACK);
		UIManager.put("RadioButtonMenuItem.foreground", new Color(227, 76, 15));
		UIManager.put("RadioButtonMenuItem.selectionBackground", new Color(227, 76, 15));
		UIManager.put("RadioButtonMenuItem.selectionForeground", new Color(255, 255, 255));

		UIManager.put("MenuItem.borderPainted", false);
		UIManager.put("MenuItem.background", Color.BLACK);
		UIManager.put("MenuItem.foreground", new Color(227, 76, 15));
		UIManager.put("MenuItem.selectionBackground", new Color(227, 76, 15));
		UIManager.put("MenuItem.selectionForeground", new Color(255, 255, 255));

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		frame.setJMenuBar(menuBar);

		JMenu optionsMenu = new JMenu("Options");
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(optionsMenu);

		JRadioButtonMenuItem fullScreenRadio =
        	new JRadioButtonMenuItem("Full screen");
		fullScreenRadio.addActionListener(this);
		optionsMenu.add(fullScreenRadio);

		JMenuItem helpAction = new JMenuItem("Help");
		helpAction.addActionListener(this);
		optionsMenu.add(helpAction);

		JMenuItem aboutAction = new JMenuItem("About");
		aboutAction.addActionListener(this);
		optionsMenu.add(aboutAction);
	}


	public void toggleFullScreen () {
		fullScreen = !fullScreen;
		frame.setVisible(false);
		frame.dispose();
		frame.setUndecorated(fullScreen);

		if (fullScreen) {
			setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
			frame.pack();
			frame.setLocation(0, 0);
		}
		else {
			setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
			frame.pack();
			frame.setLocationRelativeTo(null);
		}

		frame.setVisible(true);
		requestFocus();
	}


	@Override
	public void actionPerformed(ActionEvent event) {
		switch (event.getActionCommand()) {
			case "About": {
				JOptionPane.showMessageDialog(
					frame,
					"Developed by Matheus Ramalho de Oliveira\nhttps://github.com/kastorcode\nhttps://www.instagram.com/kastorcode\nkastorcode@gmail.com",
					"About",
					JOptionPane.PLAIN_MESSAGE
				);
				break;
			}

			case "Full screen": {
				toggleFullScreen();
				break;
			}

			case "Help": {
				JOptionPane.showMessageDialog(
					frame,
					"RIGHT or D: move the character to the right.\nLEFT or A: move the character to the left.\nUP, W, SPACE, J or X: move the character up.\nDOWN or S: move the character down.\nESC or BACK_SPACE: pause the game.\nF1 or F12: save the game.\nF5 or F8: load the game.\nF or F11: enter or exit full screen.",
					"Help",
					JOptionPane.PLAIN_MESSAGE
				);
				break;
			}
		}
	}
}