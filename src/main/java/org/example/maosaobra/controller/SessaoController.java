package org.example.maosaobra.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.maosaobra.dto.LoginDTO;
import org.example.maosaobra.model.Pessoa;
import org.example.maosaobra.model.Trabalhador;
import org.example.maosaobra.security.PessoaDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class SessaoController {

    private final AuthenticationManager authenticationManager;

    public SessaoController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDTO dto, HttpServletRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha())
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        request.getSession(true)
                .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        PessoaDetails details = (PessoaDetails) auth.getPrincipal();
        Pessoa pessoa = details.getPessoa();
        String tipo = (pessoa instanceof Trabalhador) ? "TRABALHADOR" : "CLIENTE";

        return ResponseEntity.ok(Map.of(
                "nome", pessoa.getNome(),
                "email",  pessoa.getEmail(),
                "tipo", tipo
        ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("erro", "Email ou senha inválidos!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        PessoaDetails details = (PessoaDetails) authentication.getPrincipal();
        Pessoa pessoa = details.getPessoa();
        String tipo = (pessoa instanceof Trabalhador) ? "TRABALHADOR" : "CLIENTE";
        return ResponseEntity.ok(Map.of("nome", pessoa.getNome(), "tipo", tipo));
    }
}
