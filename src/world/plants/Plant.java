package world.plants;

import java.awt.Point;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import interfaces.Drawable;

public class Plant extends Drawable {

	public Plant(String spritePath, Point.Double newPos) {
		try {
			this.sprite = new Image(spritePath);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.position = newPos;
	}
	
}
