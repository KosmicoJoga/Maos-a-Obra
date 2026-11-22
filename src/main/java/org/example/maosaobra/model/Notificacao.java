package org.example.maosaobra.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacao")
@NoArgsConstructor
@Getter
@Setter

public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacao")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa")
    private Pessoa destinatario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNotificacao tipo;

    @Column(nullable = false, length = 255)
    private String mensagem;

    @Column(nullable = false, length = 255)
    private String link;

    @Column(nullable = false)
    private boolean lida;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

}
