package rendering;

import java.awt.Point;
import java.util.HashMap;
import java.util.function.Function;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import drawer.Gradient;
import interfaces.Drawable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

	private static Color SAND = new Color(244f, 209f, 66f);
	private static Color DARK_SAND = new Color(221f, 186f, 39f);
	private static Color PINK = new Color(186f, 50f, 174f);
	private static Color GRASS = new Color(125f, 186f, 81f);
	private static Color BLUE = new Color(50f, 127f, 186f);
	private static Color GREY = new Color(98f, 102f, 101f);
	private static Color LIGHT_BLUE = new Color(114f, 219f, 245f);
	private static Color DARK_GREEN = new Color(50f, 86f, 29f);
	private static Color SCORCHED = new Color(211f, 182f, 40f);
	private static Color SHRUB = new Color(195f, 218f, 66f);
	private static Color TAIGA = new Color(174f, 218f, 66f);
	private static Color SAVANNAH = new Color(167f, 187f, 41f);
	private static Color DECIDUOUS = new Color(61f, 153f, 34f);
	private static Color RAINFOREST = new Color(17f, 109f, 26f);
	private static Color JUNGLE = new Color(43f, 117f, 22f);
	private static Color TUNDRA = new Color(163f, 154f, 105f);
	private static Color WHITE = new Color(255f, 255f, 255f);

	private static HashMap<Biome, Color> BIOME_COLOUR_TABLE = new HashMap<Biome, Color>();

	public Renderer() {
		fillColourTable();
	}

	public void fillColourTable() {
		BIOME_COLOUR_TABLE.put(Biome.SEA, BLUE);
		BIOME_COLOUR_TABLE.put(Biome.SHALLOW_SEA, LIGHT_BLUE);
		BIOME_COLOUR_TABLE.put(Biome.WATER, BLUE);
		BIOME_COLOUR_TABLE.put(Biome.DESERT, SAND);
		BIOME_COLOUR_TABLE.put(Biome.TEMPERATE_DESERT, DARK_SAND);
		BIOME_COLOUR_TABLE.put(Biome.SUBTROPICAL_DESERT, SAND);
		BIOME_COLOUR_TABLE.put(Biome.SHRUBLAND, SHRUB);
		BIOME_COLOUR_TABLE.put(Biome.GRASSLAND, GRASS);
		BIOME_COLOUR_TABLE.put(Biome.SAVANNAH, SAVANNAH);
		BIOME_COLOUR_TABLE.put(Biome.TAIGA, TAIGA);
		BIOME_COLOUR_TABLE.put(Biome.FOREST, DARK_GREEN);
		BIOME_COLOUR_TABLE.put(Biome.TEMPERATE_DECIDUOUS_FOREST, DECIDUOUS);
		BIOME_COLOUR_TABLE.put(Biome.TEMPERATE_RAIN_FOREST, JUNGLE);
		BIOME_COLOUR_TABLE.put(Biome.TROPICAL_RAIN_FOREST, DARK_GREEN);
		BIOME_COLOUR_TABLE.put(Biome.TROPICAL_SEASONAL_FOREST, RAINFOREST);
		BIOME_COLOUR_TABLE.put(Biome.BEACH, SAND);
		BIOME_COLOUR_TABLE.put(Biome.SCORCHED, SCORCHED);
		BIOME_COLOUR_TABLE.put(Biome.BARE, GREY);
		BIOME_COLOUR_TABLE.put(Biome.TUNDRA, TUNDRA);
		BIOME_COLOUR_TABLE.put(Biome.SNOW, WHITE);
		//normalise
		for (Biome b: BIOME_COLOUR_TABLE.keySet()) {
			Color c = BIOME_COLOUR_TABLE.get(b);
			BIOME_COLOUR_TABLE.put(b,new Color(c.r/255, c.g/255, c.b/255));
		}

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
					//drawLabel(d, coords, zoomLevel, canvas);
				}
			}
		} else if (mode == Mode.MOISTURE) {
			drawHeightMap(world.getMap().getTerrain().getMoisture(), coords, zoomLevel, canvas);
		} else if (mode == Mode.HEIGHTMAP) {
			drawHeightMap(world.getMap().getTerrain().getElevation(), coords, zoomLevel, canvas);
		}

	}

	public void drawWorldSlick(World world, Point coords, int zoomLevel, GameContainer container, Graphics g) {

		// clear screen
		if (mode == Mode.TERRAIN) {
			drawMapSlick(world.getMap(), coords, zoomLevel, container, g);

			for (Plant p : world.getMap().getPlants()) {
				drawDrawableSlick(p, coords, zoomLevel, container, g);
			}
			for (Dinosaur d : world.getDinos()) {
				drawDrawableSlick(d, coords, zoomLevel, container, g);
				if (world.isLabels()) {
					drawLabelSlick(d, coords, zoomLevel, container, g);
				}
			}
		} else if (mode == Mode.MOISTURE) {
			drawHeightMapSlick(world.getMap().getTerrain().getMoisture(), coords, zoomLevel, container, g);
		} else if (mode == Mode.HEIGHTMAP) {
			drawHeightMapSlick(world.getMap().getTerrain().getElevation(), coords, zoomLevel, container, g);
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
					//gc.setFill(BIOME_COLOUR_TABLE.get(terrain.getBiomeLayer()[iX][iY]));
					gc.fillRect(((iX - coords.x) * cell_width), ((iY - coords.y) * cell_height), Math.ceil(cell_width),
							Math.ceil(cell_height));
				} catch (Exception e) {
					// System.out.println("rendering went wrong");
				}
			}
		}
	}

	private void drawMapSlick(Map map, Point coords, int zoomLevel, GameContainer container, Graphics g) {
		float cell_width = container.getWidth() / (float) zoomLevel;
		float cell_height = container.getHeight() / (float) zoomLevel;
		
		// draw & fill
		Terrain terrain = map.getTerrain();

		for (int iY = coords.y; iY < coords.y + zoomLevel; iY++) {
			for (int iX = coords.x; iX < coords.x + zoomLevel; iX++) {
				try {
					g.setColor(BIOME_COLOUR_TABLE.get(terrain.getBiomeLayer()[iX][iY]));
					
					float x = (float) ((iX - coords.x) * cell_width);
					float y = (float) ((iY - coords.y) * cell_height);
					float width = (float) (cell_width);
					float height =(float) (cell_height); 
					Rectangle rect = new Rectangle(x,y, width, height);

					//g.draw(rect);
					g.fill(rect);
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

					//gc.setFill(Color.rgb((int) test[0], (int) test[1], (int) test[2]));

					gc.fillRect(((iX - coords.x) * cell_width), ((iY - coords.y) * cell_height), Math.ceil(cell_width),
							Math.ceil(cell_height));
				} catch (Exception e) {
					// System.out.println("rendering went wrong");
				}
			}
		}
	}

	private void drawHeightMapSlick(double[][] map, Point coords, int zoomLevel, GameContainer container, Graphics g) {
		float cell_width = container.getWidth() / (float) zoomLevel;
		float cell_height = container.getHeight() / (float) zoomLevel;
		Function<Double, double[]> grad = Gradient
				.gradient_func(new double[][] { { 0, 0, 255 }, { 0, 255, 255 }, { 255, 255, 0 }, { 255, 0, 0 } });

		for (int iY = coords.y; iY < coords.y + zoomLevel; iY++) {
			for (int iX = coords.x; iX < coords.x + zoomLevel; iX++) {

				try {
					double[] test = grad.apply((Double) map[iX][iY]);
					// System.out.println(test[0] + ", " + test[1] + ", " + test[2]);

					g.setColor(new Color((float) test[0], (float) test[1], (float) test[2]));
					//gc.fillRect(((iX - coords.x) * cell_width), ((iY - coords.y) * cell_height), Math.ceil(cell_width),Math.ceil(cell_height));
					Rectangle rect = new Rectangle((float) ((iX - coords.x) * cell_width),
							(float) ((iY - coords.y) * cell_height), (float) Math.ceil(cell_width),
							(float) Math.ceil(cell_height));
					
					g.draw(rect);
					g.fill(rect);
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
				//.drawImage(d.getSprite(), (d.getPos().x - coords.x) * cellWidth - (imgWidth / 2), (d.getPos().y - coords.y) * cellHeight - (imgHeight / 2), imgWidth, imgHeight);

			} else if (d.getDirection() == -1) {
				//gc.drawImage(d.getSprite(), (d.getPos().x - coords.x) * cellWidth + (imgWidth / 2),(d.getPos().y - coords.y) * cellHeight - (imgHeight / 2), -imgWidth, imgHeight);

			}
		}
	}

	public void drawDrawableSlick(Drawable d, Point coords, int zoomLevel, GameContainer container, Graphics g) {
		// System.out.println("render dinosaur");
		float cellWidth = container.getWidth() / (float) zoomLevel;
		float cellHeight = container.getHeight() / (float) zoomLevel;

		double imgWidth = d.getSprite().getWidth() / zoomLevel * 40;
		double imgHeight = d.getSprite().getHeight() / zoomLevel * 40;

		Image sprite = d.getSprite().getScaledCopy((1 / ((float) zoomLevel))*40 );

		if (d.getPos().x > coords.x && d.getPos().x < coords.x + zoomLevel && d.getPos().y > coords.y
				&& d.getPos().y < coords.y + zoomLevel) {
			if (d.getDirection() == 1) {
			} else if (d.getDirection() == -1) {
				sprite = sprite.getFlippedCopy(true, false);
			}
			g.drawImage(sprite,(float) ((d.getPos().x - coords.x) * cellWidth + (imgWidth / 2)),(float)	((d.getPos().y - coords.y) * cellHeight - (imgHeight / 2)));

		}
	}

	public void drawLabelSlick(Dinosaur d, Point coords, int zoomLevel, GameContainer container, Graphics g) {
		
		if (d.getPos().x > coords.x && d.getPos().x < coords.x + zoomLevel && d.getPos().y > coords.y
				&& d.getPos().y < coords.y + zoomLevel) {
			float cellWidth = container.getWidth() / (float) zoomLevel;
			float cellHeight = container.getHeight() / (float) zoomLevel;
			double imgWidth = d.getSprite().getWidth() / zoomLevel * 40;
			double imgHeight = d.getSprite().getHeight() / zoomLevel * 40;

			Point.Double dinoPos = new Point.Double((d.getPos().x - coords.x) * cellWidth,
					(d.getPos().y - coords.y) * cellHeight);
			//gc.setTextAlign(TextAlignment.CENTER);

			g.setColor(new Color(255f,0f,0f));

			// name
			//gc.setFont(Font.font("Nimbus Mono L", FontWeight.BOLD, 12));
			//gc.fillText();
			g.drawString(d.getName(),(float) dinoPos.x, (float) (dinoPos.y - (imgHeight / 2 + (13 * 3))));
			
			// activity
			//gc.setFont(Font.font("Nimbus Mono L", FontWeight.NORMAL, 12));
			//gc.setFill(Color.RED);
			//gc.fillText(d.getActivity().toString(), dinoPos.x, dinoPos.y - (imgHeight / 2 + (13 * 2)));
			g.drawString(d.getActivity().toString(),(float) dinoPos.x, (float) (dinoPos.y - (imgHeight / 2 + (13 * 2))));
			// mood
			//gc.setFont(Font.font("Nimbus Mono L", FontWeight.NORMAL, 12));
			//gc.setFill(Color.RED);
			//gc.fillText(d.getMood().toString(), dinoPos.x, dinoPos.y - (imgHeight / 2 + (13 * 1)));
			g.drawString(d.getMood().toString(),(float) dinoPos.x, (float) (dinoPos.y - (imgHeight / 2 + (13 * 1))));

		}
	}

	public void switchMode() {
		if (mode == Mode.TERRAIN) {
			mode = Mode.HEIGHTMAP;
		} else if (mode == Mode.HEIGHTMAP) {
			mode = Mode.MOISTURE;
		} else {
			mode = Mode.TERRAIN;
		}
	}
}
