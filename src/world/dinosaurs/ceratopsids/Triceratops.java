package world.dinosaurs.ceratopsids;

import java.awt.Point;

import world.World;
import world.dinosaurs.Dinosaur;
import world.dinosaurs.Dinosaur.DIET;

public class Triceratops extends Dinosaur {

	public Triceratops(Point.Double newPos, World newWorld) {
		super("triceratops", newPos, "assets/triceratops_large.png", newWorld, DIET.HERBIVORE);
		
	}
}
