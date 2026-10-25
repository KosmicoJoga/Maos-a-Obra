package org.example.maosaobra.dto;

import org.example.maosaobra.model.Cliente;
import org.example.maosaobra.model.Trabalhador;

import java.time.LocalDateTime;

public record AvaliacaoRequestDTO(
        Long idDemanda,
        Integer nota,
        String descricao
) {
}
