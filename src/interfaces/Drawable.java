package interfaces;

import java.awt.Point;

import org.newdawn.slick.Image;

public class Drawable {

	protected Image sprite;// = new Image("assets/unkown.bmp");
	protected Point.Double position;
	protected int direction =1;
	
	public Image getSprite() {
		return this.sprite;
	}

	public Point.Double getPos() {
		return this.position;
	}

	public int getDirection() {
		return this.direction;
	}
}
