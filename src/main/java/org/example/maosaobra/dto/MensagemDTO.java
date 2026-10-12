package org.example.maosaobra.dto;

import java.time.LocalDateTime;

public record MensagemDTO(
        Long id,
        String remetente,
        String conteudo,
        LocalDateTime dataEnvio,
        boolean minha
) {
}
