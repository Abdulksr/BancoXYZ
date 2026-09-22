package com.banco.xyz.bff_mobile.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.banco.xyz.bff_mobile.clients.MsbankLegacyClient;
import com.banco.xyz.bff_mobile.dto.core.EstadoCuentaCoreDTO;
import com.banco.xyz.bff_mobile.dto.core.InteresesCoreDTO;
import com.banco.xyz.bff_mobile.dto.core.TransaccionesCoreDTO;
import com.banco.xyz.bff_mobile.dto.mobile.EstadoCuentaMobileDTO;
import com.banco.xyz.bff_mobile.dto.mobile.InteresesMobileDTO;
import com.banco.xyz.bff_mobile.dto.mobile.TransaccionMobileDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BffMobileService {

    private final MsbankLegacyClient client;

    public List<EstadoCuentaMobileDTO> obtenerEstadoCuentas() {
        return client.obtenerEstadosCuenta().stream()
                .map(this::mapToEstadoCuentaMobileDTO)
                .collect(Collectors.toList());
    }

    public EstadoCuentaMobileDTO obtenerEstadoCuentaPorId(Long id) {
        return mapToEstadoCuentaMobileDTO(client.obtenerEstadoCuentaPorId(id));
    }

    public List<InteresesMobileDTO> obtenerIntereses() {
        return client.obtenerIntereses().stream()
                .map(this::mapToInteresesMobileDTO)
                .collect(Collectors.toList());
    }

    public InteresesMobileDTO obtenerInteresPorId(Long id) {
        return mapToInteresesMobileDTO(client.obtenerInteresPorId(id));
    }

    public List<TransaccionMobileDTO> obtenerTransacciones() {
        return client.obtenerTransacciones().stream()
                .map(this::mapToTransaccionMobileDTO)
                .collect(Collectors.toList());
    }

    public TransaccionMobileDTO obtenerTransaccionPorId(Long id) {
        return mapToTransaccionMobileDTO(client.obtenerTransaccionPorId(id));
    }

    private EstadoCuentaMobileDTO mapToEstadoCuentaMobileDTO(EstadoCuentaCoreDTO core) {
        if (core == null) return null;
        return new EstadoCuentaMobileDTO(core.getCuentaId(), core.getAnio(), core.getSaldoFinal());
    }

    private InteresesMobileDTO mapToInteresesMobileDTO(InteresesCoreDTO core) {
        if (core == null) return null;
        return new InteresesMobileDTO(core.getCuentaId(), core.getSaldo(), core.getTipo());
    }

    private TransaccionMobileDTO mapToTransaccionMobileDTO(TransaccionesCoreDTO core) {
        if (core == null) return null;
        return new TransaccionMobileDTO(core.getFecha(), core.getMonto(), core.getTipo());
    }
}
