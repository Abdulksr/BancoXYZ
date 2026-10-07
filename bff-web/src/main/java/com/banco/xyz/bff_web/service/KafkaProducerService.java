package com.banco.xyz.bff_web.service;

import java.time.Instant;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.banco.xyz.bff_web.config.KafkaProducerConfig;
import com.banco.xyz.bff_web.dto.event.EventoBatchDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, EventoBatchDTO> kafkaTemplate;

    @CircuitBreaker(name = "kafkaWebProducer", fallbackMethod = "fallbackEnviarMensaje")
    public void enviarEventoBatch(EventoBatchDTO evento) {
        log.info("Enviando evento a Kafka: {}", evento.getTipoProceso());
        evento.setId(System.nanoTime());
        evento.setFecha(Instant.now().toString());
        kafkaTemplate.send(KafkaProducerConfig.TOPIC, evento);
    }

    public void fallbackEnviarMensaje(EventoBatchDTO evento, Throwable t) {
        log.error("Error al enviar evento a Kafka: {}", evento);
    }

}



