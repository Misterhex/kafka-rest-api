package com.kafkaadmin.topic;

import com.kafkaadmin.common.KafkaAdminPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Service providing topic management operations.
 */
@Service
public class TopicService {

    private final KafkaAdminPort kafkaAdminPort;

    /**
     * Creates a service with the given Kafka admin port.
     *
     * @param kafkaAdminPort the Kafka admin port
     */
    public TopicService(KafkaAdminPort kafkaAdminPort) {
        this.kafkaAdminPort = kafkaAdminPort;
    }

    /**
     * Lists all topics.
     *
     * @return list of topic DTOs sorted by name
     */
    public List<TopicResponse> listTopics() {
        return kafkaAdminPort.listTopicNames().stream()
                .map(kafkaAdminPort::getTopic)
                .map(TopicResponse::from)
                .sorted(Comparator.comparing(TopicResponse::name))
                .toList();
    }

    /**
     * Retrieves details for a specific topic.
     *
     * @param topicName the topic name
     * @return topic detail DTO
     * @throws TopicNotFoundException if the topic does not exist
     */
    public TopicDetailResponse getTopic(String topicName) {
        Topic topic = kafkaAdminPort.getTopic(topicName);
        return TopicDetailResponse.from(topic);
    }

    /**
     * Retrieves partition information for a topic.
     *
     * @param topicName the topic name
     * @return list of partition info DTOs sorted by partition number
     * @throws TopicNotFoundException if the topic does not exist
     */
    public List<TopicPartitionInfoResponse> getTopicPartitions(String topicName) {
        return kafkaAdminPort.getTopicPartitions(topicName).stream()
                .map(TopicPartitionInfoResponse::from)
                .sorted(Comparator.comparingInt(TopicPartitionInfoResponse::partition))
                .toList();
    }

    /**
     * Describes producers on a specific topic partition.
     *
     * @param topicName the topic name
     * @param partition the partition number
     * @return list of producer state DTOs sorted by producer ID
     * @throws TopicNotFoundException if the topic does not exist
     */
    public List<ProducerStateResponse> describeProducers(String topicName, int partition) {
        return kafkaAdminPort.describeProducers(topicName, partition).stream()
                .map(ProducerStateResponse::from)
                .sorted(Comparator.comparingLong(ProducerStateResponse::producerId))
                .toList();
    }

    /**
     * Describes replica log directories for a topic.
     *
     * @param topicName the topic name
     * @return list of replica log dir info DTOs sorted by broker ID and partition
     * @throws TopicNotFoundException if the topic does not exist
     */
    public List<ReplicaLogDirInfoResponse> describeReplicaLogDirs(String topicName) {
        return kafkaAdminPort.describeReplicaLogDirs(topicName).stream()
                .map(ReplicaLogDirInfoResponse::from)
                .sorted(Comparator.comparingInt(ReplicaLogDirInfoResponse::brokerId)
                        .thenComparingInt(ReplicaLogDirInfoResponse::partition))
                .toList();
    }
}
