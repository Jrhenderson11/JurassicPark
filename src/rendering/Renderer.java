package rendering;

import java.awt.Point;
import java.util.HashMap;
import java.util.function.Function;

import drawer.Gradient;
import interfaces.Drawable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import version2.Terrain;
import version2.Terrain.Biome;
import world.Map;
import world.World;
import world.dinosaurs.Dinosaur;
import world.plants.Plant;

public class Renderer {

	private enum Mode {
		TERRAIN, HEIGHTMAP, MOISTURE
	}

	private Mode mode = Mode.TERRAIN;

	private static Color SAND = Color.rgb(244, 209, 66);
	private static Color PINK = Color.rgb(186, 50, 174);
	private static Color GRASS = Color.rgb(125, 186, 81);
	private static Color BLUE = Color.rgb(50, 127, 186);
	private static Color GREY = Color.rgb(98, 102, 101);
	private static Color LIGHT_BLUE = Color.rgb(114, 219, 245);
	private static Color DARK_GREEN = Color.rgb(50, 86, 29);
	private static Color SCORCHED = Color.rgb(211, 182, 40);
	private static Color SHRUB = Color.rgb(195, 218, 66);
	private static Color TAIGA = Color.rgb(174, 218, 66);
	private static Color SAVANNAH = Color.rgb(167, 187, 41);
	private static Color DECIDUOUS = Color.rgb(61, 153, 34);
	private static Color JUNGLE = Color.rgb(43, 117, 22);
	private static Color TUNDRA = Color.rgb(163, 154, 105);

	private static HashMap<Biome, Color> BIOME_COLOUR_TABLE = new HashMap<Biome, Color>();

	public Renderer() {
		fillColourTable();
	}

	public void fillColourTable() {
		BIOME_COLOUR_TABLE.put(Biome.SEA, LIGHT_BLUE);
		BIOME_COLOUR_TABLE.put(Biome.WATER, BLUE);
		BIOME_COLOUR_TABLE.put(Biome.DESERT, SAND);
		BIOME_COLOUR_TABLE.put(Biome.TEMPERATE_DESERT, SAND);
		BIOME_COLOUR_TABLE.put(Biome.SUBTROPICAL_DESERT, SAND);
		BIOME_COLOUR_TABLE.put(Biome.SHRUBLAND, SHRUB);
		BIOME_COLOUR_TABLE.put(Biome.GRASSLAND, GRASS);
		BIOME_COLOUR_TABLE.put(Biome.SAVANNAH, SAVANNAH);
		BIOME_COLOUR_TABLE.put(Biome.TAIGA, TAIGA);
		BIOME_COLOUR_TABLE.put(Biome.FOREST, DARK_GREEN);
		BIOME_COLOUR_TABLE.put(Biome.TEMPERATE_DECIDUOUS_FOREST, DECIDUOUS);
		BIOME_COLOUR_TABLE.put(Biome.TEMPERATE_RAIN_FOREST, JUNGLE);
		BIOME_COLOUR_TABLE.put(Biome.TROPICAL_RAIN_FOREST, DARK_GREEN);
		BIOME_COLOUR_TABLE.put(Biome.TROPICAL_SEASONAL_FOREST, DECIDUOUS);
		BIOME_COLOUR_TABLE.put(Biome.BEACH, SAND);
		BIOME_COLOUR_TABLE.put(Biome.SCORCHED, SCORCHED);
		BIOME_COLOUR_TABLE.put(Biome.BARE, GREY);
		BIOME_COLOUR_TABLE.put(Biome.TUNDRA, TUNDRA);
		BIOME_COLOUR_TABLE.put(Biome.SNOW, Color.WHITE);

	}

	public void drawWorld(World world, Point coords, int zoomLevel, Canvas canvas) {

		// clear screen

		if (mode == Mode.TERRAIN) {
			drawMap(world.getMap(), coords, zoomLevel, canvas);

			for (Plant p : world.getMap().getPlants()) {
				drawDrawable(p, coords, zoomLevel, canvas);
			}
			for (Dinosaur d : world.getDinos()) {
				drawDrawable(d, coords, zoomLevel, canvas);
				if (world.isLabels()) {
					drawLabel(d, coords, zoomLevel, canvas);
				}
			}
		} else if (mode == Mode.MOISTURE) {
			drawHeightMap(world.getMap().getTerrain().getMoisture(), coords, zoomLevel, canvas);
		} else if (mode == Mode.HEIGHTMAP) {
			drawHeightMap(world.getMap().getTerrain().getElevation(), coords, zoomLevel, canvas);
		}

	}

