package world;

import java.util.ArrayList;
import java.util.List;

import world.dinosaurs.Dinosaur;

public class World {

	Map map;
	List<Dinosaur> dinos;
	
	
	public World() {
		map = new Map();
		dinos = new ArrayList<Dinosaur>();
	}
	
	
	public Map getMap() {
		return map;
	}
	public List<Dinosaur> getDinos() {
		return dinos;
	}
	
}
