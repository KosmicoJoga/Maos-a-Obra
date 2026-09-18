package org.example.maosaobra.repository;

import org.example.maosaobra.model.Contratacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratacaoRepository extends JpaRepository<Contratacao, Long> {
}
