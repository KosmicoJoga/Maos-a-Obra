package org.example.maosaobra.controller;

import jakarta.validation.Valid;
import org.example.maosaobra.dto.CadastroDTO;
import org.example.maosaobra.model.Trabalhador;
import org.example.maosaobra.service.CadastroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/trabalhadores")
public class TrabalhadorController {

    private final CadastroService cadastroService;

    public TrabalhadorController(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody CadastroDTO dto) {
        Trabalhador trabalhador = cadastroService.cadastrarTrabalhador(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", trabalhador.getId()));
    }


}
