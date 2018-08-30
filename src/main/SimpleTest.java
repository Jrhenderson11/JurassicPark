package main;

import java.awt.Point;
import java.util.LinkedHashSet;
import java.util.Set;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;

import rendering.Renderer;
import version2.Terrain;
import world.World;

public class SimpleTest extends BasicGame {

	private int displayWidth;
	private int displayHeight;
	protected Renderer renderer;
	private World world;

	private Point coords = new Point(0, 0);
	private int zoomLevel;
	private int ZOOMSPEED = 2;
	private int DEFAULT_SPEED = 1;
	private int SPEED = DEFAULT_SPEED;
	int height, width;

	private Input input;

	private boolean started = false;
	Set<Integer> keysPressed;
	public Object lock = new Object();
	public Object lock2 = new Object();

	public SimpleTest() {
		super("Jurassic Park");
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		//container.setIcon("assets/stegosaurus.png");
		input = container.getInput();
		keysPressed = new LinkedHashSet<Integer>();
		addListeners();
		renderer = new Renderer();
		world = new World();
		world.setLabels(true);

		System.out.println("[*] made world");
		this.zoomLevel = world.getMap().getWidth();
		Terrain terrain = world.getMap().getTerrain();
		width = terrain.getSize();
		height = terrain.getSize();

	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		world.update();
		synchronized (lock2) {

			if (keysPressed.contains(Input.KEY_Z)) {
				// filer.saveStringMap("files/map.txt", grid);
			}
			if (keysPressed.contains(Input.KEY_SPACE)) {
			}
			if (keysPressed.contains(Input.KEY_UP)) {

				synchronized (lock) {
					// zoom in
					if (zoomLevel > 10) {
						zoomLevel -= ZOOMSPEED;
						if (coords.y + ZOOMSPEED / 2 > 0 && coords.y + ZOOMSPEED / 2 < height) {
							coords.translate(0, ZOOMSPEED / 2);
						}
						if (coords.x + ZOOMSPEED / 2 > 0 && coords.x + ZOOMSPEED / 2 < width) {
							coords.translate(ZOOMSPEED / 2, 0);
						}
						if (zoomLevel < 75) {
							SPEED = 1;
						}
					}

				}

			}
			if (keysPressed.contains(Input.KEY_DOWN)) {
				synchronized (lock) {
					// zoom out
					if (zoomLevel < height - 1) {
						zoomLevel += ZOOMSPEED;
						if (coords.y - ZOOMSPEED / 2 > 0 && coords.y - ZOOMSPEED / 2 < height) {
							coords.translate(0, -ZOOMSPEED / 2);
						}
						if (coords.x - ZOOMSPEED / 2 > 0 && coords.x - ZOOMSPEED / 2 < width) {
							coords.translate(-ZOOMSPEED / 2, 0);
						}

						if (coords.y + zoomLevel > height) {
							coords.translate(0, -ZOOMSPEED);
						}
						if (coords.x + zoomLevel > width) {
							coords.translate(-ZOOMSPEED, 0);
						}
						if (coords.y < 0) {
							coords.translate(0, ZOOMSPEED);
						}
						if (coords.x < 0) {
							coords.translate(ZOOMSPEED, 0);
						}
					}
					if (zoomLevel >= 75) {
						SPEED = DEFAULT_SPEED;
					}
				}
			}
			if (keysPressed.contains(Input.KEY_W)) {

				if (coords.y - SPEED >= 0) {
					coords.translate(0, -SPEED);
				}
			}
			if (keysPressed.contains(Input.KEY_S)) {

				synchronized (lock) {
					if (coords.y + zoomLevel + SPEED <= height) {
						coords.translate(0, SPEED);
					}
				}
			}
			if (keysPressed.contains(Input.KEY_A)) {

				if (coords.x - SPEED >= 0) {
					coords.translate(-SPEED, 0);
				}
			}
			if (keysPressed.contains(Input.KEY_D)) {

				synchronized (lock) {
					if (coords.x + zoomLevel + SPEED <= width) {
						coords.translate(SPEED, 0);
					}
				}
			}
			if (keysPressed.contains(Input.KEY_T)) {

			}
		}
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		if (!started) {
			renderer.initiateMap(world.getMap(), coords, zoomLevel, container, g);
			started = true;
		}
		synchronized (lock2) {
			renderer.drawWorldSlick(world, coords, zoomLevel, container, g);
			// g.drawString("Hello, Slick world!", 0, 300);

		}
	}

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new SimpleTest());
			app.setDisplayMode(900, 900, false);

			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	private void addListeners() {
		input.addKeyListener(new KeyListener() {

			@Override
			public void inputEnded() {
				// TODO Auto-generated method stub

			}

			@Override
			public void inputStarted() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isAcceptingInput() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public void setInput(Input input) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(int key, char c) {
				keysPressed.add(key);
			}

			@Override
			public void keyReleased(int key, char c) {
				keysPressed.remove(key);
				if (key == Input.KEY_T) {
					if (world.isLabels()) {
						world.setLabels(false);
					} else {
						world.setLabels(true);
					}
				} else if (key == Input.KEY_M) {
					renderer.switchMode();
				}

			}

		});
	}
}