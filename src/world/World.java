package world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import utils.RandomUtils;
import world.dinosaurs.Dinosaur;
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

		int numDinos = 1;
		for (int i =0; i< numDinos;i++) {
			
			int x = RandomUtils.randomInt(300, 0);					
			int y = RandomUtils.randomInt(300, 0);		
			
			while (map.getPos(x, y).equals("x") || map.getPos(x, y).equals("w")) {
				x = RandomUtils.randomInt(300, 0);					
				y = RandomUtils.randomInt(300, 0);		
			}
			dinos.add(new Triceratops(new Point.Double(x, y), this));
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
