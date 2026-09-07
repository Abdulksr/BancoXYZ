package com.banco.xyz.bff_atm.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.banco.xyz.bff_atm.clients.MsbankLegacyClient;
import com.banco.xyz.bff_atm.dto.core.EstadoCuentaCoreDTO;
import com.banco.xyz.bff_atm.dto.core.InteresesCoreDTO;
import com.banco.xyz.bff_atm.dto.core.TransaccionesCoreDTO;
import com.banco.xyz.bff_atm.dto.atm.EstadoCuentaAtmDTO;
import com.banco.xyz.bff_atm.dto.atm.InteresesAtmDTO;
import com.banco.xyz.bff_atm.dto.atm.TransaccionAtmDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BffAtmService {

    private final MsbankLegacyClient client;

    public List<EstadoCuentaAtmDTO> obtenerEstadoCuentas() {
        return client.obtenerEstadosCuenta().stream()
                .map(this::mapToEstadoCuentaAtmDTO)
                .collect(Collectors.toList());
    }

    public EstadoCuentaAtmDTO obtenerEstadoCuentaPorId(Long id) {
        return mapToEstadoCuentaAtmDTO(client.obtenerEstadoCuentaPorId(id));
    }

    public List<InteresesAtmDTO> obtenerIntereses() {
        return client.obtenerIntereses().stream()
                .map(this::mapToInteresesAtmDTO)
                .collect(Collectors.toList());
    }

    public InteresesAtmDTO obtenerInteresPorId(Long id) {
        return mapToInteresesAtmDTO(client.obtenerInteresPorId(id));
    }

    public List<TransaccionAtmDTO> obtenerTransacciones() {
        return client.obtenerTransacciones().stream()
                .map(this::mapToTransaccionAtmDTO)
                .collect(Collectors.toList());
    }

    public TransaccionAtmDTO obtenerTransaccionPorId(Long id) {
        return mapToTransaccionAtmDTO(client.obtenerTransaccionPorId(id));
    }

    private EstadoCuentaAtmDTO mapToEstadoCuentaAtmDTO(EstadoCuentaCoreDTO core) {
        return new EstadoCuentaAtmDTO(core.getCuentaId(), core.getSaldoFinal());
    }

    private InteresesAtmDTO mapToInteresesAtmDTO(InteresesCoreDTO core) {
        return new InteresesAtmDTO(core.getCuentaId(), core.getSaldo());
    }

    private TransaccionAtmDTO mapToTransaccionAtmDTO(TransaccionesCoreDTO core) {
        return new TransaccionAtmDTO(core.getMonto(), core.getTipo());
    }
}
