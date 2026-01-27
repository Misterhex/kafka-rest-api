package com.kafkaadmin.common;

import com.kafkaadmin.consumergroup.ConsumerGroupNotFoundException;
import com.kafkaadmin.topic.TopicNotFoundException;
import com.kafkaadmin.cluster.BrokerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler providing consistent error responses across all REST endpoints.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(TopicNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTopicNotFound(
            TopicNotFoundException ex, HttpServletRequest request) {
        log.warn("Topic not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(
                        HttpStatus.NOT_FOUND.value(),
                        "Not Found",
                        ex.getMessage(),
                        request.getRequestURI()));
    }

    @ExceptionHandler(ConsumerGroupNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleConsumerGroupNotFound(
            ConsumerGroupNotFoundException ex, HttpServletRequest request) {
        log.warn("Consumer group not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(
                        HttpStatus.NOT_FOUND.value(),
                        "Not Found",
                        ex.getMessage(),
                        request.getRequestURI()));
    }

    @ExceptionHandler(BrokerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBrokerNotFound(
            BrokerNotFoundException ex, HttpServletRequest request) {
        log.warn("Broker not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(
                        HttpStatus.NOT_FOUND.value(),
                        "Not Found",
                        ex.getMessage(),
                        request.getRequestURI()));
    }

    @ExceptionHandler(KafkaAdminException.class)
    public ResponseEntity<ErrorResponse> handleKafkaAdminException(
            KafkaAdminException ex, HttpServletRequest request) {
        log.error("Kafka admin error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal Server Error",
                        ex.getMessage(),
                        request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal Server Error",
                        "An unexpected error occurred",
                        request.getRequestURI()));
    }
}
