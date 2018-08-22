package world.plants;

import java.awt.Point;
import java.util.Random;

public class Bush extends Plant {
	
	public Bush(Point.Double newPos) {
		spriteNames = new String[] {"assets/bush1.png", "assets/bush2.png", "assets/bush3.png"};
		setup(spriteNames[new Random().nextInt(spriteNames.length)], newPos);
	}

}
