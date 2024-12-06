package com.aiven.datagen;

public class AtmosphericCondition {
	
	private double humidity;
	private double temperature;
	
	enum Humidity {
		
		OPTIMAL, //80%
		SUBOPTIMAL //anything outside of 80%
	}
	
	enum Temperature {
		OPTIMAL, // 64°F - 75°F
		SUBOPTIMAL //anything outside of OPTIMAL
	}
	
	AtmosphericCondition (double humidity, double temperature){
		this.humidity = humidity;
		this.temperature = temperature;
	}
	
	public boolean isIdealConditionForFarming() {
		if(humidity != 80) {
			if(temperature >= 64 && temperature <= 75) {
				return true;
			}
		}
		
		return false;
	}

}
