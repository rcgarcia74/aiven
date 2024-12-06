-- When this Apache Flink application runs, it routes all records where farms need water
CREATE TABLE dry_soil (

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
  'topic' = 'dry_soil',
  'properties.bootstrap.servers' = 'kafka-sensors-iot-farm.e.aivencloud.com:13912',
  'value.format' = 'json',
  'key.format' = 'json'
)