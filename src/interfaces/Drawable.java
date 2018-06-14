package interfaces;

import java.awt.Point;

import javafx.scene.image.Image;

public class Drawable {

	protected Image sprite;// = new Image("assets/unkown.bmp");
	protected Point.Double position;
	
	public Image getSprite() {
		return this.sprite;
	}

	public Point.Double getPos() {
		return this.position;
	}

}