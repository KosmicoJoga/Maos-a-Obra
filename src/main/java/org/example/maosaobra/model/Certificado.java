package org.example.maosaobra.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "certificado")

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_certificado")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prestador", nullable = false)
    private Trabalhador trabalhador;

    @Column(name = "curso", nullable = false, length = 150)
    private String curso;

    @Lob
    @Column(name = "foto", columnDefinition = "MEDIUMBLOB", nullable = false)
    private byte[] arquivo;

    @Column(name = "nome_arquivo", nullable = false)
    private String nomeArquivo;

    @Column(name = "tipo_conteudo", nullable = false, length = 100)
    private String tipoConteudo;
}
