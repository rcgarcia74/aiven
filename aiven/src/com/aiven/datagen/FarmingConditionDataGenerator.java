package com.aiven.datagen;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Generate IoT data for farming conditions
 * 
 * FarmingConditionDataGenerator creates attributes that reflects ideal farming conditions.
 * It includes data for atmospheric weather like temperature and humidity along with soil types and
 * soil moisture level. 
 */

public class FarmingConditionDataGenerator {
	private TimeZone tz;
	private DateFormat df;
	
	public FarmingConditionDataGenerator() {
		this.tz = TimeZone.getTimeZone("UTC");
		this.df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.df.setTimeZone(tz);
	}
	
	public String generateDataInJSON() {
		String recordDate = df.format(new Date());
        String deviceId = UUID.randomUUID().toString();
    	
        //Generate Atmospheric Condition data
        Randomizer randomizer = new Randomizer();
        double temperature = randomizer.generateRandomDouble(0, 125); 
        double humidity = randomizer.generateRandomDouble(0, 100);   
        
        
        //Generate Soil Moisture Levels
        ArrayList<String> soilTypes = new ArrayList<String>();
        soilTypes.add(SoilCondition.Type.CLAY.toString());
        soilTypes.add(SoilCondition.Type.LOAMY.toString());
        soilTypes.add(SoilCondition.Type.SANDY.toString());
        
        randomizer = new Randomizer(soilTypes);
        String soilType = randomizer.getRandomString();
        
        randomizer = new Randomizer();
        double moistureLevel = randomizer.generateRandomDouble(0, 100);
        
        SoilCondition soilCondition = new SoilCondition(soilType, moistureLevel);
        boolean addWater = soilCondition.addWater();
        

        // Create JSON string
        String jsonData = String.format("{\"deviceId\": \"%s\", \"temperature\": %.2f, \"humidity\": %.2f, \"soilType\": \"%s\", \"moisture_level\": %.2f, \"add_water\": \"%s\", \"recordDate\": \"%s\"}",
                deviceId, temperature, humidity, soilType, moistureLevel, addWater, recordDate);

        return jsonData;

	}

}
