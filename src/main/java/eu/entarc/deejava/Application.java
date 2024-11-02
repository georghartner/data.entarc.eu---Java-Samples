package eu.entarc.deejava;

import lombok.extern.java.Log;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableTransactionManagement
@Log
@EnableScheduling
@EnableKafka
@AutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Configuration
    class KafkaConfig {

        @Value("${dataservices.kafka.bootstrapurls}")
        private String bootstrapAddress = "localhost:9097";

        @Value("${dataservices.kafka.groupid}")
        private String groupId = "dsid";

        @Value("${dataservices.kafka.truststore.file}")
        private String trustStoreFile;
        @Value("${dataservices.kafka.truststore.password}")
        private String trustStorePassword;
        @Value("${dataservices.kafka.username}")
        private String kafkaUsername;
        @Value("${dataservices.kafka.password}")
        private String kafkaPassword;

        @Bean
        public ConsumerFactory<String, String> consumerFactory() {
            log.info("Creating Kafka consumer factory " + bootstrapAddress + "|" + groupId + "|" + trustStoreFile + "|" + trustStorePassword);

            Map<String, Object> props = new HashMap<>();
            props.put(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                    bootstrapAddress);
            props.put(
                    ConsumerConfig.GROUP_ID_CONFIG,
                    groupId);
            props.put(
                    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                    StringDeserializer.class);
            props.put(
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                    StringDeserializer.class);
            props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
            //props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            props.put("sasl.mechanism", "PLAIN");
            props.put("security.protocol", "SASL_SSL");
            props.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required \n" +
                    "        username=\"" + kafkaUsername + "\" \n" +
                    "        password=\"" + kafkaPassword + "\";");

            props.put("ssl.truststore.location", trustStoreFile);
            props.put("ssl.truststore.password", trustStorePassword);
            return new DefaultKafkaConsumerFactory<>(props);
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, String>
        kafkaListenerContainerFactory() {
            ConcurrentKafkaListenerContainerFactory<String, String> factory =
                    new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory());
            return factory;
        }
    }

}
