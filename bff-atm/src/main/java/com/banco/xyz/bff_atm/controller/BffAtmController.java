package com.banco.xyz.bff_atm.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.xyz.bff_atm.dto.atm.EstadoCuentaAtmDTO;
import com.banco.xyz.bff_atm.dto.atm.InteresesAtmDTO;
import com.banco.xyz.bff_atm.dto.atm.TransaccionAtmDTO;
import com.banco.xyz.bff_atm.service.BffAtmService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bff-atm")
@RequiredArgsConstructor
public class BffAtmController {

    private final BffAtmService service;

    @GetMapping("/estadoCuentas")
    public ResponseEntity<List<EstadoCuentaAtmDTO>> listadoEstadoCuentas() {
        return ResponseEntity.ok(service.obtenerEstadoCuentas());
    }

    @GetMapping("/estadoCuenta/{id}")
    public ResponseEntity<EstadoCuentaAtmDTO> obtenerEstadoCuentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerEstadoCuentaPorId(id));
    }

    @GetMapping("/intereses")
    public ResponseEntity<List<InteresesAtmDTO>> listadoIntereses() {
        return ResponseEntity.ok(service.obtenerIntereses());
    }

    @GetMapping("/interes/{id}")
    public ResponseEntity<InteresesAtmDTO> obtenerInteresPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerInteresPorId(id));
    }

    @GetMapping("/transacciones")
    public ResponseEntity<List<TransaccionAtmDTO>> listadoTransacciones() {
        return ResponseEntity.ok(service.obtenerTransacciones());
    }

    @GetMapping("/transaccion/{id}")
    public ResponseEntity<TransaccionAtmDTO> obtenerTransaccionPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerTransaccionPorId(id));
    }
}
