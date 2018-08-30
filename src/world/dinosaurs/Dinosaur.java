package world.dinosaurs;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Image;

import ai.AStar;
import ai.Translations;
import heightmaps.generators.RandomUtils;
import interfaces.Drawable;
import javafx.util.Pair;
import version2.Terrain.Biome;
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

	private double MAXHUNGER;
	private double MAXTHIRST;
	private double hunger = 0;
	private double thirst = 0;

	private World world;
	private List<Point.Double> path;
	private int pathIndex;

	private double PURPOSEFUL_SPEED = 0.1;
	private double LAZY_SPEED = 0.05;

	private double speed = LAZY_SPEED;

	private int waitCounter = 0;
	private int waitTime;

	public Dinosaur(String newName, Point.Double newPos, String spritePath, World brave, DIET foodType) {
		this.name = newName;
		this.position = newPos;
		try {
			// System.out.println(spritePath);
			this.sprite = new Image(spritePath);
		} catch (Exception e) {
			System.out.println("could not load sprite for " + name);
		}
		this.world = brave;
		this.path = new ArrayList<Point.Double>();
		this.activity = ACTIVITY.CHILLING;
		this.diet = foodType;
		this.mood = MOOD.CONTENT;
		this.MAXHUNGER=RandomUtils.randomPosGaussian(500, 100);

		this.MAXTHIRST=RandomUtils.randomPosGaussian(500, 100);

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
		// System.out.println("activity: " + activity);
		switch (activity) {
		case CHILLING:
			if (mood == MOOD.SAD) {
				// sit still :(

			} else {
				// move around a bit
				if (path.isEmpty() || path == null || (position.equals(path.get(path.size() - 1)))) {
					// wait random amount
					if (waitCounter == 0) {
						if (mood == MOOD.CONTENT) {
							this.waitTime = RandomUtils.randomPosGaussian(1000, 750);
						} else {
							this.waitTime = RandomUtils.randomInt(600, 0);
						}
						// System.out.println("TIME: " + waitTime);
					}
					// System.out.println("CHILLING: " + waitCounter +" / "+ waitTime);

					if (waitCounter++ == waitTime) {
						// make new path
						this.speed = LAZY_SPEED;
						waitCounter = 0;
						try {
							int dX = RandomUtils.randomInt(20, -20);
							int dY = RandomUtils.randomInt(20, -20);
							Biome terrain;
							// check in / out of bounds
							while (position.x + dX < 0 || position.x + dX > world.getMap().getWidth()
									|| position.y + dY < 0 || position.y + dY > world.getMap().getWidth()
									|| world.getMap()
											.getPosElev(new Point.Double(position.x + dX, position.y + dY)) > 0.7
									|| world.getMap()
											.getPos(new Point.Double(position.x + dX, position.y + dY)) == Biome.SEA) {
								dX = RandomUtils.randomInt(20, -20);
								dY = RandomUtils.randomInt(20, -20);
								terrain = world.getMap().getPos(new Point.Double(position.x + dX, position.y + dY));
							}
							path = (new AStar(this.position, new Point.Double(position.x + dX, position.y + dY),
									this.world.getMap()).getPath(false));
							pathIndex = 0;
						} catch (Exception e) {
							//wants to go off map?
							System.out.println(e.getStackTrace().toString());
						}
					}
					// System.out.println(waitCounter);
				} else {
					proceedOnPath();
				}
			}

			break;
		case MOVING:
			if ((position.equals(path.get(path.size() - 1)))) {
				activity = ACTIVITY.CHILLING;
			} else {
				proceedOnPath();
			}

			break;
		case HUNTING:
			if (path.isEmpty() || path == null || (position.equals(path.get(path.size() - 1)))) {
				activity = ACTIVITY.CHILLING;
			} else {
				proceedOnPath();
			}

			break;
		case HUNTING_PLANT:
			if (position.equals(path.get(path.size() - 1))) {
				eat();
			} else {
				proceedOnPath();
			}
			break;
		case HUNTING_WATER:
			if (position.equals(path.get(path.size() - 1))) {
				drink();
			} else {
				proceedOnPath();
			}
			break;
		case DRINKING:
			drink();
			break;
		case EATING:
			eat();
			break;
		case SLEEPING:
			break;

		default:
			break;
		}

		// trigger hunger or thirst
		hunger += 0.1;

		if (hunger >= MAXHUNGER && (activity != ACTIVITY.HUNTING && activity != ACTIVITY.HUNTING_PLANT && activity != ACTIVITY.HUNTING_WATER && activity != ACTIVITY.DRINKING)) {
			// find and seek food
			// trigger hunger, search for food, plot path and start moving
			if (diet == DIET.HERBIVORE) {
				this.speed = PURPOSEFUL_SPEED;
				System.out.println("HUNTING PLANT");
				this.activity = ACTIVITY.HUNTING_PLANT;

				this.path = (new AStar(new Point.Double((int) this.position.getX(), (int) this.position.getY()),
						findNearestFood(), this.world.getMap()).getPath(false));
				System.out.println("length: " + path.size());
				pathIndex = 0;
			}
		}

		if (thirst >= MAXTHIRST) {
			//if not already eating or drinking
			if (activity != ACTIVITY.HUNTING_WATER && activity != ACTIVITY.DRINKING && (activity != ACTIVITY.HUNTING && activity != ACTIVITY.HUNTING_PLANT)) {
				// find and seek water
				this.speed = PURPOSEFUL_SPEED;
				System.out.println("HUNTING WATER");
				this.activity = ACTIVITY.HUNTING_WATER;
				AStar astar = new AStar(this.position, findNearestFood(), this.world.getMap());
				this.path = (new AStar(this.position, astar.findNearestWater(), this.world.getMap()).getPath(true));
				System.out.println("length: " + path.size());
				pathIndex = 0;
			}
		} else {
			thirst += 0.1;
			// System.out.println("thirst: " + thirst);
		}

	}

	private void proceedOnPath() {
		if (!path.isEmpty() && pathIndex < path.size()) {
			Point.Double next = path.get(pathIndex);
			// System.out.println("MOVING TO " + next.x + ", " + next.y + " FROM " +
			// position.x + ", " + position.y);

			// snap to
			if (next.distance(this.position) < speed) {
				this.move(next);
				pathIndex++;
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

		} else {
			// System.out.println("hmm path is done?");
			// for (Point.Double p : path) System.out.println(p);
		}

	}

	private void drink() {
		boolean water = false;
		// check water here

		if (world.getMap().getPos(position) == Biome.WATER || world.getMap().getPos(position) == Biome.SHALLOW_SEA) {
			water = true;
		}
		// check water nearby

		Pair<Integer, Integer> translation;
		Point.Double test;

		for (int i = 0; i < 8; i++) {
			translation = Translations.TRANSLATIONS_GRID.get(i);
			test = new Point.Double(position.x + translation.getKey(), position.y + translation.getValue());
			if (world.getMap().getPos(test) == Biome.WATER || world.getMap().getPos(test) == Biome.SHALLOW_SEA) {
				water = true;
				break;
			}
		}

		if (water) {
			activity = ACTIVITY.DRINKING;
			System.out.println("SLURP");

			thirst -= MAXTHIRST / 100;
			if (thirst <= 0) {
				this.activity = ACTIVITY.CHILLING;
			}

		} else {
			System.out.println("no water found");
			return;
		}
	}

	private void eat() {
		boolean food = false;
		// check water here

		if (findNearestFood().distance(this.position) < 1) {
			food = true;
		}

		Pair<Integer, Integer> translation;
		Point.Double test;

		for (int i = 0; i < 8; i++) {
			translation = Translations.TRANSLATIONS_GRID.get(i);
			test = new Point.Double(position.x + translation.getKey(), position.y + translation.getValue());
			if (findNearestFood().distance(this.position) < 1) {
				food = true;
				break;
			}
		}

		if (food) {
			activity = ACTIVITY.EATING;
			System.out.println("CHOMP");

			hunger -= MAXHUNGER / 100;
			if (hunger <= 0) {
				this.activity = ACTIVITY.CHILLING;
			}

		} else {
			System.out.println("no food found :(");
			return;
		}
	}

	public Point.Double findNearestFood() {
		Point.Double foodPos = null;
		if (diet == DIET.HERBIVORE) {
			// find plant
			double dist = Integer.MAX_VALUE;
			for (Plant p : world.getMap().getSmallPlants()) {
				if (p.getPos().distance(this.position) < dist) {
					// System.out.println(p.getPos() + ": " + dist);
					dist = p.getPos().distance(this.position);
					foodPos = p.getPos();
				}
			}
		}
		// System.out.println("nearest plant to " + name + " is at " + foodPos.x + ", "
		// + foodPos.y);
		return foodPos;
	}

	private void move(double dX, double dY) {
		this.position = new Point.Double(this.position.x + dX, this.position.y + dY);
		if (dX < 0) {
			this.direction = -1;
		} else if (dX > 0) {
			this.direction = 1;
		}
	}

	private void move(Point.Double p) {
		this.position = p;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ACTIVITY getActivity() {
		return activity;
	}

	public void setActivity(ACTIVITY activity) {
		this.activity = activity;
	}

	public MOOD getMood() {
		return mood;
	}

	public void setMood(MOOD mood) {
		this.mood = mood;
	}

	public double getHunger() {
		return hunger;
	}

	public void setHunger(double hunger) {
		this.hunger = hunger;
	}

	public double getThirst() {
		return thirst;
	}

	public void setThirst(double thirst) {
		this.thirst = thirst;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getWaitCounter() {
		return waitCounter;
	}

	public void setWaitCounter(int waitCounter) {
		this.waitCounter = waitCounter;
	}

	public int getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	public DIET getDiet() {
		return diet;
	}

}