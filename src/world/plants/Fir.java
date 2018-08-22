package world.plants;

import java.awt.Point;
import java.util.Random;

public class Fir extends Plant {
	
	public Fir(Point.Double newPos) {
		spriteNames = new String[] {"assets/fir1.png"};
		setup(spriteNames[new Random().nextInt(spriteNames.length)], newPos);

	}

}
