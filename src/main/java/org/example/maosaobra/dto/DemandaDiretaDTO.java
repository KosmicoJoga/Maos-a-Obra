package org.example.maosaobra.dto;

import java.math.BigDecimal;

public record DemandaDiretaDTO(
        Long idTrabalhador,
        String endereco,
        String descricao,
        BigDecimal orcamento
) {
}
