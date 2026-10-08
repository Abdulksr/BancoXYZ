package com.banco.xyz.bff_web.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoBatchDTO {
    private Long id;
    private String fecha;
    private String tipoProceso;

}

