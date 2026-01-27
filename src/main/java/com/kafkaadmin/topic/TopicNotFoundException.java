package com.kafkaadmin.topic;

import com.kafkaadmin.common.KafkaAdminException;

public class TopicNotFoundException extends KafkaAdminException {

    public TopicNotFoundException(String topicName) {
        super("Topic '" + topicName + "' not found");
    }
}
