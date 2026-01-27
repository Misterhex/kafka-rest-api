package com.kafkaadmin.cluster;

import com.kafkaadmin.common.KafkaAdminException;

/**
 * Exception thrown when a requested broker cannot be found.
 */
public class BrokerNotFoundException extends KafkaAdminException {

    /**
     * Creates an exception for the given broker ID.
     *
     * @param brokerId the ID of the broker that was not found
     */
    public BrokerNotFoundException(int brokerId) {
        super("Broker with ID '" + brokerId + "' not found");
    }
}
