package com.banco.xyz.bff_mobile.dto.mobile;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionMobileDTO {
    private LocalDate fecha;
    private Double monto;
    private String tipo;
}
