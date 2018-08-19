package world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import version2.Terrain;
import version2.Terrain.Biome;
import world.plants.Fern;
import world.plants.Plant;

public class Map {
	
	Terrain terrain = new Terrain();
	List<Plant> plants;

	public Map() {
		generateNewMap("island");
		this.plants = new ArrayList<Plant>();
		//this.plants.add(new Fern(new Point.Double(250,50)));
		//generate trees using noise
		for (Point.Double spawnPoint : terrain.getTreePositions()) {
			this.plants.add(new Fern(spawnPoint));
		}
	}
	
	public void generateNewMap(String type) {
		terrain = new Terrain();
	}

	public Terrain getTerrain() {
		return terrain;
	}
	
	public List<Plant> getPlants() {
		return plants;
	}
	
	public int getWidth() {
		return this.terrain.getSize();
	}
	
	public Biome getPos(Point.Double p) {
		return terrain.getBiomeLayer()[(int) p.x][(int) p.y];
	}
	
	public Biome getPos(double x, double y) {
		return terrain.getBiomeLayer()[(int) x][(int) y];
	}
	
	public double getPosElev(Point.Double p) {
		return terrain.getElevation()[(int) p.x][(int) p.y];
	}
	
	public double getPosElev(double x, double y) {
		return terrain.getElevation()[(int) x][(int) y];
	}
	
}