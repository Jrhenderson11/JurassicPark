package world;

import java.util.ArrayList;
import java.util.List;

import landscapes.Original;
import world.plants.Plant;

public class Map {

	
	String[][] grid;
	List<Plant> plants;

	public Map() {
		generateNewMap("normal");
		this.plants = new ArrayList<Plant>();
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

}
