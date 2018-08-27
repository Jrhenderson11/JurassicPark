package world.plants;

import java.awt.Point;
import java.util.Random;

import world.plants.Plant.PlantType;

public class Beech extends Plant {
	
	public Beech(Point.Double newPos) {
		spriteNames = new String[] {"assets/beech1.png"};
		setup(spriteNames[new Random().nextInt(spriteNames.length)], newPos, PlantType.TREE);
	}

}
