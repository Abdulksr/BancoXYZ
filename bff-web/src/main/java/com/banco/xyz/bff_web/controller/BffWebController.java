package com.banco.xyz.bff_web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.xyz.bff_web.dto.web.EstadoCuentaWebDTO;
import com.banco.xyz.bff_web.dto.web.InteresesWebDTO;
import com.banco.xyz.bff_web.dto.web.TransaccionesWebDTO;
import com.banco.xyz.bff_web.service.BffWebService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bff-web")
@RequiredArgsConstructor
public class BffWebController {

    private final BffWebService service;

    @GetMapping("/estadoCuentas")
    public ResponseEntity<List<EstadoCuentaWebDTO>> listadoEstadoCuentas() {
        return ResponseEntity.ok(service.obtenerEstadoCuentas());
    }

    @GetMapping("/estadoCuenta/{id}")
    public ResponseEntity<EstadoCuentaWebDTO> obtenerEstadoCuentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerEstadoCuentaPorId(id));
    }

    @GetMapping("/intereses")
    public ResponseEntity<List<InteresesWebDTO>> listadoIntereses() {
        return ResponseEntity.ok(service.obtenerIntereses());
    }

    @GetMapping("/interes/{id}")
    public ResponseEntity<InteresesWebDTO> obtenerInteresPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerInteresPorId(id));
    }

    @GetMapping("/transacciones")
    public ResponseEntity<List<TransaccionesWebDTO>> listadoTransacciones() {
        return ResponseEntity.ok(service.obtenerTransacciones());
    }

    @GetMapping("/transaccion/{id}")
    public ResponseEntity<TransaccionesWebDTO> obtenerTransaccionPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerTransaccionPorId(id));
    }
}
