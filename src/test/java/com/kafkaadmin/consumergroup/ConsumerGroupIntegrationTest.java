package com.kafkaadmin.consumergroup;

import com.kafkaadmin.BaseIntegrationTest;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

class ConsumerGroupIntegrationTest extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    private static final String TEST_TOPIC = "consumer-group-test-topic";
    private static final String TEST_GROUP = "integration-test-group";

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();

        Properties adminProps = new Properties();
        adminProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());

        try (AdminClient adminClient = AdminClient.create(adminProps)) {
            // Create test topic
            try {
                NewTopic newTopic = new NewTopic(TEST_TOPIC, 2, (short) 1);
                adminClient.createTopics(List.of(newTopic)).all().get();
                Thread.sleep(500);
            } catch (Exception ignored) {
            }
        }

        // Create a consumer to register the consumer group
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, TEST_GROUP);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
            consumer.subscribe(List.of(TEST_TOPIC));
            consumer.poll(Duration.ofSeconds(5));
            consumer.commitSync();
        }
    }

    @Test
    void listConsumerGroups_shouldIncludeTestGroup() {
        ConsumerGroupResponse[] groups = restClient.get()
                .uri("/api/v1/consumer-groups")
                .retrieve()
                .body(ConsumerGroupResponse[].class);

        assertThat(groups).isNotNull();
        assertThat(groups).extracting(ConsumerGroupResponse::groupId).contains(TEST_GROUP);
    }

    @Test
    void getConsumerGroup_shouldReturnGroupDetails() {
        ConsumerGroupDetailResponse group = restClient.get()
                .uri("/api/v1/consumer-groups/" + TEST_GROUP)
                .retrieve()
                .body(ConsumerGroupDetailResponse.class);

        assertThat(group).isNotNull();
        assertThat(group.groupId()).isEqualTo(TEST_GROUP);
        assertThat(group.state()).isNotNull();
    }

    @Test
    void getConsumerGroupOffsets_shouldReturnOffsets() {
        ConsumerGroupOffsetResponse[] offsets = restClient.get()
                .uri("/api/v1/consumer-groups/" + TEST_GROUP + "/offsets")
                .retrieve()
                .body(ConsumerGroupOffsetResponse[].class);

        assertThat(offsets).isNotNull();
    }

    @Test
    void getConsumerGroup_whenNotFound_shouldReturn404() {
        var response = restClient.get()
                .uri("/api/v1/consumer-groups/non-existent-group")
                .exchange((request, resp) -> {
                    return resp.getStatusCode();
                });

        assertThat(response.value()).isEqualTo(404);
    }
}
