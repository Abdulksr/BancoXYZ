package com.banco.xyz.batch.service;

import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import com.banco.xyz.batch.config.KafkaConsumerConfig;
import com.banco.xyz.batch.dtos.event.EventoBatchDTO;
import com.banco.xyz.batch.service.TransaccionesJobService;
import com.banco.xyz.batch.service.InteresesJobService;
import com.banco.xyz.batch.service.EstadoCuentaJobService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final TransaccionesJobService transaccionesJobService;
    private final InteresesJobService interesesJobService;
    private final EstadoCuentaJobService estadoCuentaJobService;

    @KafkaListener(id = "batchListener", topics = KafkaConsumerConfig.TOPIC, groupId = KafkaConsumerConfig.CONSUMER_GROUP_ID)

    public void consumirEventoBatch(EventoBatchDTO evento, Acknowledgment acknowledgment) {
        try {

            log.info("¡Nuevo evento recibido desde AWS Kafka!: {}", evento.getTipoProceso());
            switch (evento.getTipoProceso()) {
                case "BATCH_TRANSACCIONES":
                    transaccionesJobService.ejecutar();
                    break;
                case "BATCH_INTERESES":
                    interesesJobService.ejecutar();
                    break;
                case "BATCH_ESTADOS_CUENTA":
                    estadoCuentaJobService.ejecutar();
                    break;
            }
            acknowledgment.acknowledge();
            log.info("Acknowledge realizado con éxito. Kafka, puedes borrar el mensaje.");
        } catch (Exception e) {
            log.error("Error al procesar el mensaje de Kafka: {}", e.getMessage());
            throw new RuntimeException("Error al procesar el mensaje de Kafka", e);
        }
    }

}


