package org.example.maosaobra.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.maosaobra.dto.ExcluirContaDTO;
import org.example.maosaobra.dto.PerfilUpdateDTO;
import org.example.maosaobra.dto.SenhaUptadeDTO;
import org.example.maosaobra.model.Pessoa;
import org.example.maosaobra.service.ContaService;
import org.example.maosaobra.service.PessoaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/perfil")
public class PerfilController {

    private final PessoaService pessoaService;
    private final ContaService contaService;

    public PerfilController(PessoaService pessoaService, ContaService contaService) {
        this.pessoaService = pessoaService;
        this.contaService = contaService;
    }

    @PutMapping
    public ResponseEntity<?> atualizar(@RequestBody PerfilUpdateDTO dto, Authentication authentication) {
        pessoaService.atualizarPerfil(authentication.getName(), dto);
        return ResponseEntity.ok().build();
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

    @PostMapping("/excluir")
    public ResponseEntity<?> excluirConta(@RequestBody ExcluirContaDTO dto, Authentication auth, HttpServletRequest request) {

        contaService.excluirConta(auth.getName(), dto);

        // só chega aqui se a exclusão deu certo, se der exceção desfaz tudo e a sessão se mantém
        HttpSession sessao = request.getSession(false);
        if (sessao != null) sessao.invalidate();
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok().build();
    }

    @PostMapping("/foto")
    public ResponseEntity<?> atualizarFoto(@RequestParam("foto")MultipartFile arquivo, Authentication auth) throws IOException {

        if (arquivo.isEmpty())
            throw new IllegalArgumentException("Selecione uma imagem!");

        if (arquivo.getSize() > 5 * 1024 * 1024)
            throw new IllegalArgumentException("A imagem deve ter no máximo 5MB!");

        String tipo = arquivo.getContentType();

        if (tipo == null || !(tipo.equals("image/jpeg") || tipo.equals("image/png") || tipo.equals("image/webp")))
            throw new IllegalArgumentException("Envie uma imagem PNG, JPEG ou WEBP!");

        pessoaService.atualizarFotoPerfil(auth.getName(), arquivo.getBytes(), tipo);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/foto/{id}")
    public ResponseEntity<byte[]> buscarFoto(@PathVariable Long id) {

        Pessoa pessoa = pessoaService.buscarPorId(id);

        if (pessoa == null || pessoa.getFotoPerfil() == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Foto de perfil não encontrada.");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(pessoa.getTipoFotoPerfil()))
                .body(pessoa.getFotoPerfil());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleValidacao(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
    }
}
