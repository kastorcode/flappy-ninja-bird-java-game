package com.kastorcode.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.util.Comparator;
import java.util.Random;

import com.kastorcode.main.Game;
import com.kastorcode.main.Window;
import com.kastorcode.world.Camera;
import com.kastorcode.world.Tile;
import com.kastorcode.world.World;


public class Entity {
	public static Random rand = new Random();

	public double x, y, z, speed;

	protected int width, height;

	protected BufferedImage sprite;

	public int depth;


	public Entity (double x, double y, int width, int height, double speed, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.z = 0;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.sprite = sprite;
	}


	public int getX () {
		return (int)x;
	}
	
	
	public void setX (double newX) {
		x = newX;
	}
	
	
	public int getY () {
		return (int)y;
	}
	
	
	public void setY (double newY) {
		y = newY;
	}
	
	
	public int getWidth () {
		return width;
	}
	
	
	public int getHeight () {
		return height;
	}


	public static boolean isColliding (Entity entity1, Entity entity2) {
		Rectangle entity1Mask = new Rectangle(entity1.getX(), entity1.getY(), entity1.getWidth(), entity1.getHeight());
		Rectangle entity2Mask = new Rectangle(entity2.getX(), entity2.getY(), entity2.getWidth(), entity2.getHeight());
		return entity1Mask.intersects(entity2Mask);
	}


	public void tick () {}
	
	
	public double calculateDistance (int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}


	public boolean isColliding (int nextX, int nextY) {
		Rectangle currentEntity = new Rectangle(nextX, nextY, Tile.TILE_SIZE, Tile.TILE_SIZE);
		int entitiesSize = Game.entities.size();

		for (int i = 0; i < entitiesSize; i++) {
			Entity entity = Game.entities.get(i);

			if (entity == this) { continue; }

			Rectangle targetEntity = new Rectangle(entity.getX(), entity.getY(), Tile.TILE_SIZE, Tile.TILE_SIZE);

			if (currentEntity.intersects(targetEntity)) {
				return true;
			}
		}

		return false;
	}


	public boolean pixelPerfectIsColliding (int x1, int y1, int x2, int y2, int[] pixels1, int[] pixels2, BufferedImage sprite1, BufferedImage sprite2) {
		for (int xx1 = 0; xx1 < sprite1.getWidth(); xx1++) {
			for (int yy1 = 0; yy1 < sprite1.getHeight(); yy1++) {
				for (int xx2 = 0; xx2 < sprite2.getWidth(); xx2++) {
					for (int yy2 = 0; yy2 < sprite2.getHeight(); yy2++) {
						int currentPixel1 = pixels1[xx1 + yy1 * sprite1.getWidth()];
						int currentPixel2 = pixels2[xx2 + yy2 * sprite2.getWidth()];

						if (currentPixel1 == 0x00ffffff || currentPixel2 == 0x00ffffff) {
							continue;
						}

						if (xx1 + x1 == xx2 + x2 && yy1 + y1 == yy2 + y2) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}


	public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {
		@Override

		public int compare (Entity e0, Entity e1) {
			if (e1.depth < e0.depth) {
				return + 1;
			}

			if (e1.depth > e0.depth) {
				return - 1;
			}

			return 0;
		}
	};


	public void updateCamera () {
		Camera.setX(Camera.clamp(
			getX() - (Window.WIDTH / 2),
			0,
			(World.WIDTH * Tile.TILE_SIZE) - Window.WIDTH
		));
		Camera.setY(Camera.clamp(
			getY() - (Window.HEIGHT / 2),
			0,
			(World.HEIGHT * Tile.TILE_SIZE) - Window.HEIGHT
		));
	}


	public void render (Graphics g) {
		g.drawImage(sprite, getX() - Camera.getX(), getY() - Camera.getY(), null);
	}
}