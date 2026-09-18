package org.example.maosaobra.model;

import jakarta.persistence.*;
import lombok.*;

// JPA
@Entity
@Table(name = "mei")

// LOMBOK
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Mei extends Trabalhador{

    @Id
    @Column(name = "id_mei")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_mei")
    private Trabalhador trabalhador;

    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;
}
