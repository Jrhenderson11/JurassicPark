package rendering;

import java.awt.Point;
import java.util.HashMap;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import world.Map;
import world.World;
import world.dinosaurs.Dinosaur;
import world.plants.Plant;

public class Renderer {
	private Color SAND = Color.rgb(244, 209, 66);
	private Color PINK = Color.rgb(186, 50, 174);
	private Color GRASS = Color.rgb(125, 186, 81);
	private Color BLUE = Color.rgb(50, 127, 186);
	private Color GREY = Color.rgb(98, 102, 101);
	private Color LIGHT_BLUE = Color.rgb(114, 219, 245);
	private Color DARK_GREEN = Color.rgb(50, 86, 29);
	private HashMap<String, Color> COLOUR_TABLE = new HashMap<String, Color>();

	public Renderer() {
		fillColourTable();
	}
	
	public void fillColourTable() {
		COLOUR_TABLE.put("^", DARK_GREEN);
		COLOUR_TABLE.put("=", BLUE);
		COLOUR_TABLE.put("w", LIGHT_BLUE);
		COLOUR_TABLE.put("-", SAND);
		COLOUR_TABLE.put("x", GREY);
		COLOUR_TABLE.put(".", GRASS);
	}

	public void drawWorld(World world, Point coords, int zoomLevel, Canvas canvas) {
		//clear screen
		
		drawMap(world.getMap(),coords,zoomLevel, canvas);
		for (Dinosaur d:world.getDinos()) {
			drawDinosaur(d, canvas);
		}
		
	}

	private void drawMap(Map map, Point coords, int zoomLevel, Canvas canvas) {
		double cell_width = canvas.getWidth() / zoomLevel;
		double cell_height = canvas.getHeight() / zoomLevel;

		String[][] grid = map.getGrid();

		GraphicsContext gc = canvas.getGraphicsContext2D();

		for (int iY = coords.y; iY < coords.y + zoomLevel; iY++) {
			for (int iX = coords.x; iX < coords.x + zoomLevel; iX++) {

				try {
					gc.setFill(COLOUR_TABLE.get(grid[iX][iY]));
					gc.fillRect(((iX - coords.x) * cell_width), ((iY - coords.y) * cell_height), Math.ceil(cell_width),
							Math.ceil(cell_height));
				} catch (Exception e) {
					System.out.println("rendering went wrong");
				}
			}
		}
		
		for (Plant plant: map.getPlants()) {
			
		}

	}

	public void drawDinosaur(Dinosaur d, Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.drawImage(d.getSprite(), d.getPos().x , d.getPos().y);
	}
}

