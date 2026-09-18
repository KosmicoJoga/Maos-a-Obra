package org.example.maosaobra.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// JPA
@Entity
@Table(name = "avaliacao")

// LOMBOK
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avaliacao")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "trabalhador", nullable = false)
    private Trabalhador trabalhador;

    @Column(nullable = false)
    private Integer nota;

    @Column(length = 255)
    private String descricao;

    @Column(name = "data_avaliacao")
    private LocalDateTime dataAvaliacao;

}
