package com.banco.xyz.bff_mobile.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.xyz.bff_mobile.dto.mobile.EstadoCuentaMobileDTO;
import com.banco.xyz.bff_mobile.dto.mobile.InteresesMobileDTO;
import com.banco.xyz.bff_mobile.dto.mobile.TransaccionMobileDTO;
import com.banco.xyz.bff_mobile.service.BffMobileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bff-mobile")
@RequiredArgsConstructor
public class BffMobileController {

    private final BffMobileService service;

    @GetMapping("/estadoCuentas")
    public ResponseEntity<List<EstadoCuentaMobileDTO>> listadoEstadoCuentas() {
        return ResponseEntity.ok(service.obtenerEstadoCuentas());
    }

    @GetMapping("/estadoCuenta/{id}")
    public ResponseEntity<EstadoCuentaMobileDTO> obtenerEstadoCuentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerEstadoCuentaPorId(id));
    }

    @GetMapping("/intereses")
    public ResponseEntity<List<InteresesMobileDTO>> listadoIntereses() {
        return ResponseEntity.ok(service.obtenerIntereses());
    }

    @GetMapping("/interes/{id}")
    public ResponseEntity<InteresesMobileDTO> obtenerInteresPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerInteresPorId(id));
    }

    @GetMapping("/transacciones")
    public ResponseEntity<List<TransaccionMobileDTO>> listadoTransacciones() {
        return ResponseEntity.ok(service.obtenerTransacciones());
    }

    @GetMapping("/transaccion/{id}")
    public ResponseEntity<TransaccionMobileDTO> obtenerTransaccionPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerTransaccionPorId(id));
    }
}