	private void drawMap(Map map, Point coords, int zoomLevel, Canvas canvas) {
		double cell_width = canvas.getWidth() / zoomLevel;
		double cell_height = canvas.getHeight() / zoomLevel;

		Terrain terrain = map.getTerrain();

		GraphicsContext gc = canvas.getGraphicsContext2D();

		for (int iY = coords.y; iY < coords.y + zoomLevel; iY++) {
			for (int iX = coords.x; iX < coords.x + zoomLevel; iX++) {

				try {
					gc.setFill(BIOME_COLOUR_TABLE.get(terrain.getBiomeLayer()[iX][iY]));
					gc.fillRect(((iX - coords.x) * cell_width), ((iY - coords.y) * cell_height), Math.ceil(cell_width),
							Math.ceil(cell_height));
				} catch (Exception e) {
					// System.out.println("rendering went wrong");
				}
			}
		}
	}

	private void drawHeightMap(double[][] map, Point coords, int zoomLevel, Canvas canvas) {
		double cell_width = canvas.getWidth() / zoomLevel;
		double cell_height = canvas.getHeight() / zoomLevel;
		Function<Double, double[]> grad = Gradient
				.gradient_func(new double[][] { { 0, 0, 255 }, { 0, 255, 255 }, { 255, 255, 0 }, { 255, 0, 0 } });

		GraphicsContext gc = canvas.getGraphicsContext2D();

		for (int iY = coords.y; iY < coords.y + zoomLevel; iY++) {
			for (int iX = coords.x; iX < coords.x + zoomLevel; iX++) {

				try {
					double[] test = grad.apply((Double) map[iX][iY]);
					// System.out.println(test[0] + ", " + test[1] + ", " + test[2]);

					gc.setFill(Color.rgb((int) test[0], (int) test[1], (int) test[2]));

					gc.fillRect(((iX - coords.x) * cell_width), ((iY - coords.y) * cell_height), Math.ceil(cell_width),
							Math.ceil(cell_height));
				} catch (Exception e) {
					// System.out.println("rendering went wrong");
				}
			}
		}
	}
	
	public void drawDrawable(Drawable d, Point coords, int zoomLevel, Canvas canvas) {
		// System.out.println("render dinosaur");
		double cellWidth = canvas.getWidth() / zoomLevel;
		double cellHeight = canvas.getHeight() / zoomLevel;

		double imgWidth = d.getSprite().getWidth() / zoomLevel * 40;
		double imgHeight = d.getSprite().getHeight() / zoomLevel * 40;

		if (d.getPos().x > coords.x && d.getPos().x < coords.x + zoomLevel && d.getPos().y > coords.y
				&& d.getPos().y < coords.y + zoomLevel) {
			GraphicsContext gc = canvas.getGraphicsContext2D();
			if (d.getDirection() == 1) {
				gc.drawImage(d.getSprite(), (d.getPos().x - coords.x) * cellWidth - (imgWidth / 2),
						(d.getPos().y - coords.y) * cellHeight - (imgHeight / 2), imgWidth, imgHeight);

			} else if (d.getDirection() == -1) {
				gc.drawImage(d.getSprite(), (d.getPos().x - coords.x) * cellWidth + (imgWidth / 2),
						(d.getPos().y - coords.y) * cellHeight - (imgHeight / 2), -imgWidth, imgHeight);

			}
		}
	}

	public void drawLabel(Dinosaur d, Point coords, int zoomLevel, Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();

		if (d.getPos().x > coords.x && d.getPos().x < coords.x + zoomLevel && d.getPos().y > coords.y
				&& d.getPos().y < coords.y + zoomLevel) {
			double cellWidth = canvas.getWidth() / zoomLevel;
			double cellHeight = canvas.getHeight() / zoomLevel;
			double imgWidth = d.getSprite().getWidth() / zoomLevel * 40;
			double imgHeight = d.getSprite().getHeight() / zoomLevel * 40;

			Point.Double dinoPos = new Point.Double((d.getPos().x - coords.x) * cellWidth,
					(d.getPos().y - coords.y) * cellHeight);
			gc.setTextAlign(TextAlignment.CENTER);

			gc.setFill(Color.RED);

			// name
			gc.setFont(Font.font("Nimbus Mono L", FontWeight.BOLD, 12));
			gc.fillText(d.getName(), dinoPos.x, dinoPos.y - (imgHeight / 2 + (13 * 3)));

			// activity
			gc.setFont(Font.font("Nimbus Mono L", FontWeight.NORMAL, 12));
			gc.setFill(Color.RED);
			gc.fillText(d.getActivity().toString(), dinoPos.x, dinoPos.y - (imgHeight / 2 + (13 * 2)));

			// mood
			gc.setFont(Font.font("Nimbus Mono L", FontWeight.NORMAL, 12));
			gc.setFill(Color.RED);
			gc.fillText(d.getMood().toString(), dinoPos.x, dinoPos.y - (imgHeight / 2 + (13 * 1)));
		}
	}

	public void switchMode() {
		if (mode==Mode.TERRAIN) {
			mode=Mode.HEIGHTMAP;
		} else if (mode==Mode.HEIGHTMAP){
			mode=Mode.MOISTURE;
		} else {
			mode = Mode.TERRAIN;
		}
	}
}
