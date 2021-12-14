package com.kastorcode.entities;

import java.awt.Graphics;
import java.awt.Graphics2D;

import com.kastorcode.graphics.Spritesheet;
import com.kastorcode.main.Game;
import com.kastorcode.main.NewerSound;
import com.kastorcode.main.Window;
import com.kastorcode.world.Tile;


public class Player extends Entity {
	private static final int SPRITES[][] = {
		{ 0, 0 },
		{ Tile.TILE_SIZE, 0 },
		{ Tile.TILE_SIZE * 2, 0 },
		{ Tile.TILE_SIZE * 3, 0 }
	};

	public static final NewerSound
		WING_SOUND = new NewerSound("/effects/wing.wav");

	public static int xMax = Window.WIDTH / 2;

	public boolean
		isPressed = false,
		right = false,
		left = false,
		down = false,
		playWingSound = false;


	public Player(int x, int y, int width, int height) {
		super(x, y, width, height, 7, null);
		int index = Entity.rand.nextInt(SPRITES.length);
		this.sprite = Spritesheet.getSprite(SPRITES[index][0], SPRITES[index][1], Tile.TILE_SIZE, Tile.TILE_SIZE);
	}


	public void tick () {
		depth = 1;

		if (isPressed) {
			y -= speed;
		}
		else {
			if (down) y += (speed * 1.5);
			else y += speed;
		}

		if (right && getX() < xMax) {
			x += speed;
		}
		else if (left && getX() > 0) {
			x -= speed;
		}

		if (y > Window.HEIGHT || y < -Tile.TILE_SIZE) {
			Game.over();
		}

		if (playWingSound) {
			playWingSound = false;
			WING_SOUND.play();
		}
	}


	public void render (Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		if (isPressed) {
			g2.drawImage(sprite, getX(), getY(), null);
		}
		else {
			g2.rotate(Math.toRadians(20), getX() + width / 2, getY() + height / 2);
			g2.drawImage(sprite, getX(), getY(), null);
			g2.rotate(Math.toRadians(-20), getX() + width / 2, getY() + height / 2);
		}
	}
}