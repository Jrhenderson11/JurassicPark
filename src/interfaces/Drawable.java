package interfaces;

import javafx.scene.image.Image;

public class Drawable {

	protected Image sprite = new Image("assets/unkown.bmp");
	
	public Image getSprite() {
		return this.sprite;
	}
}
