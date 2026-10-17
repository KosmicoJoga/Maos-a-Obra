package org.example.maosaobra.repository;

import org.example.maosaobra.model.Demanda;
import org.example.maosaobra.model.StatusDemanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandaRepository extends JpaRepository<Demanda, Long> {

    List<Demanda> findByStatusOrderByDataSolicitacaoDesc(StatusDemanda status);

    List<Demanda> findByTrabalhadorIdAndStatusOrderByDataSolicitacaoDesc(Long idTrabalhador, StatusDemanda status);

    boolean existsByClienteIdAndStatus(Long idCliente, StatusDemanda status);

    boolean existsByTrabalhadorIdAndStatus(Long idTrabalhador, StatusDemanda status);

    List<Demanda> findByTrabalhadorId(Long idTrabalhador);

    void deleteByClienteId(Long idCliente);
}
