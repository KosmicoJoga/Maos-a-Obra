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

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "idade", nullable = false)
    private Integer idade;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @Column(name = "avaliacao", precision = 3, scale = 2)
    private BigDecimal avaliacao;

    @Column(name = "sobre_mim", columnDefinition = "TEXT")
    private String sobreMim;
}
