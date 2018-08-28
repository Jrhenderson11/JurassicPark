package world.plants.trees;

import java.awt.Point;
import java.util.Random;

import world.plants.Plant;
import world.plants.Plant.PlantType;

public class Cactus extends Plant{

	public Cactus(Point.Double newPos) {
		spriteNames = new String[] {"assets/cactus1.png"};
		setup(spriteNames[new Random().nextInt(spriteNames.length)], newPos, PlantType.SMALL);
	}
}
