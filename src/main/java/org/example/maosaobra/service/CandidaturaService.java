package org.example.maosaobra.service;


import org.example.maosaobra.model.*;
import org.example.maosaobra.repository.CandidaturaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CandidaturaService {

    private final CandidaturaRepository candidaturaRepository;
    private final DemandaService demandaService;
    private final TrabalhadorService trabalhadorService;
    private final NotificacaoService notificacaoService;

    public CandidaturaService(CandidaturaRepository c, DemandaService d, TrabalhadorService t, NotificacaoService n) {
        this.candidaturaRepository = c;
        this.demandaService = d;
        this.trabalhadorService = t;
        this.notificacaoService = n;
    }

    @Transactional
    public Candidatura candidatar(Long idDemanda, Long idTrabalhador) {

        Demanda demanda = demandaService.buscarPorId(idDemanda);
        Trabalhador trabalhador = trabalhadorService.buscarPorId(idTrabalhador);

        if (demanda == null || demanda.getStatus() != StatusDemanda.ABERTA)
            throw new IllegalArgumentException("Esta demanda não está mais aberta!");

        if (candidaturaRepository.existsByDemandaIdAndTrabalhadorId(idDemanda, idTrabalhador))
            throw new IllegalArgumentException("Você já se candidatou a esta demanda!");

        Candidatura candidatura = new Candidatura();
        candidatura.setDemanda(demanda);
        candidatura.setTrabalhador(trabalhador);
        candidatura.setDataCandidatura(LocalDateTime.now());

        notificacaoService.criar(demanda.getCliente(), TipoNotificacao.CANDIDATURA,
                trabalhador.getNome() + " se candidatou à sua demanda de " + demanda.getObra().getServico().getTipo(),
                "/minhasDemandas");

        return candidaturaRepository.save(candidatura);
    }

    @Transactional
    public Demanda escolher(Long idDemanda, Long idTrabalhador, Long idCliente) {

        Demanda demanda = demandaService.buscarPorId(idDemanda);

        if (demanda == null || !demanda.getCliente().getId().equals(idCliente))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Demanda não encontrada!");

        if (demanda.getStatus() != StatusDemanda.ABERTA)
            throw new IllegalArgumentException("Esta demanda não está mais aberta!");

        if (!candidaturaRepository.existsByDemandaIdAndTrabalhadorId(idDemanda, idTrabalhador))
            throw new IllegalArgumentException("Este prestador não se candidatou a esta demanda!");

        Trabalhador escolhido = trabalhadorService.buscarPorId(idTrabalhador);

        demanda.setTrabalhador(escolhido);
        demanda.setStatus(StatusDemanda.ACEITA);
        demanda.getObra().setTrabalhador(escolhido); // a obra da demanda ganha o candidato escolhido pelo cliente

        Demanda salva = demandaService.salvar(demanda);

        notificacaoService.criar(escolhido, TipoNotificacao.ESCOLHA,
                demanda.getCliente().getNome() + " escolheu você para a demanda de " + demanda.getObra().getServico().getTipo(),
                "/demandasAceitas");

        return salva;
    }

    // para a tela do prestador: em quais destas demandas ele já se candidatou
    public Set<Long> idsJaCandidatados(Long idTrabalhador, List<Demanda> demandas) {
        List<Long> ids = demandas.stream().map(Demanda::getId).toList();
        if (ids.isEmpty()) return Set.of();

        return candidaturaRepository.findByTrabalhadorIdAndDemandaIdIn(idTrabalhador, ids).stream()
                .map(c -> c.getDemanda().getId())
                .collect(Collectors.toSet());
    }

    // para a tela do cliente: candidatos de cada demanda aberta
    public Map<Long, List<Candidatura>> candidatosPorDemanda(List<Demanda> demandas) {
        List<Long> ids = demandas.stream()
                .filter(d -> d.getStatus() == StatusDemanda.ABERTA)
                .map(Demanda::getId)
                .toList();
        if (ids.isEmpty()) return Map.of();

        return candidaturaRepository.findByDemandaIdInOrderByDataCandidaturaAsc(ids).stream()
                .collect(Collectors.groupingBy(c -> c.getDemanda().getId()));
    }
}
