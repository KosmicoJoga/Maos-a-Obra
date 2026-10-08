package org.example.maosaobra.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.maosaobra.dto.PerfilUpdateDTO;
import org.example.maosaobra.dto.SenhaUptadeDTO;
import org.example.maosaobra.service.PessoaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PutMapping("/senha")
    public ResponseEntity<?> alterarSenha(@RequestBody SenhaUptadeDTO dto, Authentication auth, HttpServletRequest request) {

        pessoaService.alterarSenha(auth.getName(), dto);

        // encerra a sessão: a pessoa precisa entrar de novo já utilizando a senha nova
        HttpSession sessao = request.getSession(false);
        if (sessao != null) sessao.invalidate();
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok().build();
    }
}
