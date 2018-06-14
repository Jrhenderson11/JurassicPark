package ai;

import java.util.ArrayList;

import javafx.util.Pair;

public class Translations {
	private enum Step {
		GRID(1), DISPLAY(2);
		private final int STEP;

		Step(int step) {
			this.STEP = step;
		}
	}

	/**
	 * The possible translations that can be done using a grid "step"
	 */
	public static final ArrayList<Pair<Integer, Integer>> TRANSLATIONS_GRID = getTranslations(Step.GRID);

	/**
	 * The possible translations that can be done using a display "step"
	 */
	public static final ArrayList<Pair<Integer, Integer>> TRANSLATIONS_DISPLAY = getTranslations(Step.DISPLAY);

	/**
	 * 
	 * @param type
	 *            The type of point that is being translated: GRID or DISPLAY
	 * @return The translations that can be applied to this point, represented as a
	 *         pair where the key is the x translation and the value is the y
	 *         translation
	 */
	private static ArrayList<Pair<Integer, Integer>> getTranslations(Step type) {
		ArrayList<Pair<Integer, Integer>> translations = new ArrayList<>();
		translations.add(new Pair<>(-type.STEP, -type.STEP));
		translations.add(new Pair<>(0, -type.STEP));
		translations.add(new Pair<>(type.STEP, -type.STEP));
		translations.add(new Pair<>(-type.STEP, 0));
		translations.add(new Pair<>(type.STEP, 0));
		translations.add(new Pair<>(-type.STEP, type.STEP));
		translations.add(new Pair<>(0, type.STEP));
		translations.add(new Pair<>(type.STEP, type.STEP));
		return translations;
	}
}
