package com.banco.xyz.bff_web.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.banco.xyz.bff_web.clients.MsbankLegacyClient;
import com.banco.xyz.bff_web.dto.core.EstadoCuentaCoreDTO;
import com.banco.xyz.bff_web.dto.core.InteresesCoreDTO;
import com.banco.xyz.bff_web.dto.core.TransaccionesCoreDTO;
import com.banco.xyz.bff_web.dto.web.EstadoCuentaWebDTO;
import com.banco.xyz.bff_web.dto.web.InteresesWebDTO;
import com.banco.xyz.bff_web.dto.web.TransaccionesWebDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BffWebService {

    private final MsbankLegacyClient client;

    public List<EstadoCuentaWebDTO> obtenerEstadoCuentas() {
        return client.obtenerEstadosCuenta().stream()
                .map(this::mapToEstadoCuentaWebDTO)
                .collect(Collectors.toList());
    }

    public EstadoCuentaWebDTO obtenerEstadoCuentaPorId(Long id) {
        return mapToEstadoCuentaWebDTO(client.obtenerEstadoCuentaPorId(id));
    }

    public List<InteresesWebDTO> obtenerIntereses() {
        return client.obtenerIntereses().stream()
                .map(this::mapToInteresesWebDTO)
                .collect(Collectors.toList());
    }

    public InteresesWebDTO obtenerInteresPorId(Long id) {
        return mapToInteresesWebDTO(client.obtenerInteresPorId(id));
    }

    public List<TransaccionesWebDTO> obtenerTransacciones() {
        return client.obtenerTransacciones().stream()
                .map(this::mapToTransaccionesWebDTO)
                .collect(Collectors.toList());
    }

    public TransaccionesWebDTO obtenerTransaccionPorId(Long id) {
        return mapToTransaccionesWebDTO(client.obtenerTransaccionPorId(id));
    }

    private EstadoCuentaWebDTO mapToEstadoCuentaWebDTO(EstadoCuentaCoreDTO core) {
        if (core == null) return null;
        return new EstadoCuentaWebDTO(core.getId(), core.getCuentaId(), core.getCantidadTransacciones(),
                core.getTotalIngresos(), core.getTotalRetiros(), core.getFechaProceso(), core.getAnio(),
                core.getSaldoFinal());
    }

    private InteresesWebDTO mapToInteresesWebDTO(InteresesCoreDTO core) {
        if (core == null) return null;
        return new InteresesWebDTO(core.getCuentaId(), core.getNombre(), core.getSaldo(), core.getEdad(),
                core.getTipo());
    }

    private TransaccionesWebDTO mapToTransaccionesWebDTO(TransaccionesCoreDTO core) {
        if (core == null) return null;
        return new TransaccionesWebDTO(core.getId(), core.getFecha(), core.getMonto(), core.getTipo());
    }
}
