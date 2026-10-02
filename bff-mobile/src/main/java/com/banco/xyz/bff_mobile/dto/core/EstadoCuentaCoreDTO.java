package com.banco.xyz.bff_mobile.dto.core;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoCuentaCoreDTO {
    private Long id;
    private String cuentaId;
    private int cantidadTransacciones;
    private Double totalIngresos;
    private Double totalRetiros;
    private LocalDate fechaProceso;
    private int anio;
    private Double saldoFinal;
}
