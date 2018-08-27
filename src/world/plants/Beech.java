package world.plants;

import java.awt.Point;
import java.util.Random;

import world.plants.Plant.PlantType;

public class Beech extends Plant {
	
	public Beech(Point.Double newPos) {
		spriteNames = new String[] {"assets/beech1.png", "assets/beech2.png", "assets/beech3.png","assets/beech4.png", "assets/beech5.png", "assets/beech6.png"};
		setup(spriteNames[new Random().nextInt(spriteNames.length)], newPos, PlantType.TREE);
	}

}
