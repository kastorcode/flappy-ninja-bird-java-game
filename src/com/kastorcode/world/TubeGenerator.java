package com.kastorcode.world;

import com.kastorcode.entities.Entity;
import com.kastorcode.entities.Tube;
import com.kastorcode.main.Game;
import com.kastorcode.main.Window;


public class TubeGenerator {
	public static int
		spacing = Tile.TILE_SIZE * 2,
		minX = Window.WIDTH - spacing,
		maxHeight = Window.HEIGHT - spacing;

	public int[]
		nextX = {
			Entity.rand.nextInt(Window.WIDTH - minX) + minX,
			Entity.rand.nextInt(Window.WIDTH - minX) + minX
		};

	public int
		time = 0,
		targetTime = 15;


	public void tick () {
		time++;

		if (time == targetTime) {
			time = 0;
			int height1, height2, nextX0, nextX1;
			Tube tube1, tube2;

			if (Entity.rand.nextInt(2) == 0) {
				height1 = Entity.rand.nextInt(maxHeight);
				tube1 = new Tube(nextX[0], 0, Tile.TILE_SIZE, height1, false);

				if (Entity.rand.nextInt(2) == 0) {
					height2 = Window.HEIGHT - height1 - spacing;
				}
				else {
					height2 = Entity.rand.nextInt(Window.HEIGHT - height1 - spacing);
				}

				tube2 = new Tube(nextX[1], Window.HEIGHT - height2, Tile.TILE_SIZE, height2, true);
			}
			else {
				height2 = Entity.rand.nextInt(maxHeight);
				tube2 = new Tube(nextX[1], Window.HEIGHT - height2, Tile.TILE_SIZE, height2, true);

				if (Entity.rand.nextInt(2) == 0) {
					height1 = Window.HEIGHT - height2 - spacing;
				}
				else {
					height1 = Entity.rand.nextInt(Window.HEIGHT - height2 - spacing);
				}

				tube1 = new Tube(nextX[0], 0, Tile.TILE_SIZE, height1, false);
			}

			nextX0 = Entity.rand.nextInt((nextX[1] + 288) - (nextX[1] + 224)) + (nextX[1] + 224);

			if (Entity.rand.nextInt(2) == 0) {
				nextX1 = nextX0;
			}
			else {
				nextX1 = Entity.rand.nextInt((nextX[0] + 288) - (nextX[0] + 224)) + (nextX[0] + 224);
			}

			nextX[0] = nextX0;
			nextX[1] = nextX1;
			Game.entities.add(tube1);
			Game.entities.add(tube2);
		}
	}
}