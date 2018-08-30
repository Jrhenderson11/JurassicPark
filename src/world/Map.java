package world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import version2.Terrain;
import version2.Terrain.Biome;
import world.plants.Plant;
import world.plants.Plant.PlantType;
import world.plants.grass.Grass;
import world.plants.smallplants.Bush;
import world.plants.smallplants.Fern;
import world.plants.trees.Beech;
import world.plants.trees.Cactus;
import world.plants.trees.Fir;

public class Map {

	Terrain terrain = new Terrain();
	List<Plant> plants;

	public Map() {
		generateNewMap("island");
		this.plants = new ArrayList<Plant>();
		// this.plants.add(new Fern(new Point.Double(250,50)));

		// generate trees using noise
		for (Point.Double spawnPoint : terrain.getTreePositions()) {
			Biome point = getPos(spawnPoint);
			if (point == Biome.DESERT || point == Biome.TEMPERATE_DESERT || point == Biome.SUBTROPICAL_DESERT) {
				this.plants.add(new Cactus(spawnPoint));
			} else if (point == Biome.DRIED_MUD || point == Biome.TUNDRA || point == Biome.SHRUBLAND) {
				this.plants.add(new Beech(spawnPoint));
			} else {
				this.plants.add(new Fir(spawnPoint));
			}
		}

		for (Point.Double spawnPoint : terrain.getGrassPositions()) {
			this.plants.add(new Grass(spawnPoint));
		}
		for (Point.Double spawnPoint : terrain.getBushPositions()) {
			Biome point = getPos(spawnPoint);
			if (point == Biome.FERNLAND) {
				this.plants.add(new Fern(spawnPoint));
			} else {
				this.plants.add(new Bush(spawnPoint));
			}
		}

		// sort all plants and bushes
		this.plants.sort(new Comparator<Plant>() {
			@Override
			public int compare(Plant arg0, Plant arg1) {
				if (arg0.getPos().y > arg1.getPos().y) {
					return 1;
				} else if (arg0.getPos().y == arg1.getPos().y) {
					return 0;
				} else {
					return -1;
				}
			}
		});

	}

	public void generateNewMap(String type) {
		terrain = new Terrain(600);
		terrain.setWaterDistMap(terrain.generateWaterDistMap());
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public List<Plant> getPlants() {
		return plants;
	}

	public List<Plant> getTreePlants() {
		return (plants.stream().filter(x -> x.getPlantType() == PlantType.TREE)).collect(Collectors.toList());
	}

	public List<Plant> getSmallPlants() {
		return plants.stream().filter(x -> x.getPlantType() == PlantType.SMALL).collect(Collectors.toList());
	}

	public List<Plant> getGrassPlants() {
		return plants.stream().filter(x -> x.getPlantType() == PlantType.GRASS).collect(Collectors.toList());
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

	public double getPosWaterDist(Point.Double p) {
		return terrain.getWaterDistMap()[(int) p.x][(int) p.y];
	}
}