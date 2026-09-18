package org.example.maosaobra.dto;

import org.example.maosaobra.model.Cliente;
import org.example.maosaobra.model.Trabalhador;

import java.time.LocalDateTime;

public record AvaliacaoDTO(
        Cliente cliente,
        Trabalhador trabalhador,
        Integer nota,
        String descricao,
        LocalDateTime dataAvaliacao
) {
}
