package org.example.maosaobra.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidatura")
@NoArgsConstructor
@Getter
@Setter
public class Candidatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_candidatura")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_demanda")
    private Demanda demanda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_trabalhador")
    private Trabalhador trabalhador;

    @Column(name = "data_candidatura")
    private LocalDateTime dataCandidatura;

}
