package org.example.maosaobra.repository;

import org.example.maosaobra.model.Notificacao;
import org.example.maosaobra.model.TipoNotificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findTop30ByDestinatarioIdOrderByIdDesc(Long idPessoa);

    Optional<Notificacao> findByIdAndDestinatarioId(Long id, Long idPessoa);

    boolean existsByDestinatarioIdAndTipoAndLinkAndLidaFalse(Long idPessoa, TipoNotificacao tipo, String link);

    @Modifying
    @Query("update Notificacao n set n.lida = true where n.destinatario.id = :idPessoa and n.lida = false")
    int marcarTodasComoLidas(@Param("idPessoa") Long idPessoa);

    @Modifying
    @Query("update Notificacao n set n.lida = true where n.destinatario.id = :idPessoa and n.link = :link and n.lida = false")
    int marcarLidasPorLink(@Param("idPessoa") Long idPessoa, @Param("link") String link);
}
