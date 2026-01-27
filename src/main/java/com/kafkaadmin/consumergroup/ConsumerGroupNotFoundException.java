package com.kafkaadmin.consumergroup;

import com.kafkaadmin.common.KafkaAdminException;

public class ConsumerGroupNotFoundException extends KafkaAdminException {

    public ConsumerGroupNotFoundException(String groupId) {
        super("Consumer group '" + groupId + "' not found");
    }
}
