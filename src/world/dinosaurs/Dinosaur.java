package world.dinosaurs;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import ai.AStar;
import ai.Translations;
import heightmaps.generators.RandomUtils;
import interfaces.Drawable;
import javafx.scene.image.Image;
import javafx.util.Pair;
import world.World;
import world.plants.Plant;

public class Dinosaur extends Drawable {

	private String name;

	public enum DIET {
		CARNIVORE, HERBIVORE, OMNIVORE
	}

	public enum ACTIVITY {
		SLEEPING, CHILLING, EATING, DRINKING, HUNTING, HUNTING_PLANT, HUNTING_WATER, MOVING
	}

	public enum MOOD {
		HAPPY, EXCITED, CONTENT, SAD, RESTLESS, ANGRY
	}

	private DIET diet;
	private ACTIVITY activity;
	private MOOD mood;

	private double MAXHUNGER = 200;
	private double MAXTHIRST = 200;
	private double hunger = 0;
	private double thirst = 0;

	private World world;
	private List<Point.Double> path;
	private int pathIndex;

	private double PURPOSEFUL_SPEED = 0.1;
	private double LAZY_SPEED = 0.05;
	
	private double speed = LAZY_SPEED;

	private int waitCounter=0;
	private int waitTime;
	
	
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
		this.mood = MOOD.CONTENT;

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
		//System.out.println("activity: " + activity);
		switch (activity) {
		case CHILLING:
			if (mood == MOOD.SAD) {
				// sit still :(
				
			} else {
				// move around a bit
				if (path.isEmpty() || path == null || (position.equals(path.get(path.size()-1)))) {
					//wait random amount 
					if (waitCounter==0) {
						this.waitTime = RandomUtils.randomGaussian(1000, 750);
					}
					System.out.println("CHILLING: " + waitCounter +" / "+ waitTime);
					
					
					if (waitCounter++ == waitTime) {
						//make new path
						this.speed = LAZY_SPEED;
						waitCounter=0;
						
						int dX = RandomUtils.randomInt(20, -20);
						int dY = RandomUtils.randomInt(20, -20);
						String terrain = world.getMap().getPos(new Point.Double(position.x+dX, position.y+dY));
						while (terrain.equals("x") || terrain.equals("w")) {
							dX = RandomUtils.randomInt(20, -20);
							dY = RandomUtils.randomInt(20, -20);
							terrain = world.getMap().getPos(new Point.Double(position.x+dX, position.y+dY));
						}
						path = (new AStar(this.position, new Point.Double(position.x+dX, position.y+dY), this.world.getMap()).getPath());
					}
					
				} else {

					proceedOnPath();
				}
			}

			break;
		case MOVING:
			if ((position.equals(path.get(path.size()-1)))) {
				activity = ACTIVITY.CHILLING;
			} else {
				proceedOnPath();
			}

			break;
		case HUNTING:
			if (path.isEmpty() || path == null || (position.equals(path.get(path.size()-1)))) {
				activity = ACTIVITY.CHILLING;
			} else {
				proceedOnPath();
			}

			break;
		case HUNTING_PLANT:
			if (position.equals(path.get(path.size()-1))) {
				activity = ACTIVITY.CHILLING;
			} else {
				proceedOnPath();
			}

			break;
		case HUNTING_WATER:
			if (position.equals(path.get(path.size()-1))) {
				drink();
			} else {
				proceedOnPath();
			}
			break;
		case DRINKING:
			drink();
			break;
		case SLEEPING:
			break;

		default:
			break;
		}
		// trigger hunger or thirst
		// hunger += 0.1;

		if (hunger == MAXHUNGER && (activity != ACTIVITY.HUNTING || activity != ACTIVITY.HUNTING_PLANT)) {
			// find and seek food
			// trigger hunger, search for food, plot path and start moving
			if (diet == DIET.HERBIVORE) {
				this.speed = PURPOSEFUL_SPEED;
				System.out.println("HUNTING PLANT");
				this.activity = ACTIVITY.HUNTING_PLANT;
				this.path = (new AStar(this.position, findNearestFood(), this.world.getMap()).getPath());
				System.out.println("length: " + path.size());
				pathIndex = 0;
			}

		}

		if (thirst >= MAXTHIRST) {

			if (activity != ACTIVITY.HUNTING_WATER && activity != ACTIVITY.DRINKING) {
				// find and seek water
				this.speed = PURPOSEFUL_SPEED;
				System.out.println("HUNTING WATER");
				this.activity = ACTIVITY.HUNTING_WATER;
				AStar astar = new AStar(this.position, findNearestFood(), this.world.getMap());
				this.path = (new AStar(this.position, astar.findNearestWater(), this.world.getMap()).getPath());
				System.out.println("length: " + path.size());
				pathIndex = 0;
			}
		} else {
			thirst += 0.1;
			//System.out.println("thirst: " + thirst);

		}

	}

	private void proceedOnPath() {
		if (!path.isEmpty() && pathIndex < path.size()) {
			Point.Double next = path.get(pathIndex);
			// System.out.println("MOVING TO " + next.x + ", " + next.y);

			// snap to
			if (next.distance(this.position) < speed) {
				this.move(next);
				pathIndex++;
				//path.remove();
				// System.out.println("next");
			} else {

				// move X
				if (next.x > position.x) {
					this.move(speed, 0);
				} else if (next.x < position.x) {
					this.move(-speed, 0);
				}

				// move Y
				if (next.y > position.y) {
					this.move(0, speed);
				} else if (next.y < position.y) {
					this.move(0, -speed);
				}
				// System.out.println("moving to point");
			}

		}

	}

	private void drink() {
		boolean water = false;
		// check water here

		if (world.getMap().getPos(position).equals("=") || world.getMap().getPos(position).equals("w")) {
			water = true;
		}
		// check water nearby

		Pair<Integer, Integer> translation;
		Point.Double test;

		for (int i = 0; i < 8; i++) {
			translation = Translations.TRANSLATIONS_GRID.get(i);
			test = new Point.Double(position.x + translation.getKey(), position.y + translation.getValue());
			if (world.getMap().getPos(test).equals("=") || world.getMap().getPos(test).equals("w")) {
				water = true;
				break;
			}
		}
		
		if (water) {
			activity = ACTIVITY.DRINKING;
			System.out.println("SLURP");
			
			thirst -= MAXTHIRST/100;
			if (thirst <= 0) {
				this.activity=ACTIVITY.CHILLING;
			}

		} else {
			System.out.println("no water found");
			return;
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
		this.position = new Point.Double(this.position.x + dX, this.position.y + dY);
	}

	private void move(Point.Double p) {
		this.position = p;
	}

}
