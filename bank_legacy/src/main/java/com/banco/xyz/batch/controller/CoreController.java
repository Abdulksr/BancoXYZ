package com.banco.xyz.batch.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.xyz.batch.entities.EstadoCuentaEntity;
import com.banco.xyz.batch.entities.InteresesEntity;
import com.banco.xyz.batch.entities.TransaccionesEntity;
import com.banco.xyz.batch.repository.EstadoCuentaRepository;
import com.banco.xyz.batch.repository.InteresesRepository;
import com.banco.xyz.batch.repository.TransaccionesRepository;

@RestController
@RequestMapping("api/core")
public class CoreController {

    @Autowired
    private EstadoCuentaRepository estadoCuentaRepository;

    @Autowired
    private InteresesRepository interesesRepository;

    @Autowired
    private TransaccionesRepository transaccionRepository;

    @GetMapping("estadoCuentas")
    public ResponseEntity<List<EstadoCuentaEntity>> listadoEstadoCuentas() {
        return ResponseEntity.ok(estadoCuentaRepository.findAll());
    }

    @GetMapping("estadoCuenta/{id}")
    public ResponseEntity<EstadoCuentaEntity> obtenerEstadoCuentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(estadoCuentaRepository.findById(id).orElseThrow());
    }

    @GetMapping("intereses")
    public ResponseEntity<List<InteresesEntity>> listadoIntereses() {
        return ResponseEntity.ok(interesesRepository.findAll());
    }

    @GetMapping("interes/{id}")
    public ResponseEntity<InteresesEntity> obtenerInteresPorId(@PathVariable Long id) {
        return ResponseEntity.ok(interesesRepository.findById(id).orElseThrow());
    }

    @GetMapping("transacciones")
    public ResponseEntity<List<TransaccionesEntity>> listadoTransacciones() {
        return ResponseEntity.ok(transaccionRepository.findAll());
    }

    @GetMapping("transaccion/{id}")
    public ResponseEntity<TransaccionesEntity> obtenerTransaccionPorId(@PathVariable Long id) {
        return ResponseEntity.ok(transaccionRepository.findById(id).orElseThrow());
    }

}
