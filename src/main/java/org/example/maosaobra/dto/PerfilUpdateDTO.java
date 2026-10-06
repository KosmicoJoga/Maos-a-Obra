package org.example.maosaobra.dto;

public record PerfilUpdateDTO(
        String nome,
        Integer idade,
        String sobreMim,
        Long idServico   // só vale para trabalhador; pode vir null
) {
}
