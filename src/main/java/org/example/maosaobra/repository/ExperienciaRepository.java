package org.example.maosaobra.repository;

import org.example.maosaobra.model.Experiencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExperienciaRepository extends JpaRepository<Experiencia,Long> {

    List<Experiencia> findAllByTrabalhador_IdOrderByDataInicioDesc(Long idTrabalhador);
}
