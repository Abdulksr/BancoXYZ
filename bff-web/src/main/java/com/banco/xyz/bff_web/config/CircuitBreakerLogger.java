package com.banco.xyz.bff_web.config;

import org.springframework.stereotype.Component;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CircuitBreakerLogger {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @PostConstruct
    public void init() {
        // Escucha a los existentes
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(cb -> {
            cb.getEventPublisher().onStateTransition(event -> {
                log.warn(" [CORTACIRCUITOS] Cambio de estado detectado: {}", event.getStateTransition());
            });
        });

        // Escucha a los que se creen en el futuro
        circuitBreakerRegistry.getEventPublisher()
                .onEntryAdded(entryAddedEvent -> {
                    entryAddedEvent.getAddedEntry().getEventPublisher()
                            .onStateTransition(event -> {
                                log.warn(" [CORTACIRCUITOS] Cambio de estado detectado: {}",
                                        event.getStateTransition());
                            });
                });
    }
}
