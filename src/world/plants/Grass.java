package world.plants;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

public class Grass extends Plant {

	public Grass(Point.Double newPos) {
		spriteNames = new String[] {"assets/grass1.png", "assets/grass2.png", "assets/grass3.png"};
		setup(spriteNames[new Random().nextInt(spriteNames.length)], newPos);
	}

}
