package com.banco.xyz.bff_mobile.dto.mobile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InteresesMobileDTO {
    private Long cuentaId;
    private Double saldo;
    private String tipo;
}
