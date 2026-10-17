package org.example.maosaobra.repository;

import org.example.maosaobra.model.Obra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObraRepository extends JpaRepository<Obra, Long> {

    List<Obra> findByTrabalhadorId(Long idTrabalhador);

    void deleteByClienteId(Long idCliente);

}
