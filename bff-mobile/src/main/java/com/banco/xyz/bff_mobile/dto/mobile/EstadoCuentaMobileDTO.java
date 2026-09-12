package com.banco.xyz.bff_mobile.dto.mobile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoCuentaMobileDTO {
    private String cuentaId;
    private int anio;
    private Double saldoFinal;
}
