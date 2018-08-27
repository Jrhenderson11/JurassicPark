package world.plants;

import java.awt.Point;
import java.util.Random;

public class Flower extends Plant {
	
	public Flower(Point.Double newPos) {
		spriteNames = new String[] {"assets/flower1.png", "assets/flower2.png"};
		setup(spriteNames[new Random().nextInt(spriteNames.length)], newPos, PlantType.SMALL);
	}

}
