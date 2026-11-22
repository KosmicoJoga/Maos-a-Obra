package org.example.maosaobra.dto;

import java.time.LocalDateTime;

public record NotificacaoDTO(
        Long id,
        String tipo,
        String mensagem,
        String link,
        boolean lida,
        LocalDateTime dataCriacao
) {}
