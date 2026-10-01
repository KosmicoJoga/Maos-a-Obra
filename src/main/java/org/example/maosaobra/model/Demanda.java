package org.example.maosaobra.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// JPA
@Entity
@Table(name = "demanda")

// LOMBOK
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Demanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_demanda")
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
    private StatusDemanda status;

    @Column(name = "data_solicitacao")
    private LocalDateTime dataSolicitacao;
}
