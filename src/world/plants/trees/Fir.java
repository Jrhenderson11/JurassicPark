package world.plants.trees;

import java.awt.Point;
import java.util.Random;

import world.plants.Plant;
import world.plants.Plant.PlantType;

public class Fir extends Plant {
	
	public Fir(Point.Double newPos) {
		spriteNames = new String[] {"assets/fir1.png", "assets/fir2.png", "assets/fir3.png", "assets/fir4.png", "assets/fir5.png"};
		setup(spriteNames[new Random().nextInt(spriteNames.length)], newPos, PlantType.TREE);

	}

}
