package com.kastorcode.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.kastorcode.graphics.Spritesheet;
import com.kastorcode.main.Game;
import com.kastorcode.main.NewerSound;
import com.kastorcode.main.Window;
import com.kastorcode.world.Tile;


public class Tube extends Entity {
	private static final BufferedImage[][] SPRITES = {
		{
			Spritesheet.getSprite(0, Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE),
			Spritesheet.getSprite(0, Tile.TILE_SIZE * 2, Tile.TILE_SIZE, Tile.TILE_SIZE)
		},
		{
			Spritesheet.getSprite(Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE),
			Spritesheet.getSprite(Tile.TILE_SIZE, Tile.TILE_SIZE * 2, Tile.TILE_SIZE, Tile.TILE_SIZE)
		}
	};

	public static final NewerSound
		POINT_SOUND = new NewerSound("/effects/point.wav");

	private boolean down;


	public Tube(double x, double y, int width, int height, boolean down) {
		super(x, y, width, height, 7.5, null);
		this.down = down;
	}


	private boolean isCollidingWithPlayer () {
		Rectangle tubeMask = new Rectangle(getX(), getY(), getWidth(), getHeight());
		Rectangle playerMask = new Rectangle(Game.player.getX() + 1, Game.player.getY() + 7, 62, 50);
		return tubeMask.intersects(playerMask);
	}


	public void tick () {
		depth = 0;
		x -= speed;

		if (x + width < 0) {
			Game.score += 0.5;
			Game.entities.remove(this);

			if (Game.score % 10 == 0) {
				POINT_SOUND.play();
			}

			return;
		}

		if (isCollidingWithPlayer()) {
			Game.over();
			return;
		}
	}


	public void render (Graphics g) {
		if (down) {
			for (int i = height; i > -Tile.TILE_SIZE; i -= Tile.TILE_SIZE) {
				if (i == height) g.drawImage(SPRITES[0][0], getX(), getY(), Tile.TILE_SIZE, Tile.TILE_SIZE, null);
				else g.drawImage(SPRITES[0][1], getX(), Window.HEIGHT - i, Tile.TILE_SIZE, Tile.TILE_SIZE, null);
			}
		}
		else {
			for (int i = height; i > -Tile.TILE_SIZE; i -= Tile.TILE_SIZE) {
				if (i == height) g.drawImage(SPRITES[1][1], getX(), height - Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE, null);
				else g.drawImage(SPRITES[1][0], getX(), getY() + (i - Tile.TILE_SIZE), Tile.TILE_SIZE, Tile.TILE_SIZE, null);
			}
		}
	}
}