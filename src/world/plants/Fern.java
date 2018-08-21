package world.plants;

import java.awt.Point;
import java.util.Random;

public class Fern extends Plant {
	
	public Fern(Point.Double newPos) {
		switch (new Random().nextInt(2)) {
		case 1:
			setup("assets/fern.png", newPos);			
			break;

		default:
			setup("assets/fern2.png", newPos);
			break;
		}

	}

}
