package com.kafkaadmin.cluster;

import com.kafkaadmin.common.KafkaAdminException;

public class BrokerNotFoundException extends KafkaAdminException {

    public BrokerNotFoundException(int brokerId) {
        super("Broker with ID '" + brokerId + "' not found");
    }
}
