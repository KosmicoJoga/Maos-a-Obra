package org.example.maosaobra.dto;

import org.example.maosaobra.model.Pessoa;
import org.example.maosaobra.model.Servico;

public record TrabalhadorDTO(
        Pessoa pessoa,
        Servico servico,
        byte[] certificados
) {
}
