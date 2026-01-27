package com.kafkaadmin.common;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaAdminClientConfig {

    @Bean
    public AdminClient adminClient(KafkaAdminProperties properties) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, properties.bootstrapServers());
        props.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, properties.requestTimeoutMs());
        props.put(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, properties.defaultApiTimeoutMs());
        props.put(AdminClientConfig.CLIENT_ID_CONFIG, properties.clientId());
        return AdminClient.create(props);
    }
}
