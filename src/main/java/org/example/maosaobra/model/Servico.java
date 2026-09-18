package org.example.maosaobra.model;

import jakarta.persistence.*;
import lombok.*;

// JPA
@Entity
@Table(name = "servico")

// LOMBOK
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servico")
    private Long id;

    @Column(nullable = false, length = 100)
    private String tipo;

}
