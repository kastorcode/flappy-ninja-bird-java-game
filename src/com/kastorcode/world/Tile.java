package com.kastorcode.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Tile {
	public static final int TILE_SIZE = 64;

	private BufferedImage sprite;

	private int x, y;


	public Tile (int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}


	public void render (Graphics g) {
		g.drawImage(sprite, x - Camera.getX(), y - Camera.getY(), null);
	}
}