package org.example.maosaobra.controller;

import org.example.maosaobra.dto.DemandaDiretaDTO;
import org.example.maosaobra.dto.DemandaRequestDTO;
import org.example.maosaobra.model.Cliente;
import org.example.maosaobra.model.Demanda;
import org.example.maosaobra.model.Pessoa;
import org.example.maosaobra.security.PessoaDetails;
import org.example.maosaobra.service.ClienteService;
import org.example.maosaobra.service.DemandaService;
import org.example.maosaobra.service.PessoaService;
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
    private final PessoaService pessoaService;

    public DemandaController(DemandaService demandaService, ClienteService clienteService, PessoaService pessoaService) {
        this.demandaService = demandaService;
        this.clienteService = clienteService;
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody DemandaRequestDTO dto, Authentication authentication) {

        PessoaDetails details = (PessoaDetails) authentication.getPrincipal();
        Cliente cliente = clienteService.buscarPorId(details.getPessoa().getId());

        Demanda demanda = demandaService.criarDemandaPublica(cliente, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", demanda.getId(),
                "status", demanda.getStatus()
        ));
    }

    @PostMapping("/direta")
    public ResponseEntity<?> criarDireta(@RequestBody DemandaDiretaDTO dto, Authentication auth) {

        PessoaDetails details = (PessoaDetails) auth.getPrincipal();
        Cliente cliente = clienteService.buscarPorId(details.getPessoa().getId());

        Demanda demanda = demandaService.criarDemandaDireta(cliente, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", demanda.getId(),
                "status", demanda.getStatus()
        ));
    }

    @PostMapping("/{id}/aceitar")
    public ResponseEntity<?> aceitar(@PathVariable Long id, Authentication auth) {
        return responder(id, auth, true);
    }

    @PostMapping("/{id}/recusar")
    public ResponseEntity<?> recusar(@PathVariable Long id, Authentication auth) {
        return responder(id, auth, false);
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizar(@PathVariable Long id, Authentication auth) {
        Pessoa pessoa = pessoaService.buscarPorEmail(auth.getName());
        Demanda demanda = demandaService.finalizarDemanda(id, pessoa);

        return ResponseEntity.ok(Map.of("id", demanda.getId(), "status", demanda.getStatus()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleValidacao(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
    }

    // função privada para usar somente nesta classe
    private ResponseEntity<?> responder(Long id, Authentication auth, boolean aceitar) {
        PessoaDetails details = (PessoaDetails) auth.getPrincipal();
        Demanda demanda = demandaService.responderSolicitacao(id, details.getPessoa().getId(), aceitar);

        return ResponseEntity.ok(Map.of(
                "id", demanda.getId(),
                "status", demanda.getStatus()
        ));
    }
}
