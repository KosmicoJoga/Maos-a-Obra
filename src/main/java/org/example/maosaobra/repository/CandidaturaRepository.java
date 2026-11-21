package org.example.maosaobra.repository;

import org.example.maosaobra.model.Candidatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CandidaturaRepository extends JpaRepository<Candidatura,Long> {

    boolean existsByDemandaIdAndTrabalhadorId(Long demandaId, Long trabalhadorId);

    List<Candidatura> findByDemandaIdInOrderByDataCandidaturaAsc(Collection<Long> idsDemanda);

    List<Candidatura> findByTrabalhadorIdAndDemandaIdIn(Long idTrabalhador, Collection<Long> idsDemanda);

}
