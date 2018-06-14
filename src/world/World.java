package world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import world.dinosaurs.Dinosaur;
import world.dinosaurs.Testosaurus;

public class World {

	Map map;
	List<Dinosaur> dinos;

	public World() {
		map = new Map();
		dinos = new ArrayList<Dinosaur>();
		spawnDinos();
	}

	private void spawnDinos() {

		int numDinos;
		dinos.add(new Testosaurus(new Point.Double(50, 50), this));

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

}
