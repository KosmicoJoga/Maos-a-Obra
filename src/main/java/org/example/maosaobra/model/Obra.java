package org.example.maosaobra.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

// JPA
@Entity
@Table(name = "obra")

// LOMBOK
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Obra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_obra")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_trabalhador")
    private Trabalhador trabalhador;

    @ManyToOne
    @JoinColumn(name = "id_servico")
    private Servico servico;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(nullable = false, length = 255)
    private String endereco;

    @Column(precision = 10, scale = 2)
    private BigDecimal orcamento;

}
