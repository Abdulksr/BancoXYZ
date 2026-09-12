package com.banco.xyz.bff_atm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EstadoCuentaNoEncontradoException extends RuntimeException {
    public EstadoCuentaNoEncontradoException(Long id) {
        super("No se encontro el estado de cuenta con id: " + id);
    }
}
