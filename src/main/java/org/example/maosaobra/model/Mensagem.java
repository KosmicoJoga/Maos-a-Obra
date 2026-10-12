package org.example.maosaobra.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensagem")
@NoArgsConstructor
@Getter
@Setter
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensagem")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_conversa")
    private Conversa conversa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_remetente")
    private Pessoa remetente;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    private boolean lida;
}
