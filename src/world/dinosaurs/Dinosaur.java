package world.dinosaurs;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import ai.AStar;
import interfaces.Drawable;
import javafx.scene.image.Image;
import world.World;
import world.plants.Plant;

public class Dinosaur extends Drawable {

	private String name;

	public enum DIET {
		CARNIVORE, HERBIVORE, OMNIVORE
	}

	public enum ACTIVITY {
		SLEEPING, CHILLING, EATING, DRINKING, HUNTING, HUNTING_PLANT, MOVING
	}

	private DIET diet;
	private ACTIVITY activity;
	private World world;
	private List<Point.Double> path;
	private int pathIndex;
	
	
	private double speed = 1;

	public Dinosaur(String newName, Point.Double newPos, String spritePath, World brave, DIET foodType) {
		this.name = newName;
		this.position = newPos;
		try {
			// System.out.println(spritePath);
			this.sprite = new Image("file:" + spritePath);
		} catch (Exception e) {
			System.out.println("could not load sprite for " + name);
		}
		this.world = brave;
		this.path = new ArrayList<Point.Double>();
		this.activity = ACTIVITY.CHILLING;
		this.diet = foodType;

	}

	public void setDiet(DIET newDiet) {
		this.diet = newDiet;
	}

	// will have unkown sprite
	public Dinosaur(String newName, Point.Double newPos) {
		this.name = newName;
		this.position = newPos;
	}

	public void update() {
		if (activity == ACTIVITY.MOVING || activity == ACTIVITY.HUNTING_PLANT) {
			for (int i = 0; i < speed; i++) {
				if (!path.isEmpty() && pathIndex < path.size()) {
			
					Point.Double next = path.get(pathIndex);
					//System.out.println("MOVING TO " + next.x + ", " + next.y);
					
					// TODO: smooth movement
					this.move(next);
					path.remove(pathIndex);
					pathIndex++;
				}
			}
		} else if (activity == ACTIVITY.CHILLING) {
			// TEST
			// trigger hunger, search for food, plot path and start moving
			System.out.println("HUNTING PLANT");
			this.activity = ACTIVITY.HUNTING_PLANT;
			this.path = (new AStar(this.position, findNearestFood(), this.world.getMap()).getPath());
			System.out.println("length: " + path.size());
			pathIndex = 0;
		}

	}

	public Point.Double findNearestFood() {
		Point.Double foodPos = null;
		if (diet == DIET.HERBIVORE) {
			// find plant
			double dist = Integer.MAX_VALUE;
			for (Plant p : world.getMap().getPlants()) {
				if (p.getPos().distance(this.position) < dist) {
					foodPos = p.getPos();
				}
			}
		}
		System.out.println("nearest plant to " + name + " is at " + foodPos.x + ", " + foodPos.y);
		return foodPos;
	}

	private void move(double dX, double dY) {
		this.position.setLocation(this.position.x + dX, this.position.y + dY);
	}

	private void move(Point.Double p) {
		this.position = p;
	}

}
