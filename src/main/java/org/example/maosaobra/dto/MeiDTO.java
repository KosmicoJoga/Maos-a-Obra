package org.example.maosaobra.dto;

import org.example.maosaobra.model.Trabalhador;

public record MeiDTO(
        Trabalhador trabalhador,
        String cnpj
) {
}
