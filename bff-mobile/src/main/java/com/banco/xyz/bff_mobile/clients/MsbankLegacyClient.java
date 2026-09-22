package com.banco.xyz.bff_mobile.clients;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.banco.xyz.bff_mobile.dto.core.EstadoCuentaCoreDTO;
import com.banco.xyz.bff_mobile.dto.core.InteresesCoreDTO;
import com.banco.xyz.bff_mobile.dto.core.TransaccionesCoreDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MsbankLegacyClient {

    private final RestClient restClient;

    @CircuitBreaker(name = "msbankLegacyClient", fallbackMethod = "fallbackObtenerEstadosCuenta")
    @Retry(name = "msbankLegacyClient")
    @RateLimiter(name = "msbankLegacyClient")
    public List<EstadoCuentaCoreDTO> obtenerEstadosCuenta() {
        return restClient.get()
                .uri("api/core/estadoCuentas")
                .retrieve()
                .body(new ParameterizedTypeReference<List<EstadoCuentaCoreDTO>>() {
                });
    }

    public List<EstadoCuentaCoreDTO> fallbackObtenerEstadosCuenta(Throwable t) {
        log.warn("Error al obtener estados de cuenta", t);
        return List.of();
    }

    @CircuitBreaker(name = "msbankLegacyClient", fallbackMethod = "fallbackEstadoCuentaPorId")
    @Retry(name = "msbankLegacyClient")
    @RateLimiter(name = "msbankLegacyClient")
    public EstadoCuentaCoreDTO obtenerEstadoCuentaPorId(Long id) {
        return restClient.get()
                .uri("api/core/estadoCuenta/{id}", id)
                .retrieve()
                .body(EstadoCuentaCoreDTO.class);
    }

    public EstadoCuentaCoreDTO fallbackEstadoCuentaPorId(Long id, Throwable t) {
        log.warn("Error en el msbank-legacy al obtener estado de cuenta. Retornando null.", t);
        return null;
    }

    @CircuitBreaker(name = "msbankLegacyClient", fallbackMethod = "fallbackObtenerIntereses")
    @Retry(name = "msbankLegacyClient")
    @RateLimiter(name = "msbankLegacyClient")
    public List<InteresesCoreDTO> obtenerIntereses() {
        return restClient.get()
                .uri("api/core/intereses")
                .retrieve()
                .body(new ParameterizedTypeReference<List<InteresesCoreDTO>>() {
                });
    }

    public List<InteresesCoreDTO> fallbackObtenerIntereses(Throwable t) {
        log.warn("Error al obtener intereses", t);
        return List.of();
    }

    @CircuitBreaker(name = "msbankLegacyClient", fallbackMethod = "fallbackObtenerInteresPorId")
    @Retry(name = "msbankLegacyClient")
    @RateLimiter(name = "msbankLegacyClient")
    public InteresesCoreDTO obtenerInteresPorId(Long id) {
        return restClient.get()
                .uri("api/core/interes/{id}", id)
                .retrieve()
                .body(InteresesCoreDTO.class);
    }

    public InteresesCoreDTO fallbackObtenerInteresPorId(Long id, Throwable t) {
        InteresesCoreDTO interesFallback = new InteresesCoreDTO();
        interesFallback.setCuentaId(id);
        return interesFallback;
    }

    @CircuitBreaker(name = "msbankLegacyClient", fallbackMethod = "fallbackObtenerTransacciones")
    @Retry(name = "msbankLegacyClient")
    @RateLimiter(name = "msbankLegacyClient")
    public List<TransaccionesCoreDTO> obtenerTransacciones() {
        return restClient.get()
                .uri("api/core/transacciones")
                .retrieve()
                .body(new ParameterizedTypeReference<List<TransaccionesCoreDTO>>() {
                });
    }

    public List<TransaccionesCoreDTO> fallbackObtenerTransacciones(Throwable t) {
        log.warn("Error al obtener transacciones", t);
        return List.of();
    }

    @CircuitBreaker(name = "msbankLegacyClient", fallbackMethod = "fallbackObtenerTransaccionPorId")
    @Retry(name = "msbankLegacyClient")
    public TransaccionesCoreDTO obtenerTransaccionPorId(Long id) {
        return restClient.get()
                .uri("api/core/transaccion/{id}", id)
                .retrieve()
                .body(TransaccionesCoreDTO.class);
    }

    public TransaccionesCoreDTO fallbackObtenerTransaccionPorId(Long id, Throwable t) {
        TransaccionesCoreDTO transaccionFallback = new TransaccionesCoreDTO();
        transaccionFallback.setId(id);
        return transaccionFallback;
    }

}
