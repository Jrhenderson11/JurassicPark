package world;

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
		dinos.add(new Testosaurus(this));
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
