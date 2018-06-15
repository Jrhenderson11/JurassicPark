package world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import landscapes.Original;
import world.plants.Fern;
import world.plants.Plant;

public class Map {

	
	String[][] grid;
	List<Plant> plants;

	public Map() {
		generateNewMap("normal");
		this.plants = new ArrayList<Plant>();
		this.plants.add(new Fern(new Point.Double(250,50)));
	}
	
	public void generateNewMap(String type) {
		Original map = new Original(type);
		map.make();
		grid = map.getGrid();
	}

	public String[][] getGrid() {
		return grid;
	}
	public List<Plant> getPlants() {
		return plants;
	}
	
	public int getWidth() {
		return this.grid[0].length;		
	}
	
	public int getHeight() {
		return this.grid.length;
	}

	public String getPos(Point.Double p) {
		return grid[(int) p.x][(int) p.y];
	}
	
	public String getPos(double x, double y) {
		return grid[(int) x][(int) y];
	}
	
}
