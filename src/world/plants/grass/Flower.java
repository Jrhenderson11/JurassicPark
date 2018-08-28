package world.plants.grass;

import java.awt.Point;
import java.util.Random;

import world.plants.Plant;
import world.plants.Plant.PlantType;

public class Flower extends Plant {
	
	public Flower(Point.Double newPos) {
		spriteNames = new String[] {"assets/flower1.png", "assets/flower2.png"};
		setup(spriteNames[new Random().nextInt(spriteNames.length)], newPos, PlantType.SMALL);
	}

}
