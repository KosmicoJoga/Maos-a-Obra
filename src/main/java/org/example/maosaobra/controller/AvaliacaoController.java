package org.example.maosaobra.controller;

import org.example.maosaobra.dto.AvaliacaoRequestDTO;
import org.example.maosaobra.model.Pessoa;
import org.example.maosaobra.service.AvaliacaoService;
import org.example.maosaobra.service.PessoaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;
    private final PessoaService pessoaService;

    public AvaliacaoController(AvaliacaoService avaliacaoService, PessoaService pessoaService) {
        this.avaliacaoService = avaliacaoService;
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<?> avaliar(@RequestBody AvaliacaoRequestDTO dto, Authentication auth) {

        Pessoa pessoa = pessoaService.buscarPorEmail(auth.getName());
        avaliacaoService.avaliar(dto.idDemanda(), pessoa, dto.nota(), dto.descricao());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleValidacao(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
    }
}
