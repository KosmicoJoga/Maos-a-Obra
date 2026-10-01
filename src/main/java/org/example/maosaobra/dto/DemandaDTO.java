package org.example.maosaobra.dto;

import org.example.maosaobra.model.Cliente;
import org.example.maosaobra.model.Obra;
import org.example.maosaobra.model.StatusDemanda;
import org.example.maosaobra.model.Trabalhador;

import java.time.LocalDateTime;

public record DemandaDTO(
        Cliente cliente,
        Trabalhador trabalhador,
        Obra obra,
        StatusDemanda status,
        LocalDateTime dataSolicitacao
) {
}
