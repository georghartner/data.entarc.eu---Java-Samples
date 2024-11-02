package eu.entarc.deejava;

import lombok.extern.java.Log;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Component
@Log
public class DEEKafkaListener {

    private static final String SERVICE_NAME = "EDA";

    /**
     * Available streams:
     * - SERVICE_NAME_permission-market-documents
     * - SERVICE_NAME_validated-historical-data
     * - SERVICE_NAME_raw-data-in-proprietary-format
     * - SERVICE_NAME_aiida-data
     *
     * @param record
     */
    @KafkaListener(topics = SERVICE_NAME + "_permission-market-documents")
    public void listenToPermissionDocuments(ConsumerRecord<?, String> record) {
        String receivedString = record.value();
        log.info("Received: " + receivedString);

    }
}
