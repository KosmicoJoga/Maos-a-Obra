package org.example.maosaobra.controller;

import org.example.maosaobra.dto.NotificacaoDTO;
import org.example.maosaobra.security.PessoaDetails;
import org.example.maosaobra.service.NotificacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @GetMapping
    public List<NotificacaoDTO> listar(Authentication auth) {
        return notificacaoService.listar(idLogado(auth)).stream()
                .map(n -> new NotificacaoDTO(n.getId(), n.getTipo().name(), n.getMensagem(),
                        n.getLink(), n.isLida(), n.getDataCriacao()))
                .toList();
    }

    @PostMapping("/{id}/lida")
    public ResponseEntity<?> marcarLida(@PathVariable Long id, Authentication auth) {
        notificacaoService.marcarComoLida(id, idLogado(auth));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/lidas")
    public ResponseEntity<?> marcarTodas(Authentication auth) {
        notificacaoService.marcarTodasComoLidas(idLogado(auth));
        return ResponseEntity.ok().build();
    }

    private Long idLogado(Authentication auth) {
        return ((PessoaDetails) auth.getPrincipal()).getPessoa().getId();
    }
}
