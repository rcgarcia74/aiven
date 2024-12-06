EXECUTE STATEMENT SET
BEGIN

    INSERT INTO dry_soil SELECT deviceId,
    							temperature,
    							humidity,
    							soilType,
    							moisture_level,
    							add_water,
    							TO_TIMESTAMP(recordDate, 'yyyy-MM-dd HH:mm:ss') AS recordDateTs 
    							FROM farm_sensor 
    					 WHERE add_water is true;
    					 
    INSERT INTO wet_soil SELECT deviceId,
    							temperature,
    							humidity,
    							soilType,
    							moisture_level,
    							add_water,
    							TO_TIMESTAMP(recordDate, 'yyyy-MM-dd HH:mm:ss') AS recordDateTs 
    							FROM farm_sensor
    							WHERE add_water is false;
    							
END