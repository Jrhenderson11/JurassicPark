package world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import version2.Terrain;
import version2.Terrain.Biome;
import world.plants.Bush;
import world.plants.Cactus;
import world.plants.Fir;
import world.plants.Grass;
import world.plants.Plant;
import world.plants.Plant.PlantType;

public class Map {

	Terrain terrain = new Terrain();
	List<Plant> plants;

	public Map() {
		generateNewMap("island");
		this.plants = new ArrayList<Plant>();
		// this.plants.add(new Fern(new Point.Double(250,50)));

		// generate trees using noise
		for (Point.Double spawnPoint : terrain.getTreePositions()) {
			Biome point = terrain.getBiomeLayer()[(int) spawnPoint.x][(int) spawnPoint.y];
			if (point==Biome.DESERT ||point==Biome.TEMPERATE_DESERT || point==Biome.SUBTROPICAL_DESERT) {
				this.plants.add(new Cactus(spawnPoint));
			} else {
				this.plants.add(new Fir(spawnPoint));
			}

		}
		for (Point.Double spawnPoint : terrain.getGrassPositions()) {
			this.plants.add(new Grass(spawnPoint));
		}
		for (Point.Double spawnPoint : terrain.getBushPositions()) {
			this.plants.add(new Bush(spawnPoint));
		}
	}

	public void generateNewMap(String type) {
		terrain = new Terrain(600);
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public List<Plant> getPlants() {
		return plants;
	}

	public List<Plant> getTreePlants() {
		return (List<Plant>) plants.stream().filter(x -> x.getPlantType() == PlantType.TREE);
	}

	public List<Plant> getSmallPlants() {
		return (List<Plant>) plants.stream().filter(x -> x.getPlantType() == PlantType.SMALL);
	}

	public List<Plant> getGrassPlants() {
		return (List<Plant>) plants.stream().filter(x -> x.getPlantType() == PlantType.GRASS);
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