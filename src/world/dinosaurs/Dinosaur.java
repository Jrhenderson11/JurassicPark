package world.dinosaurs;

import java.awt.Point;

import interfaces.Drawable;
import javafx.scene.image.Image;

public class Dinosaur extends Drawable {

	Point.Double position;
	String name;

	public Dinosaur(String newName, Point.Double newPos, String spritePath) {
		this.name = newName;
		this.position = newPos;
		try {
			this.sprite = new Image(spritePath);
		} catch (Exception e) {
			System.out.println("could not load sprite for " + name);
		}
	}

	public Point.Double getPos() {
		return this.position;
	}

}
