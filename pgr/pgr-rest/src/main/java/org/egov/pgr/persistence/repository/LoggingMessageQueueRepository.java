package org.egov.pgr.persistence.repository;

import org.springframework.kafka.core.KafkaTemplate;

public class LoggingMessageQueueRepository<T> {

    private KafkaTemplate<String, Object> kafkaTemplate;

    public LoggingMessageQueueRepository(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void save(T request, String queueNameSuffix) {
        kafkaTemplate.send(queueNameSuffix, request);
    }
}
