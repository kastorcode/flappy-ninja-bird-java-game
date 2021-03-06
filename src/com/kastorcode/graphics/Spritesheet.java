package com.kastorcode.graphics;

import java.awt.image.BufferedImage ;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Spritesheet {
	private static BufferedImage spritesheet;

	public static BufferedImage kastorcode;


	public Spritesheet (String name) {
		try {
			spritesheet = ImageIO.read(getClass().getResource("/images/" + name));
			kastorcode = ImageIO.read(getClass().getResource("/images/kastorcode.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static BufferedImage getSprite (int x, int y, int width, int height) {
		return spritesheet.getSubimage(x, y, width, height);
	}
}