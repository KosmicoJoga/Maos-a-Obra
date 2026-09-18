package org.example.maosaobra.model;

import jakarta.persistence.*;
import lombok.*;

// JPA
@Entity
@Table(name = "trabalhador")

// LOMBOK
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Trabalhador extends Pessoa{

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_pessoa")
    private Pessoa pessoa;

    @ManyToOne
    @JoinColumn(name = "id_servico")
    private Servico servico;

    @Lob
    private byte[] certificados;

}
