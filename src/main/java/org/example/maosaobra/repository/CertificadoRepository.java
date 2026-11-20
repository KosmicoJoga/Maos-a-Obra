package org.example.maosaobra.repository;

import org.example.maosaobra.model.Certificado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificadoRepository extends JpaRepository<Certificado, Long> {

    List<Certificado> findAllByTrabalhador_IdOrderByIdDesc(Long idTrabalhador);
}
