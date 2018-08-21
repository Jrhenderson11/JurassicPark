package world.plants;

import java.awt.Point;
import java.util.Random;

public class Grass extends Plant {
	
	public Grass(Point.Double newPos) {
		switch (new Random().nextInt(2)) {
		case 1:
			setup("assets/grass1.png", newPos);			
			break;
			
		case 2:
			setup("assets/grass2.png", newPos);			
			break;
			
		default:
			setup("assets/grass3.png", newPos);
			break;
		}

	}

}
