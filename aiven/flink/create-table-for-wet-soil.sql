---- When this Apache Flink application runs, it routes all records where farms doesn't need water

CREATE TABLE wet_soil (

    `deviceId` STRING,
	`temperature` DOUBLE,
	`humidity` DOUBLE,
	`soilType` STRING,
	`moisture_level` DOUBLE,
	`add_water` BOOLEAN,
	`recordDateTs` TIMESTAMP,
    PRIMARY KEY (soilType) NOT ENFORCED
)

WITH (

  'connector' = 'upsert-kafka',
  'topic' = 'wet_soil',
  'properties.bootstrap.servers' = 'kafka-sensors-iot-farm.e.aivencloud.com:13912',
  'value.format' = 'json',
  'key.format' = 'json'
)
