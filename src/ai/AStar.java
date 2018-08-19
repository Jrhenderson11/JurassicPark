package ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import javafx.util.Pair;
import version2.Terrain.Biome;
import world.Map;

public class AStar {
	/**
	 * The cost that should be used when travelling over tiles which are not owned
	 * by the player
	 */
	private static final double RIVER_COST = 10;

	/**
	 * The possible translations that can be applied to a point to move it in any
	 * directions adjacent to it
	 */
	private ArrayList<Pair<Integer, Integer>> translations = Translations.TRANSLATIONS_GRID;

	/**
	 * The starting point in the path
	 */
	private Point.Double startPoint;

	/**
	 * The point to travel to from the starting point
	 */
	private Point.Double finishPoint;

	/**
	 * The map of the terrain
	 */
	private Map level;

	/**
	 * A HashMap of costs to a given point on the HashMap
	 */
	private HashMap<Point.Double, Double> cost;

	/**
	 * A HashMap of the parent of a given point on the HashMap
	 */
	private HashMap<Point.Double, Point.Double> parent;

	private List<Point.Double> considered;

	// -----------
	// Constructor
	// -----------

	/**
	 * An A Star path-finding implementation to get from one point on the HashMap to
	 * another given a player and the level they are playing
	 * 
	 * @param startPoint
	 *            The starting grid point on the HashMap
	 * @param finishPoint
	 *            The finishing grid point on the HashMap
	 * @param level
	 *            The level that is currently being played in the game, which
	 *            contains the HashMap
	 */
	public AStar(Point.Double startPoint, Point.Double finishPoint, Map level) {
		System.out.println("ASTAR to " + finishPoint);
		this.startPoint = startPoint;
		this.finishPoint = finishPoint;
		this.level = level;
		this.cost = new HashMap<>();
		this.parent = new HashMap<>();

	}

	// -------
	// Methods
	// -------

	/**
	 * A method to calculate the heuristic which determines how close the current
	 * point being considered is to the finishing point
	 * 
	 * @param point
	 *            The current point being considered
	 * @return The distance between the two points as the addition of the absolute
	 *         difference between the x and y co-ordinates
	 */
	private double getH(Point.Double point) {
		return Math.abs(point.x - finishPoint.x) + Math.abs(point.y - finishPoint.y);
	}

	/**
	 * Get the path that is generated by the algorithm
	 * 
	 * @return The path as a linked list of grid points that link the starting point
	 *         to the finishing point
	 */
	public List<Point.Double> getPath() {
		this.considered = new ArrayList<>();
		List<Point.Double> path = new LinkedList<>();
		// quick test to see neither are unreachable
		if (level.getPosElev(startPoint) > 0.6 || level.getPosElev(finishPoint) > 0.6
				|| level.getPos(startPoint) == Biome.SEA || level.getPos(finishPoint) == Biome.SEA) {
			System.out.println("destination unreachable, stopping");
			return path;
		}
		cost.put(startPoint, 0.0);

		Queue<Point.Double> opened = new PriorityQueue<>(11, new Comparator<Point.Double>() {
			@Override
			public int compare(Point.Double p1, Point.Double p2) {
				if ((cost.get(p1) + getH(p1)) < (cost.get(p2) + getH(p2))) {
					return -1;
				} else if ((cost.get(p1) + getH(p1)) > (cost.get(p2) + getH(p2))) {
					return 1;
				}
				return 0;
			}
		});

		Point.Double current = null;
		ArrayList<Point.Double> visited = new ArrayList<Point.Double>();
		opened.add(startPoint);

		while (!opened.isEmpty()) {
			current = opened.poll();
			visited.add(current);

			if (current.equals(finishPoint)) {
				break;
			}

			for (Point.Double t : removeInvalid(getNeighbours(current, visited))) {
				// System.out.println("a*: " + t);
				if (!visited.contains(t)) {

					// sand and grass cost 1
					if (!level.getTerrain().getBiomeLayer()[(int) t.x][(int) t.y].equals(Biome.WATER)) {
						cost.put(t, cost.get(current) + 1);
					} else if (level.getTerrain().getBiomeLayer()[(int) t.x][(int) t.y].equals(Biome.WATER)) {
						cost.put(t, cost.get(current) + RIVER_COST);
					}

					parent.put(t, current);
					opened.add(t);
				}
			}
		}

		assert (current.equals(finishPoint));
		Point.Double parentPoint = null;

		try {
			while (!(parentPoint = parent.get(current)).equals(startPoint)) {
				path.add(current);
				current = parentPoint;
			}
		} catch (NullPointerException e) {
			// just ignore it, it will probably be fine
		}
		path.add(current);
		Collections.reverse(path);

		return path;
	}

