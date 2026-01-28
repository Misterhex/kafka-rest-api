package com.kafkaadmin.sharegroup;

import com.kafkaadmin.common.KafkaAdminException;

/**
 * Exception thrown when a requested share group cannot be found.
 */
public class ShareGroupNotFoundException extends KafkaAdminException {

    /**
     * Creates an exception for the given share group ID.
     *
     * @param groupId the ID of the share group that was not found
     */
    public ShareGroupNotFoundException(String groupId) {
        super("Share group '" + groupId + "' not found");
    }
}
