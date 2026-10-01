package org.example.maosaobra.controller;

import org.example.maosaobra.dto.DemandaRequestDTO;
import org.example.maosaobra.model.Cliente;
import org.example.maosaobra.model.Demanda;
import org.example.maosaobra.security.PessoaDetails;
import org.example.maosaobra.service.ClienteService;
import org.example.maosaobra.service.DemandaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/demandas")
public class DemandaController {

    private final DemandaService demandaService;
    private final ClienteService clienteService;

    public DemandaController(DemandaService demandaService, ClienteService clienteService) {
        this.demandaService = demandaService;
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody DemandaRequestDTO dto, Authentication authentication){

        PessoaDetails details = (PessoaDetails) authentication.getPrincipal();
        Cliente cliente = clienteService.buscarPorId(details.getPessoa().getId());

        Demanda demanda = demandaService.criarDemandaPublica(cliente, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", demanda.getId(),
                "status", demanda.getStatus()
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleValidacao(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
    }
}
