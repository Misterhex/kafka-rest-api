package com.kafkaadmin.consumergroup;

import com.kafkaadmin.common.KafkaAdminException;

/**
 * Exception thrown when a requested consumer group cannot be found.
 */
public class ConsumerGroupNotFoundException extends KafkaAdminException {

    /**
     * Creates an exception for the given consumer group ID.
     *
     * @param groupId the ID of the consumer group that was not found
     */
    public ConsumerGroupNotFoundException(String groupId) {
        super("Consumer group '" + groupId + "' not found");
    }
}
