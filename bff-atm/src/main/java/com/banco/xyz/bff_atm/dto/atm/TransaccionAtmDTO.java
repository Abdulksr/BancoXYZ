package com.banco.xyz.bff_atm.dto.atm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionAtmDTO {
    private Double monto;
    private String tipo;
}
