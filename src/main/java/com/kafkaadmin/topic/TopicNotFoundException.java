package com.kafkaadmin.topic;

import com.kafkaadmin.common.KafkaAdminException;

/**
 * Exception thrown when a requested topic cannot be found.
 */
public class TopicNotFoundException extends KafkaAdminException {

    /**
     * Creates an exception for the given topic name.
     *
     * @param topicName the name of the topic that was not found
     */
    public TopicNotFoundException(String topicName) {
        super("Topic '" + topicName + "' not found");
    }
}
