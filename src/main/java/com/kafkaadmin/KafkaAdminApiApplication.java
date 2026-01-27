package com.kafkaadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Spring Boot application entry point for the Kafka Admin API.
 *
 * <p>Provides REST endpoints for managing and monitoring Kafka clusters,
 * topics, and consumer groups.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class KafkaAdminApiApplication {

    /**
     * Application entry point.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(KafkaAdminApiApplication.class, args);
    }
}
