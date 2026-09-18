package org.example.maosaobra.dto;

import org.example.maosaobra.model.Cliente;
import org.example.maosaobra.model.Obra;
import org.example.maosaobra.model.StatusContratacao;
import org.example.maosaobra.model.Trabalhador;

import java.time.LocalDateTime;

public record ContratacaoDTO(
        Cliente cliente,
        Trabalhador trabalhador,
        Obra obra,
        StatusContratacao status,
        LocalDateTime dataSolicitacao
) {
}
