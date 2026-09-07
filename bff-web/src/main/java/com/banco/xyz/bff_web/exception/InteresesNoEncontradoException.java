package com.banco.xyz.bff_web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InteresesNoEncontradoException extends RuntimeException {

    public InteresesNoEncontradoException(Long id) {
        super("No se encontraron intereses con cuentaId: " + id);
    }
}
