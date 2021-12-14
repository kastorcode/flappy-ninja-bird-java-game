package com.kastorcode.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import com.kastorcode.entities.Entity;
import com.kastorcode.entities.Player;
import com.kastorcode.graphics.Spritesheet;
import com.kastorcode.graphics.UI;
import com.kastorcode.world.Tile;
import com.kastorcode.world.TubeGenerator;
import com.kastorcode.world.World;


public class Game extends Window implements Runnable, KeyListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;

	public static boolean pause = true;

	public static String saveOrLoad = null;

	public static TubeGenerator tubeGenerator;

	private Thread thread;

	private boolean isRunning;

	private BufferedImage image;

	public static List<Entity> entities;

	public static Spritesheet spritesheet;

	public static World world;

	public static Player player = null;

	public UI ui;

	public int[] pixels;

	public static double score = 0;

	public static final NewerSound
		DIE_SOUND = new NewerSound("/effects/die.wav");


	public Game () {
		super();

		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		spritesheet = new Spritesheet("spritesheet.png");
		entities = new ArrayList<Entity>();
		world = new World();
		player = new Player(WIDTH / 8, HEIGHT / 4, Tile.TILE_SIZE, Tile.TILE_SIZE);
		tubeGenerator = new TubeGenerator();
		ui = new UI();
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		entities.add(player);
	}


	public synchronized void start () {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}


	public synchronized void stop () {
		isRunning = false;

		try {
			thread.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public static void main (String args[]) {
		Game game = new Game();
		game.start();
	}


	public void tick () {
		if (saveOrLoad != null) {
			switch (saveOrLoad) {
				case "load": {
					String saver = Menu.loadGame(10);

					if (saver.length() > 0) {
						Menu.applySave(saver);
					}
					else {
						pause = true;
						JOptionPane.showMessageDialog(
							frame,
							"No saves found.",
							"",
							JOptionPane.PLAIN_MESSAGE
						);
					}

					break;
				}

				case "save": {
					if (score > 0) {
						String[] keys = { "score" };

						int[] values = { (int)score };

						Menu.saveGame(keys, values, 10);
					}
					break;
				}
			}
			saveOrLoad = null;
		}

		if (pause) {
			Game.player.playWingSound = false;
			return;
		}

		tubeGenerator.tick();

		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			entity.tick();
		}
	}


	public static void over () {
		DIE_SOUND.play();
		score = 0;
		entities = new ArrayList<Entity>();
		spritesheet = new Spritesheet("spritesheet.png");
		world = new World();
		player = new Player(WIDTH / 8, HEIGHT / 4, Tile.TILE_SIZE, Tile.TILE_SIZE);
		tubeGenerator = new TubeGenerator();

		entities.add(player);
		return;
	}


	public void render () {
		BufferStrategy bs = this.getBufferStrategy();

		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = image.getGraphics();

		g.setColor(new Color(122, 102, 255));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		Collections.sort(entities, Entity.nodeSorter);
		world.render(g);

		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			entity.render(g);
		}

		ui.render(g);

		if (pause) {
			g.setColor(new Color(0, 0, 0, 127));
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.drawImage(Spritesheet.kastorcode, (Window.WIDTH / 2) - (Spritesheet.kastorcode.getWidth() / 2), (Window.HEIGHT / 2) - (Spritesheet.kastorcode.getHeight() / 2), null);
		}

		g.dispose();
		g = bs.getDrawGraphics();

		if (fullScreen) {
			g.drawImage(image, 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, null);
		}
		else {
			g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		}

		bs.show();
	}


	public void run () {
		long lastTime = System.nanoTime();
		double amountOfTicks = 30.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();

		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}

			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}

		stop();
	}


	@Override
	public void keyPressed (KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_J:
			case KeyEvent.VK_X: {
				Game.player.isPressed = true;
				break;
			}

			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D: {
				Game.player.right = true;
				break;
			}

			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A: {
				Game.player.left = true;
				break;
			}

			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S: {
				Game.player.down = true;
				break;
			}

			case KeyEvent.VK_ESCAPE:
			case KeyEvent.VK_BACK_SPACE: {
				pause = !pause;
				break;
			}

			case KeyEvent.VK_F1:
			case KeyEvent.VK_F12: {
				saveOrLoad = "save";
				break;
			}

			case KeyEvent.VK_F5:
			case KeyEvent.VK_F8: {
				saveOrLoad = "load";
				break;
			}

			case KeyEvent.VK_F:
			case KeyEvent.VK_F11: {
				toggleFullScreen();
				break;
			}
		}
	}


	@Override
	public void keyReleased (KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_J:
			case KeyEvent.VK_X: {
				Game.player.isPressed = false;
				Game.player.playWingSound = true;
				break;
			}

			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D: {
				Game.player.right = false;
				break;
			}

			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A: {
				Game.player.left = false;
				break;
			}

			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S: {
				Game.player.down = false;
				break;
			}
		}
	}


	@Override
	public void keyTyped (KeyEvent e) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mouseClicked (MouseEvent arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mouseEntered (MouseEvent arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mouseExited (MouseEvent arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mousePressed (MouseEvent e) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mouseReleased (MouseEvent arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}