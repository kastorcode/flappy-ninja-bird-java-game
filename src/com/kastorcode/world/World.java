package com.kastorcode.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.kastorcode.entities.Entity;
import com.kastorcode.main.Window;


public class World {
	private static final String[] LEVELS = {
		"level1.png",
		"level2.png",
		"level3.png",
		"level4.png"
	};

	private static BufferedImage bgImage;

	public static int
		WIDTH = Window.WIDTH,
		HEIGHT = Window.HEIGHT;


	public World () {
		try {
			bgImage = ImageIO.read(getClass().getResource("/images/" + LEVELS[Entity.rand.nextInt(LEVELS.length)]));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void render (Graphics g) {
		g.drawImage(bgImage, 0, 0, null);
		g.setColor(new Color(0, 0, 0, 127));
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}
}