package org.egov.pgr.persistence.repository;

import org.egov.pgr.persistence.queue.contract.SevaRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class LoggingMessageQueueRepositoryTest {

    @Mock
    KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    public void test_should_save_message() throws Exception {
        LoggingMessageQueueRepository<SevaRequest> loggingMessageQueueRepository =
                new LoggingMessageQueueRepository<>(kafkaTemplate);
        SevaRequest sevaRequest = new SevaRequest();

        loggingMessageQueueRepository.save(sevaRequest, "suffix");

        verify(kafkaTemplate).send("suffix", sevaRequest);
    }
}