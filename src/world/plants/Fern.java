package world.plants;

import java.awt.Point;
import java.util.Random;

import world.plants.Plant.PlantType;

public class Fern extends Plant {
	
	public Fern(Point.Double newPos) {
		spriteNames = new String[] {"assets/fern.png","assets/fern2.png"};
		setup(spriteNames[new Random().nextInt(spriteNames.length)], newPos, PlantType.SMALL);
	}

}
