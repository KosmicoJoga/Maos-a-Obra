package org.example.maosaobra.dto;

import java.math.BigDecimal;

public record PessoaDTO(
        String nome,
        Integer idade,
        String email,
        String senha,
        BigDecimal avaliacao
) {
}
