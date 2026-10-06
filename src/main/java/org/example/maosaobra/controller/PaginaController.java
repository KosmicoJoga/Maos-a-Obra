package org.example.maosaobra.controller;

import org.example.maosaobra.model.Pessoa;
import org.example.maosaobra.model.Trabalhador;
import org.example.maosaobra.service.DemandaService;
import org.example.maosaobra.service.PessoaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    private final DemandaService demandaService;
    private final PessoaService pessoaService;

    public PaginaController(DemandaService demandaService, PessoaService pessoaService) {
        this.demandaService = demandaService;
        this.pessoaService = pessoaService;
    }

    @GetMapping("/inicioCliente")
    public String inicioCliente(Authentication authentication, Model model) {
        Pessoa pessoa = pessoaLogada(authentication);

        model.addAttribute("nome",  pessoa.getNome());

        return "inicioCliente"; // nome do arquivo na pasta templates, e sem o ".html"
    }

    @GetMapping("/inicioPrestador")
    public String inicioPrestador(Authentication authentication, Model model) {
        Pessoa pessoa = pessoaLogada(authentication);

        model.addAttribute("nome",  pessoa.getNome());
        model.addAttribute("demandas", demandaService.buscarAbertas());

        return "inicioPrestador";
    }

    @GetMapping("/solicitacoesDemanda")
    public String solicitacoesDemanda(Authentication authentication, Model model) {
        Pessoa pessoa = pessoaLogada(authentication);

        model.addAttribute("nome",  pessoa.getNome());
        model.addAttribute("demandas", demandaService.buscarAbertas());

        return "solicitacoesDemanda";
    }

    @GetMapping("/perfil/logado")
    public String perfilLogado(Authentication authentication, Model model) {
        Pessoa pessoa = pessoaLogada(authentication);

        model.addAttribute("nome",  pessoa.getNome());
        model.addAttribute("pessoa",  pessoa);

        return "perfilLogado";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil(Authentication authentication, Model model) {
        Pessoa pessoa = pessoaLogada(authentication);

        model.addAttribute("pessoa", pessoa);
        model.addAttribute("ehTrabalhador", pessoa instanceof Trabalhador);

        return "editarPerfil";
    }

    // função somente utilizada por essa própria classe
    private Pessoa pessoaLogada(Authentication authentication) {
        return pessoaService.buscarPorEmail(authentication.getName());
    }
}
