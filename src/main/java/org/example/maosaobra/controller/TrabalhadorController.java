package org.example.maosaobra.controller;

import org.example.maosaobra.model.Trabalhador;
import org.example.maosaobra.service.TrabalhadorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trabalhadores")
public class TrabalhadorController {

    private final TrabalhadorService trabalhadorService;

    public TrabalhadorController(TrabalhadorService clienteService) {
        this.trabalhadorService = clienteService;
    }

    @PostMapping
    public Trabalhador cadastrar(@RequestBody Trabalhador trabalhador){
        return trabalhadorService.salvar(trabalhador);
    }
}
