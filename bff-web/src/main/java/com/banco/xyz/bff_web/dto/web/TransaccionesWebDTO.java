package com.banco.xyz.bff_web.dto.web;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionesWebDTO {
    private Long id;
    private LocalDate fecha;
    private Double monto;
    private String tipo;
}
