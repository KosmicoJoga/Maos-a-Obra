package org.example.maosaobra.service;

import org.example.maosaobra.model.Notificacao;
import org.example.maosaobra.model.Pessoa;
import org.example.maosaobra.model.TipoNotificacao;
import org.example.maosaobra.repository.NotificacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    public NotificacaoService(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    @Transactional
    public void criar(Pessoa destinatario, TipoNotificacao tipo, String mensagem, String link) {
        Notificacao n = new Notificacao();
        n.setDestinatario(destinatario);
        n.setTipo(tipo);
        n.setMensagem(mensagem.length() > 255 ? mensagem.substring(0, 255) : mensagem);
        n.setLink(link);
        n.setDataCriacao(LocalDateTime.now());
        notificacaoRepository.save(n);
    }

    // chat: no máximo UMA notificação não lida por conversa (senão seria uma por mensagem)
    @Transactional
    public void criarMensagem(Pessoa destinatario, String mensagem, String link) {
        if (notificacaoRepository.existsByDestinatarioIdAndTipoAndLinkAndLidaFalse(
                destinatario.getId(), TipoNotificacao.MENSAGEM, link))
            return;

        criar(destinatario, TipoNotificacao.MENSAGEM, mensagem, link);
    }

    public List<Notificacao> listar(Long idPessoa) {
        return notificacaoRepository.findTop30ByDestinatarioIdOrderByIdDesc(idPessoa);
    }

    @Transactional
    public void marcarComoLida(Long idNotificacao, Long idPessoa) {
        Notificacao n = notificacaoRepository.findByIdAndDestinatarioId(idNotificacao, idPessoa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificação não encontrada!"));
        n.setLida(true);
    }

    @Transactional
    public void marcarTodasComoLidas(Long idPessoa) {
        notificacaoRepository.marcarTodasComoLidas(idPessoa);
    }

    @Transactional
    public void marcarLidasPorLink(Long idPessoa, String link) {
        notificacaoRepository.marcarLidasPorLink(idPessoa, link);
    }
}