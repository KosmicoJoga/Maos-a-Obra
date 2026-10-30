package org.example.maosaobra.repository;

import org.example.maosaobra.model.Avaliacao;
import org.example.maosaobra.model.Avaliador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    boolean existsByDemandaIdAndAvaliador(Long idDemanda, Avaliador avaliador);

    List<Avaliacao> findByDemandaIdInAndAvaliador(Collection<Long> idsDemanda, Avaliador avaliador);

    @Query("select avg(a.nota) from Avaliacao a where a.trabalhador.id = :id "
            + "and a.avaliador = org.example.maosaobra.model.Avaliador.CLIENTE")
    Double mediaDoTrabalhador(@Param("id") Long id);

    @Query("select avg(a.nota) from Avaliacao a where a.cliente.id = :id "
            + "and a.avaliador = org.example.maosaobra.model.Avaliador.TRABALHADOR")
    Double mediaDoCliente(@Param("id") Long id);

    List<Avaliacao> findByCliente_IdAndAvaliador(Long idPessoa, Avaliador avaliador);

    List<Avaliacao> findByTrabalhador_IdAndAvaliador(Long idPessoa, Avaliador avaliador);
}
