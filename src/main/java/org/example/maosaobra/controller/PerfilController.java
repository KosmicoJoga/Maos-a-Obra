package org.example.maosaobra.controller;

import org.example.maosaobra.dto.PerfilUpdateDTO;
import org.example.maosaobra.service.PessoaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/perfil")
public class PerfilController {

    private final PessoaService pessoaService;

    public PerfilController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PutMapping
    public ResponseEntity<?> atualizar(@RequestBody PerfilUpdateDTO dto, Authentication authentication) {
        pessoaService.atualizarPerfil(authentication.getName(), dto);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleValidacao(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
    }
}
