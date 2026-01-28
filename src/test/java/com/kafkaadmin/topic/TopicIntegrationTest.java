package com.kafkaadmin.topic;

import com.kafkaadmin.BaseIntegrationTest;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

class TopicIntegrationTest extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    private static final String TEST_TOPIC = "integration-test-topic";

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();

        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());

        try (AdminClient adminClient = AdminClient.create(props)) {
            // Delete topic if exists
            try {
                adminClient.deleteTopics(List.of(TEST_TOPIC)).all().get();
                Thread.sleep(1000);
            } catch (Exception ignored) {
            }

            // Create test topic
            NewTopic newTopic = new NewTopic(TEST_TOPIC, 3, (short) 1);
            adminClient.createTopics(List.of(newTopic)).all().get();
            Thread.sleep(1000);
        }
    }

    @Test
    void listTopics_shouldIncludeTestTopic() {
        TopicResponse[] topics = restClient.get()
                .uri("/api/v1/topics")
                .retrieve()
                .body(TopicResponse[].class);

        assertThat(topics).isNotNull();
        assertThat(topics).extracting(TopicResponse::name).contains(TEST_TOPIC);
    }

    @Test
    void getTopic_shouldReturnTopicDetails() {
        TopicDetailResponse topic = restClient.get()
                .uri("/api/v1/topics/" + TEST_TOPIC)
                .retrieve()
                .body(TopicDetailResponse.class);

        assertThat(topic).isNotNull();
        assertThat(topic.name()).isEqualTo(TEST_TOPIC);
        assertThat(topic.partitionCount()).isEqualTo(3);
        assertThat(topic.replicationFactor()).isEqualTo(1);
    }

    @Test
    void getTopicPartitions_shouldReturnPartitionDetails() {
        TopicPartitionInfoResponse[] partitions = restClient.get()
                .uri("/api/v1/topics/" + TEST_TOPIC + "/partitions")
                .retrieve()
                .body(TopicPartitionInfoResponse[].class);

        assertThat(partitions).isNotNull();
        assertThat(partitions).hasSize(3);
        assertThat(partitions).extracting(TopicPartitionInfoResponse::partition)
                .containsExactly(0, 1, 2);
    }

    @Test
    void getTopic_whenNotFound_shouldReturn404() {
        var response = restClient.get()
                .uri("/api/v1/topics/non-existent-topic")
                .exchange((request, resp) -> {
                    return resp.getStatusCode();
                });

        assertThat(response.value()).isEqualTo(404);
    }
}
