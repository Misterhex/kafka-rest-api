package com.kafkaadmin.topic;

import com.kafkaadmin.common.KafkaAdminPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TopicService {

    private final KafkaAdminPort kafkaAdminPort;

    public TopicService(KafkaAdminPort kafkaAdminPort) {
        this.kafkaAdminPort = kafkaAdminPort;
    }

    public List<TopicDto> listTopics() {
        return kafkaAdminPort.listTopicNames().stream()
                .map(kafkaAdminPort::getTopic)
                .map(TopicDto::from)
                .sorted(Comparator.comparing(TopicDto::name))
                .toList();
    }

    public TopicDetailDto getTopic(String topicName) {
        Topic topic = kafkaAdminPort.getTopic(topicName);
        return TopicDetailDto.from(topic);
    }

    public List<TopicPartitionInfoDto> getTopicPartitions(String topicName) {
        return kafkaAdminPort.getTopicPartitions(topicName).stream()
                .map(TopicPartitionInfoDto::from)
                .sorted(Comparator.comparingInt(TopicPartitionInfoDto::partition))
                .toList();
    }
}
