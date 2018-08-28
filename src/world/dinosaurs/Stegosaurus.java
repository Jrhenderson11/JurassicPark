package world.dinosaurs;

import java.awt.Point;

import world.World;
import world.dinosaurs.Dinosaur;
import world.dinosaurs.Dinosaur.DIET;

public class Stegosaurus extends Dinosaur {

	public Stegosaurus(Point.Double newPos, World newWorld) {
		super("stegosaurus", newPos, "assets/stegosaurus.png", newWorld, DIET.HERBIVORE);
		
	}
}
