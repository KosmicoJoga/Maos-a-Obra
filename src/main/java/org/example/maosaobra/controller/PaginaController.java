package org.example.maosaobra.controller;

import org.example.maosaobra.model.Pessoa;
import org.example.maosaobra.security.PessoaDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    @GetMapping("/inicioCliente")
    public String inicioCliente(Authentication authentication, Model model) {
        PessoaDetails details = (PessoaDetails) authentication.getPrincipal();
        Pessoa pessoa = details.getPessoa();

        model.addAttribute("nome",  pessoa.getNome());

        return "inicioCliente"; // nome do arquivo na pasta templates, e sem o ".html"
    }

    @GetMapping("/inicioPrestador")
    public String inicioPrestador(Authentication authentication, Model model) {
        PessoaDetails details = (PessoaDetails) authentication.getPrincipal();
        Pessoa pessoa = details.getPessoa();

        model.addAttribute("nome",  pessoa.getNome());

        return "inicioPrestador";
    }
}
