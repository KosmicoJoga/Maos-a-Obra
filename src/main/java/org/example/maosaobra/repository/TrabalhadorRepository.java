package org.example.maosaobra.repository;

import org.example.maosaobra.model.Trabalhador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrabalhadorRepository extends JpaRepository<Trabalhador, Long> {

    List<Trabalhador> findAllByOrderByAvaliacaoDesc();
}
