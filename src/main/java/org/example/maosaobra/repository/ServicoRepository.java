package org.example.maosaobra.repository;

import org.example.maosaobra.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    List<Servico> findAllByOrderByTipoAsc();
}
