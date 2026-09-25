package org.example.maosaobra.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

// JPA
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "pessoa")

// LOMBOK
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pessoa")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false)
    private Integer idade;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String senha;

    @Column(precision = 3, scale = 2)
    private BigDecimal avaliacao;

}
