package com.aiven.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public final class Consumer {
    public static void main(String[] args) {
        String TOPIC_NAME = "farm_sensor";
        String TRUSTSTORE_PASSWORD = "aiven123";

        String sasl_username = "avnadmin";
        String sasl_password = "AVNS_L95N-k4Onl5YxyzXVVl";
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasConfig = String.format(jaasTemplate, sasl_username, sasl_password);

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "kafka-sensors-iot-farm.e.aivencloud.com:13912");
        properties.setProperty("security.protocol", "SASL_SSL");
        properties.setProperty("sasl.mechanism", "SCRAM-SHA-256");
        properties.setProperty("sasl.jaas.config", jaasConfig);
        properties.setProperty("ssl.endpoint.identification.algorithm", "");
        properties.setProperty("ssl.truststore.type", "jks");
        properties.setProperty("ssl.truststore.location", "/Users/administrator/client.truststore.jks");
        properties.setProperty("ssl.truststore.password", TRUSTSTORE_PASSWORD);
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());
        properties.setProperty("group.id", "groupid");

        try (// create a consumer
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties)) {
			consumer.subscribe(Arrays.asList(TOPIC_NAME));
			while (true) {
			  ConsumerRecords<String, String> messages = consumer.poll(Duration.ofMillis(100));
			  messages.forEach(message -> {
			    System.out.println("Got message using SASL: " + message.value());
			  });
			}
		}
    }
}
