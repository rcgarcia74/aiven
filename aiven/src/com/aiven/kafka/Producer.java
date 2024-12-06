package com.aiven.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.aiven.datagen.FarmingConditionDataGenerator;

public final class Producer {
    public static void main(String[] args) {
        String TOPIC_NAME = "farm_sensor";
        String TRUSTSTORE_PASSWORD = "";

        String sasl_username = "";
        String sasl_password = "";
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasConfig = String.format(jaasTemplate, sasl_username, sasl_password);

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "");
        properties.setProperty("security.protocol", "SASL_SSL");
        properties.setProperty("sasl.mechanism", "SCRAM-SHA-256");
        properties.setProperty("sasl.jaas.config", jaasConfig);
        properties.setProperty("ssl.endpoint.identification.algorithm", "");
        properties.setProperty("ssl.truststore.type", "jks");
        properties.setProperty("ssl.truststore.location", "");
        properties.setProperty("ssl.truststore.password", TRUSTSTORE_PASSWORD);
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
        	
			// produce messages every second
			while(true) {
				FarmingConditionDataGenerator fcdg = new FarmingConditionDataGenerator();
				String message = fcdg.generateDataInJSON();
			    
			    try {
			    	
			    	producer.send(new ProducerRecord<String, String>(TOPIC_NAME, message));

					System.out.println("Message sent: " + message);
			    	
			        Thread.sleep(1000);
			        
			    } 	catch (InterruptedException ex) {
			    	ex.printStackTrace();
			    	producer.close();
			    	
			    }	catch(Exception e) {
			    	e.printStackTrace();
			    }
			}
		}
    }
}
