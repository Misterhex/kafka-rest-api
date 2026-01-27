package com.kafkaadmin.consumergroup;

import java.util.List;

public record ConsumerGroupMember(
        String consumerId,
        String clientId,
        String host,
        List<String> assignments
) {
}
