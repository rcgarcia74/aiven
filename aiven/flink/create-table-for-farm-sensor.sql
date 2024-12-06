-- This table represents all raw records from the farm_sensor Kafka topic
CREATE TABLE farm_sensor (

	`deviceId` STRING,
	`temperature` DOUBLE,
	`humidity` DOUBLE,
	`soilType` STRING,
	`moisture_level` DOUBLE,
	`add_water` BOOLEAN,
	`recordDate` STRING
) 

PARTITIONED BY (soilType)

WITH (

	'connector' = 'kafka',
  	'topic' = 'farm_sensor',
  	'scan.startup.mode' = 'earliest-offset',
  	'properties.bootstrap.servers' = 'kafka-sensors-iot-farm.e.aivencloud.com:13912',
  	'value.format' = 'json'
);