package world.plants;

import java.awt.Point;
import java.util.Random;

import world.plants.Plant.PlantType;

public class Fern extends Plant {
	
	public Fern(Point.Double newPos) {
		spriteNames = new String[] {"assets/fern.png","assets/fern2.png","assets/fern3.png","assets/fern5.png","assets/fern6.png","assets/fern7.png","assets/fern8.png","assets/fern9.png","assets/fern10.png"};
		setup(spriteNames[new Random().nextInt(spriteNames.length)], newPos, PlantType.SMALL);
	}

}
