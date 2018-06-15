package utils;

import java.util.Random;

public class RandomUtils {

	public static int randomGaussian(double mean, double  standardDeviation) {
		Random randomer = new Random();
		double val = randomer.nextGaussian()*standardDeviation+mean;
		return (int) val;
	}
	
	public static int randomPosGaussian(double mean, double  standardDeviation) {
		Random randomer = new Random();
		double val = randomer.nextGaussian()*standardDeviation+mean;
		return (int) Math.abs(val);
	}

	public static int randomInt(int max, int min) {
		Random rand = new Random();
		
		return rand.nextInt((max - min) + 1) + min; 
	}
	
}
