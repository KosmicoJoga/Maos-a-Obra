package org.example.maosaobra.dto;

public record SenhaUptadeDTO(
        String senhaAtual,
        String senhaNova,
        String senhaConfirmada
) {

    @Override
    public String toString() {
        return "SenhaUpdateDTO[***]";
    }
}
