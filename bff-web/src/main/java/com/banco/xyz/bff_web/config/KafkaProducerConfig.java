package com.banco.xyz.bff_web.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.banco.xyz.bff_web.dto.event.EventoTransaccionDTO;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public static final String TOPIC = "transacciones-topic";

    @Bean
    KafkaAdmin kafkaAdmin() {

        Map<String, Object> configs = new HashMap<>();
        configs.put("bootstrap.servers", bootstrapServers);

        return new KafkaAdmin(configs);
    }

    @Bean
    NewTopic topicTransacciones() {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", "43200000"); // 12 horas de retenciÃ³n de mensajes
        return new NewTopic(TOPIC, 3, (short) 3).configs(configs);
    }

    @Bean
    ProducerFactory<String, EventoTransaccionDTO> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, EventoTransaccionDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
