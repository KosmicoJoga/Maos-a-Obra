package org.example.maosaobra.controller;

import org.example.maosaobra.dto.IniciarConversaDTO;
import org.example.maosaobra.dto.MensagemDTO;
import org.example.maosaobra.dto.MensagemRequestDTO;
import org.example.maosaobra.model.Conversa;
import org.example.maosaobra.model.Mensagem;
import org.example.maosaobra.model.Pessoa;
import org.example.maosaobra.repository.ConversaRepository;
import org.example.maosaobra.service.ChatService;
import org.example.maosaobra.service.PessoaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/conversas")
public class ChatController {

    private final ChatService chatService;
    private final PessoaService pessoaService;

    public ChatController(ChatService chatService, PessoaService pessoaService) {
        this.chatService = chatService;
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<?> iniciar(@RequestBody IniciarConversaDTO dto, Authentication auth) {

        Pessoa logada = pessoaService.buscarPorEmail(auth.getName());
        Pessoa outra = pessoaService.buscarPorId(dto.idOutro());

        Conversa conversa = chatService.iniciarConversa(logada, outra);
        return ResponseEntity.ok(Map.of("id", conversa.getId()));

    }

    @GetMapping("/{id}/mensagens")
    public List<MensagemDTO> listar(@PathVariable Long id, @RequestParam(defaultValue = "0") Long depoisDe, Authentication auth) {

        Pessoa logada = pessoaService.buscarPorEmail(auth.getName());
        return chatService.listarNovas(id, logada, depoisDe).stream()
                .map(m -> paraDTO(m, logada)).toList();
    }

    @PostMapping("/{id}/mensagens")
    public MensagemDTO enviar(@PathVariable Long id, @RequestBody MensagemRequestDTO dto, Authentication auth) {

        Pessoa logada = pessoaService.buscarPorEmail(auth.getName());
        return paraDTO(chatService.enviar(id, logada, dto.conteudo()), logada);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleValidacao(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
    }

    private MensagemDTO paraDTO(Mensagem m, Pessoa logada) {
        return new MensagemDTO(m.getId(), m.getRemetente().getNome(), m.getConteudo(),
                m.getDataEnvio(), m.getRemetente().getId().equals(logada.getId()));
    }
}
