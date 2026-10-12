package org.example.maosaobra.repository;

import org.example.maosaobra.model.Conversa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversaRepository extends JpaRepository<Conversa,Long> {

    Optional<Conversa> findByClienteIdAndTrabalhadorId(Long idCliente, Long idTrabalhador);

    List<Conversa> findByClienteIdOrTrabalhadorIdOrderByDataCriacaoDesc(Long idCliente, Long idTrabalhador);

}
