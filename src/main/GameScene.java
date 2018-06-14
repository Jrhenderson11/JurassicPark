package main;

import java.awt.Point;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import rendering.Renderer;
import rendering.SystemSettings;
import world.World;

public class GameScene extends Scene {
	
	private int displayWidth;
	private int displayHeight;
	protected Renderer renderer;
	private World world;
		
	private Point coords = new Point(0,0);
	private int zoomLevel;
	private int ZOOMSPEED = 2;
	private int SPEED = 4;
	
	public GameScene(Group root, Stage primaryStage) {
		super(root);
		

		
		this.displayWidth = SystemSettings.getScreenWidth();
        this.displayHeight = SystemSettings.getScreenHeight();
        Canvas canvas = new Canvas(displayWidth, displayHeight);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
		
		renderer = new Renderer();
		world = new World();
		System.out.println("[*] made world");

		this.zoomLevel = world.getMap().getWidth();
		
		String[][] grid = world.getMap().getGrid();
		int width = grid[0].length;
		int height = grid.length;

		
		setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				String code = e.getCode().toString();
				if (e.getCode()==KeyCode.Z) { 
					//filer.saveStringMap("files/map.txt", grid);
				} else if (e.getCode()==KeyCode.SPACE) { 
					//map = new Original(type);
					//map.make();
					//grid = map.getGrid();
					
					//theStage.show();

				} else if (e.getCode()==KeyCode.UP) {
					if (zoomLevel > 4) {
						zoomLevel-=ZOOMSPEED;
					}
				} else if (e.getCode()==KeyCode.DOWN) {
					if (zoomLevel < height-1) {
						zoomLevel+=ZOOMSPEED;
					
						if (coords.y+zoomLevel > height) {
							coords.translate(0, -ZOOMSPEED);
						}
						if (coords.x+zoomLevel > width) {
							coords.translate(-ZOOMSPEED, 0);
						}
					}
				} else if (e.getCode()==KeyCode.W) {
					
					if (coords.y > 0 ) {
						coords.translate(0, -SPEED);
					}
				} else if (e.getCode()==KeyCode.S) {
					if (coords.y+zoomLevel < height) {
						coords.translate(0, SPEED);
					}
				} else if (e.getCode()==KeyCode.A) {
					if (coords.x > 0 ) {
						coords.translate(-SPEED, 0);
					}
				} else if (e.getCode()==KeyCode.D) {
					if (coords.x+zoomLevel < width) {
						coords.translate(SPEED, 0);
					}
				}
				
			}
		});
		System.out.println("[*] added key press listeners");
		
		new AnimationTimer() {
	       public void handle(long currentNanoTime) {
	    	   world.update();
	    	   renderer.drawWorld(world, coords, zoomLevel, canvas);
	    	   
	       }
	       	
		}.start();
		System.out.println("[*] started game");
	}
	
}