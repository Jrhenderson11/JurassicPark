package world.plants;

import java.awt.Point;
import java.util.List;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import interfaces.Drawable;

public class Plant extends Drawable {

	public enum PlantType {GRASS, SMALL, TREE};
	
	private PlantType plantType;
	
	protected String[] spriteNames;
	
	public Plant() {}
	public Plant(String spritePath, Point.Double newPos) {
		try {
			this.sprite = new Image(spritePath);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.position = newPos;
	}
	
	public void setup(String spritePath, Point.Double newPos, PlantType newPlantType) {
		try {
			this.sprite = new Image(spritePath);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.position = newPos;
		this.plantType = newPlantType;
	}
	
	public PlantType getPlantType() {
		return this.plantType;
	}
	
}
