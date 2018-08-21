package main;

import java.awt.Point;
import java.util.LinkedList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import javafx.scene.input.KeyCode;
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
	private int SPEED = 4;
	int height, width;

	private Input input;

	public SimpleTest() {
		super("Jurassic Park");
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		input = container.getInput();
		renderer = new Renderer();
		world = new World();
		world.setLabels(true);
		System.out.println("[*] made world");
		this.zoomLevel = world.getMap().getWidth();

		Terrain terrain = world.getMap().getTerrain();
		width = terrain.getSize();
		height = terrain.getSize();

		LinkedList<KeyCode> keysPressed = new LinkedList<KeyCode>();

	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		world.update();
		input.isKeyDown(Input.KEY_SPACE);
		// input.

		if (input.isKeyDown(Input.KEY_T)) {
			if (world.isLabels()) {
				world.setLabels(false);
			} else {
				world.setLabels(true);
			}

		} else if (input.isKeyDown(Input.KEY_M)) {
			renderer.switchMode();
		}
		if (input.isKeyDown(Input.KEY_Z)) {
			// filer.saveStringMap("files/map.txt", grid);
		} else if (input.isKeyDown(Input.KEY_SPACE)) {
			// map = new Original(type);
			// map.make();
			// grid = map.getGrid();

			// theStage.show();

		} else if (input.isKeyDown(Input.KEY_UP)) {
			if (zoomLevel > 4) {
				zoomLevel -= ZOOMSPEED;
			}
			coords.translate(ZOOMSPEED / 2, ZOOMSPEED / 2);
			if (zoomLevel < 75) {
				SPEED = 1;
			}

		} else if (input.isKeyDown(Input.KEY_DOWN)) {
			if (zoomLevel < height - 1) {
				zoomLevel += ZOOMSPEED;
				coords.translate(-ZOOMSPEED / 2, -ZOOMSPEED / 2);
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
				SPEED = 4;
			}
		} else if (input.isKeyDown(Input.KEY_W)) {

			if (coords.y > 0) {
				coords.translate(0, -SPEED);
			}
		} else if (input.isKeyDown(Input.KEY_S)) {
			if (coords.y + zoomLevel < height) {
				coords.translate(0, SPEED);
			}
		} else if (input.isKeyDown(Input.KEY_A)) {
			if (coords.x > 0) {
				coords.translate(-SPEED, 0);
			}
		} else if (input.isKeyDown(Input.KEY_D)) {
			if (coords.x + zoomLevel < width) {
				coords.translate(SPEED, 0);
			}
		} else if (input.isKeyDown(Input.KEY_T)) {

		}

	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		renderer.drawWorldSlick(world, coords, zoomLevel, container, g);
		// g.drawString("Hello, Slick world!", 0, 300);
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
}