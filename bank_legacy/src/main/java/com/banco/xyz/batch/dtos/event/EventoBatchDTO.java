package com.banco.xyz.batch.dtos.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoBatchDTO {
    private Long id;
    private String tipoProceso;
    private String fecha;
}

