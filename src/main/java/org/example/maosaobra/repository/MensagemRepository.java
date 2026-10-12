package org.example.maosaobra.repository;


import org.example.maosaobra.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensagemRepository extends JpaRepository<Mensagem,Long> {

    List<Mensagem> findByConversaIdAndIdGreaterThanOrderByIdAsc(Long idConversa, Long depoisDe);
}