	public Point.Double findNearestWater() {
		this.considered = new ArrayList<>();
		// quick test to see neither are unreachable
		if (level.getPos(startPoint) == Biome.BARE || level.getPos(finishPoint) == Biome.BARE
				|| level.getPos(startPoint) == Biome.SEA || level.getPos(finishPoint) == Biome.SEA) {
			System.out.println("destination unreachable, stopping");
		}
		cost.put(startPoint, 0.0);

		Queue<Point.Double> opened = new PriorityQueue<>(11, new Comparator<Point.Double>() {
			@Override
			public int compare(Point.Double p1, Point.Double p2) {
				if (cost.get(p1) < cost.get(p2)) {
					return -1;
				} else if (cost.get(p1) > cost.get(p2)) {
					return 1;
				}
				return 0;
			}
		});

		Point.Double current = null;
		ArrayList<Point.Double> visited = new ArrayList<Point.Double>();
		opened.add(startPoint);

		while (!opened.isEmpty()) {
			current = opened.poll();
			visited.add(current);

			// if at water finish
			if (level.getPos(current) == Biome.SEA || level.getPos(current) == Biome.WATER) {
				System.out.println("found water at " + current);
				return current;
			}

			for (Point.Double t : removeInvalid(getNeighbours(current, visited))) {
				// System.out.println("a*: " + t);
				if (!visited.contains(t)) {

					cost.put(t, cost.get(current) + 1);
					parent.put(t, current);
					opened.add(t);
				}
			}
		}

		Point.Double parentPoint = null;

		try {
			while (!(parentPoint = parent.get(current)).equals(startPoint)) {
				current = parentPoint;
			}
		} catch (NullPointerException e) {
			// just ignore it, it will probably be fine
		}
		// if no water return start position
		return startPoint;
	}

	/**
	 * Remove any points from the current points being considered that are invalid
	 * (e.g. are walls or are off the grid)
	 * 
	 * @param neighbours
	 *            The list of points to be considered
	 * @return The list of points which are not invalid
	 */
	private ArrayList<Point.Double> removeInvalid(ArrayList<Point.Double> neighbours) {
		ArrayList<Point.Double> validNeighbours = new ArrayList<>();
		Biome wall;
		for (Point.Double p : neighbours) {
			wall = level.getTerrain().getBiomeLayer()[(int) p.x][(int) p.y];
			// Remove those which are off the grid or collide with a wall
			if (!(wall == Biome.SEA) && !(level.getTerrain().getElevation()[(int) p.x][(int) p.y] > 0.6)) {
				validNeighbours.add(p);
				// System.out.println("adding " + p);
			} else {
			}
		}
		return validNeighbours;
	}

	/**
	 * Get the neighbours of the current point
	 * 
	 * @param p
	 *            The point to get neighbours of
	 * @return The points which are directly adjacent to the point p
	 */

	private ArrayList<Point.Double> getNeighbours(Point.Double p, List<Point.Double> visited) {
		ArrayList<Point.Double> neighbours = new ArrayList<>();
		Point.Double current;
		Biome val;
		Pair<Integer, Integer> translation;
		for (int i = 0; i < 8; i++) {
			translation = translations.get(i);
			// Key is x translation, Value is y translation
			current = new Point.Double(p.x + translation.getKey(), p.y + translation.getValue());
			try {
				val = level.getTerrain().getBiomeLayer()[(int) current.x][(int) current.y];
				// yeah i dont get what this is
				if (!(val == Biome.SEA) && !(level.getTerrain().getElevation()[(int) current.x][(int) current.y] > 0.6)
						&& !considered.contains(current)) {
					neighbours.add(current);
					considered.add(current);
					// System.out.println(current);
				}
			} catch (ArrayIndexOutOfBoundsException e) {

			}
		}
		return neighbours;
	}

}
