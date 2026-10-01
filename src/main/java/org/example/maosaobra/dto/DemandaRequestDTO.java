package org.example.maosaobra.dto;

import java.math.BigDecimal;

public record DemandaRequestDTO(
        Long idServico,
        String endereco,
        String descricao,
        BigDecimal orcamento
) {
}
