package com.banco.xyz.batch.service;

import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import com.banco.xyz.batch.config.KafkaConsumerConfig;
import com.banco.xyz.batch.dtos.event.EventoTransaccionDto;
import com.banco.xyz.batch.service.TransaccionesJobService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final TransaccionesJobService transaccionesJobService;

    @KafkaListener(id = "transaccionesListener", topics = KafkaConsumerConfig.TOPIC, groupId = KafkaConsumerConfig.CONSUMER_GROUP_ID)

    public void consumirEventoTransacciones(EventoTransaccionDto evento, Acknowledgment acknowledgment) {
        try {

            log.info("Â¡Nuevo evento recibido desde AWS Kafka!: {}", evento.getTipoProceso());
            if (evento.getTipoProceso().equals("BATCH_TRANSACCIONES")) {
                transaccionesJobService.ejecutar();
            }
            acknowledgment.acknowledge();
            log.info("Acknowledge realizado con Ã©xito. Kafka, puedes borrar el mensaje.");
        } catch (Exception e) {
            log.error("Error al procesar el mensaje de Kafka: {}", e.getMessage());
            throw new RuntimeException("Error al procesar el mensaje de Kafka", e);
        }
    }

}
