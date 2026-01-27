package com.kafkaadmin.common;

/**
 * Base exception for Kafka administration errors.
 *
 * <p>Thrown when communication with Kafka fails or an unexpected error occurs
 * during admin operations.
 */
public class KafkaAdminException extends RuntimeException {

    /**
     * Creates an exception with the given message.
     *
     * @param message the error message
     */
    public KafkaAdminException(String message) {
        super(message);
    }

    /**
     * Creates an exception with the given message and cause.
     *
     * @param message the error message
     * @param cause the underlying cause
     */
    public KafkaAdminException(String message, Throwable cause) {
        super(message, cause);
    }
}
