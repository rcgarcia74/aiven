package com.aiven.datagen;

public class SoilCondition {
	private String soilType;
	private double moistureLevel;
	
	enum Type {
		
		CLAY,
		LOAMY,
		SANDY
	}
	
	/**
	 *  Soi Moisture Levels
	 *  
	 *  		HIGH					MEDIUM				LOW
	 *  Clay -  80-100					60-80				<60
	 *  Loamy	88-100					70-88				<70
	 *  Sandy	90-100					80-90				<80
	 */
	
	enum Moisture {
		
		LOW,
		MEDIUM,
		HIGH
	}
	
	SoilCondition(String soilType, double moistureLevel){
		this.soilType = soilType;
		this.moistureLevel = moistureLevel;
	}
	
	public boolean addWater() {
		if(soilType.equalsIgnoreCase(SoilCondition.Type.CLAY.toString())) {
			if(moistureLevel < 80)
				return true;
						
		}
		
		if(soilType.equalsIgnoreCase(SoilCondition.Type.LOAMY.toString())) {
			if(moistureLevel < 88)
				return true;
						
		} 
		
		if(soilType.equalsIgnoreCase(SoilCondition.Type.SANDY.toString())) {
			if(moistureLevel < 90)
				return true;
						
		} 
		
		return false;
	}

}
