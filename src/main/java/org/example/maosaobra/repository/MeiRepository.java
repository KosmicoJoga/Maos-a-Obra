package org.example.maosaobra.repository;

import org.example.maosaobra.model.Mei;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeiRepository extends JpaRepository<Mei, Long> {
}
