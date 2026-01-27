package com.kafkaadmin.common;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Error response")
public record ErrorResponse(
        @Schema(description = "HTTP status code", example = "404")
        int status,

        @Schema(description = "Error type", example = "Not Found")
        String error,

        @Schema(description = "Error message", example = "Topic 'my-topic' not found")
        String message,

        @Schema(description = "Request path", example = "/api/v1/topics/my-topic")
        String path,

        @Schema(description = "Timestamp of the error")
        Instant timestamp
) {
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(status, error, message, path, Instant.now());
    }
}
