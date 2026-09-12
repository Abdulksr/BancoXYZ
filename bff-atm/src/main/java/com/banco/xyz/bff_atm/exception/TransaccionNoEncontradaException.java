package com.banco.xyz.bff_atm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TransaccionNoEncontradaException extends RuntimeException {
    public TransaccionNoEncontradaException(Long id) {
        super("No se encontro la transaccion con id: " + id);
    }
}
