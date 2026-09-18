package org.example.maosaobra.dto;

import org.example.maosaobra.model.Cliente;
import org.example.maosaobra.model.Servico;
import org.example.maosaobra.model.Trabalhador;

import java.math.BigDecimal;

public record ObraDTO(
        Cliente cliente,
        Trabalhador trabalhador,
        Servico servico,
        String descricao,
        String endereco,
        BigDecimal orcamento
) {
}
