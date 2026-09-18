package org.example.maosaobra.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// JPA
@Entity
@Table(name = "contratacao")

// LOMBOK
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Contratacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contratacao")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_trabalhador")
    private Trabalhador trabalhador;

    @ManyToOne
    @JoinColumn(name = "id_obra")
    private Obra obra;

    @Enumerated(EnumType.STRING)
    private StatusContratacao status;

    @Column(name = "data_solicitacao")
    private LocalDateTime dataSolicitacao;
}
