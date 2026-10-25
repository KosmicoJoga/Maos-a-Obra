package org.example.maosaobra.service;

import org.example.maosaobra.model.*;
import org.example.maosaobra.repository.AvaliacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final PessoaService pessoaService;
    private final DemandaService demandaService;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository, PessoaService pessoaService, DemandaService demandaService) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.pessoaService = pessoaService;
        this.demandaService = demandaService;
    }

    public Avaliacao salvar(Avaliacao avaliacao) {
        return this.avaliacaoRepository.save(avaliacao);
    }

    public void excluir(Avaliacao avaliacao) {
        this.avaliacaoRepository.delete(avaliacao);
    }

    public List<Avaliacao> buscarTodos() {
        return this.avaliacaoRepository.findAll();
    }

    public Avaliacao buscarPorId(Long id) {
        return this.avaliacaoRepository.findById(id).orElse(null);
    }

    public Demanda podeAvaliar(Long idDemanda, Pessoa pessoa) {

        Demanda demanda = demandaService.buscarPorId(idDemanda);
        Avaliador papel = demanda == null ? null : papelDe(demanda, pessoa);

        if (papel == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Demanda não encontrada!");

        if (demanda.getStatus() != StatusDemanda.CONCLUIDA)
            throw new IllegalArgumentException("Só é possível avaliar depois que a demanda for concluída!");

        if (avaliacaoRepository.existsByDemandaIdAndAvaliador(idDemanda, papel))
            throw new IllegalArgumentException("Você já avaliou esta demanda!");

        return demanda;
    }

    @Transactional
    public Avaliacao avaliar(Long idDemanda, Pessoa pessoa, Integer nota, String descricao) {

        if (nota == null || nota < 1 || nota > 5)
            throw new IllegalArgumentException("A nota deve ser de 1 a 5!");
        if (descricao != null && descricao.length() > 255)
            throw new IllegalArgumentException("A observação pode ter no máximo 255 caracteres!");

        Demanda demanda = podeAvaliar(idDemanda, pessoa);
        Avaliador papel = papelDe(demanda, pessoa);


        Avaliacao a = new Avaliacao();
        a.setDemanda(demanda);
        a.setAvaliador(papel);
        a.setCliente(demanda.getCliente());
        a.setTrabalhador(demanda.getTrabalhador());
        a.setNota(nota);
        a.setDescricao(descricao == null || descricao.isBlank() ? null : descricao.trim());
        a.setDataAvaliacao(LocalDateTime.now());

        avaliacaoRepository.save(a);

        if (papel == Avaliador.CLIENTE) {
            Long id = demanda.getTrabalhador().getId();
            pessoaService.atualizarMedia(id, avaliacaoRepository.mediaDoTrabalhador(id));
        } else {
            Long id = demanda.getCliente().getId();
            pessoaService.atualizarMedia(id, avaliacaoRepository.mediaDoCliente(id));
        }

        return a;
    }

    // para as telas saberem quais demandas já foram avaliadas por este lado
    public Set<Long> idsJaAvaliadas(List<Demanda> demandas, Avaliador papel) {
        List<Long> ids = demandas.stream().map(Demanda::getId).toList();
        if (ids.isEmpty()) return Set.of();

        return avaliacaoRepository.findByDemandaIdInAndAvaliador(ids, papel).stream()
                .map(a -> a.getDemanda().getId())
                .collect(Collectors.toSet());
    }

    // Quem é a pessoa nesta demanda? (null = não participa)
    private Avaliador papelDe(Demanda demanda, Pessoa pessoa) {
        if (demanda.getCliente().getId().equals(pessoa.getId()))
            return Avaliador.CLIENTE;
        if (demanda.getTrabalhador() != null && demanda.getTrabalhador().getId().equals(pessoa.getId()))
            return Avaliador.TRABALHADOR;
        return null;
    }
}
