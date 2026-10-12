package org.example.maosaobra.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversa", uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "id_trabalhador"}))
@NoArgsConstructor
@Getter
@Setter

public class Conversa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conversa")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_trabalhador")
    private Trabalhador trabalhador;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
}
