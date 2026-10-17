package org.example.maosaobra.dto;

public record ExcluirContaDTO(String senha) {

    @Override
    public String toString() { // não ter risco da senha aparecer em logs
        return "ExcluirContaDTO[***]";
    }

}
