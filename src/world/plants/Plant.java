package world.plants;

import java.awt.Point;

import interfaces.Drawable;
import javafx.scene.image.Image;

public class Plant extends Drawable {

	public Plant(String spritePath, Point.Double newPos) {
		this.sprite = new Image("file:"+spritePath);
		this.position = newPos;
	}
	
}
