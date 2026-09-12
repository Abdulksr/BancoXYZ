package com.banco.xyz.bff_mobile.clients;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.banco.xyz.bff_mobile.dto.core.EstadoCuentaCoreDTO;
import com.banco.xyz.bff_mobile.dto.core.InteresesCoreDTO;
import com.banco.xyz.bff_mobile.dto.core.TransaccionesCoreDTO;
import com.banco.xyz.bff_mobile.exception.EstadoCuentaNoEncontradoException;
import com.banco.xyz.bff_mobile.exception.InteresesNoEncontradoException;
import com.banco.xyz.bff_mobile.exception.TransaccionNoEncontradaException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MsbankLegacyClient {

    private final RestClient restClient;

    public List<EstadoCuentaCoreDTO> obtenerEstadosCuenta() {
        return restClient.get()
                .uri("api/core/estadoCuentas")
                .retrieve()
                .body(new ParameterizedTypeReference<List<EstadoCuentaCoreDTO>>() {
                });
    }

    public EstadoCuentaCoreDTO obtenerEstadoCuentaPorId(Long id) {
        try {
            return restClient.get()
                    .uri("api/core/estadoCuenta/{id}", id)
                    .retrieve()
                    .body(EstadoCuentaCoreDTO.class);
        } catch (Exception e) {
            throw new EstadoCuentaNoEncontradoException(id);
        }
    }

    public List<InteresesCoreDTO> obtenerIntereses() {
        return restClient.get()
                .uri("api/core/intereses")
                .retrieve()
                .body(new ParameterizedTypeReference<List<InteresesCoreDTO>>() {
                });
    }

    public InteresesCoreDTO obtenerInteresPorId(Long id) {
        try {
            return restClient.get()
                    .uri("api/core/interes/{id}", id)
                    .retrieve()
                    .body(InteresesCoreDTO.class);
        } catch (Exception e) {
            throw new InteresesNoEncontradoException(id);
        }
    }

    public List<TransaccionesCoreDTO> obtenerTransacciones() {
        return restClient.get()
                .uri("api/core/transacciones")
                .retrieve()
                .body(new ParameterizedTypeReference<List<TransaccionesCoreDTO>>() {
                });
    }

    public TransaccionesCoreDTO obtenerTransaccionPorId(Long id) {
        try {
            return restClient.get()
                    .uri("api/core/transaccion/{id}", id)
                    .retrieve()
                    .body(TransaccionesCoreDTO.class);
        } catch (Exception e) {
            throw new TransaccionNoEncontradaException(id);
        }
    }

}
