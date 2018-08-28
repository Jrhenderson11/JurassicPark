package world.plants.grass;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

import world.plants.Plant;
import world.plants.Plant.PlantType;

public class Grass extends Plant {

	public Grass(Point.Double newPos) {
		spriteNames = new String[] {"assets/grass1.png", "assets/grass2.png", "assets/grass3.png"};
		setup(spriteNames[new Random().nextInt(spriteNames.length)], newPos, PlantType.GRASS);
	}

}
