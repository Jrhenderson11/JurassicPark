package world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utils.RandomUtils;
import version2.Terrain.Biome;
import world.dinosaurs.Dinosaur;
import world.dinosaurs.Stegosaurus;
import world.dinosaurs.ceratopsids.Triceratops;

public class World {

	Map map;
	List<Dinosaur> dinos;
	private boolean labels = false;

	public World() {
		map = new Map();
		dinos = new ArrayList<Dinosaur>();
		spawnDinos();
	}

	private void spawnDinos() {

		int numDinos = RandomUtils.randomPosGaussian(6, 2);

		for (int i = 0; i < numDinos; i++) {

			int x = RandomUtils.randomInt(map.getWidth(), 0);
			int y = RandomUtils.randomInt(map.getWidth(), 0);

			while (map.getPos(x, y) == Biome.SHALLOW_SEA || map.getPos(x, y) == Biome.SEA) {
				x = RandomUtils.randomInt(map.getWidth(), 0);
				y = RandomUtils.randomInt(map.getWidth(), 0);
			}
			switch (new Random().nextInt(2)) {
				case 0:
					dinos.add(new Triceratops(new Point.Double(x, y), this));
					break;
				case 1:
					dinos.add(new Stegosaurus(new Point.Double(x, y), this));
					break;
			}

			System.out.println("Dinosaur spawned: " + new Point.Double(x, y));

		}

	}

	public Map getMap() {
		return map;
	}

	public List<Dinosaur> getDinos() {
		return dinos;
	}

	public void update() {
		for (Dinosaur d : dinos) {
			d.update();
		}
	}

	public boolean isLabels() {
		return labels;
	}

	public void setLabels(boolean labels) {
		this.labels = labels;
	}

}
