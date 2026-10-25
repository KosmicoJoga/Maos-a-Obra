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

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_demanda")
    private Demanda demanda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Avaliador avaliador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_trabalhador")
    private Trabalhador trabalhador;

    @Column(nullable = false)
    private Integer nota;

    @Column(length = 255)
    private String descricao;

    @Column(name = "data_avaliacao")
    private LocalDateTime dataAvaliacao;

}
