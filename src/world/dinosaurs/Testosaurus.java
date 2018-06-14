package world.dinosaurs;

import java.awt.Point;

import world.World;

public class Testosaurus extends Dinosaur {

	public Testosaurus(World newWorld) {
		super("testosaurus", new Point.Double(50, 50), "/home/james/Documents/git/JurassicPark/assets/testosaurus.png", newWorld, DIET.HERBIVORE);
		
	}
}
