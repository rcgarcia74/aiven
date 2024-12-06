package com.aiven.datagen;

import java.util.ArrayList;
import java.util.Random;

public class Randomizer {
	
	private Random randomGenerator;
	private ArrayList<String> stringList;
	
	public Randomizer() {
		
		randomGenerator = new Random();
	}
	
	public Randomizer(ArrayList<String> stringList) {
		this.stringList = stringList;
		randomGenerator = new Random();
	}
	
	public String getRandomString() {
		
		int index = randomGenerator.nextInt(stringList.size());
		return stringList.get(index);

	}
	
	public double generateRandomDouble(double min, double max) {
		
		return min + (max - min) * randomGenerator.nextDouble();
	}

}
