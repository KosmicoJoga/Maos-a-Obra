package org.example.maosaobra.dto;

import jakarta.validation.constraints.*;

public record CadastroDTO(

        @NotBlank(message = "Informe seu nome!")
        @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres!")
        String nome,

        @NotNull(message = "Informe sua idade!")
        @Min(value = 18, message = "É preciso ter 18 anos de idade ou mais para se cadastrar!")
        @Max(value = 120, message = "Informe uma idade válida!")
        Integer idade,

        @NotBlank(message = "Informe seu email!")
        @Email(message = "Informe um email válido")
        @Size(max = 255, message = "O email pode ter no máximo 255 caracteres!")
        String email,

        @NotBlank(message = "Informe uma senha!")
        @Size(min = 8, max = 64, message = "A senha deve ter entre 8 e 64 caracteres!")
        String senha,

        Long idServico
) {

    // Para a senha não aparecer em logs
    @Override
    public String toString() {
        return "CadastroDTO[***]";
    }
}
