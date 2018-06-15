package world.dinosaurs;

import java.awt.Point;

import world.World;

public class Triceratops extends Dinosaur {

	public Triceratops(Point.Double newPos, World newWorld) {
		super("triceratops", newPos, "assets/triceratops_large.png", newWorld, DIET.HERBIVORE);
		
	}
}
