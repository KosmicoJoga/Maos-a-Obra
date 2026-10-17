package org.example.maosaobra.service;

import org.example.maosaobra.dto.DemandaDiretaDTO;
import org.example.maosaobra.dto.DemandaRequestDTO;
import org.example.maosaobra.model.*;
import org.example.maosaobra.repository.DemandaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DemandaService {

    private final DemandaRepository demandaRepository;
    private final ServicoService servicoService;
    private final ObraService obraService;
    private final GeocodingService geocodingService;
    private final TrabalhadorService trabalhadorService;

    public DemandaService(DemandaRepository demandaRepository, ServicoService servicoService, ObraService obraService, GeocodingService geocodingService, TrabalhadorService trabalhadorService) {
        this.demandaRepository = demandaRepository;
        this.servicoService = servicoService;
        this.obraService = obraService;
        this.geocodingService = geocodingService;
        this.trabalhadorService = trabalhadorService;
    }

    public Demanda salvar(Demanda demanda) {
        return this.demandaRepository.save(demanda);
    }

    public void excluir(Demanda demanda) {
        this.demandaRepository.delete(demanda);
    }

    public List<Demanda> buscarTodos() {
        return this.demandaRepository.findAll();
    }

    public Demanda buscarPorId(Long id) {
        return this.demandaRepository.findById(id).orElse(null);
    }

    @Transactional
    public Demanda criarDemandaPublica(Cliente cliente, DemandaRequestDTO dto) {

        Servico servico = servicoService.buscarPorId(dto.idServico());
        Obra obra = criarObra(cliente, servico, dto.endereco(), dto.descricao(), dto.orcamento());

        return criarDemanda(cliente, null, obra, StatusDemanda.ABERTA);

    }

    @Transactional
    public Demanda criarDemandaDireta(Cliente cliente, DemandaDiretaDTO dto) {

        Trabalhador trabalhador = trabalhadorService.buscarPorId(dto.idTrabalhador());
        if (trabalhador == null)
            throw new IllegalArgumentException("Prestador de serviços não encontrado!");

        if (trabalhador.getServico() == null)
            throw new IllegalArgumentException("Este prestador de serviços ainda não informou o seu tipo de serviço!");

        Obra obra = criarObra(cliente, trabalhador.getServico(), dto.endereco(), dto.descricao(), dto.orcamento());

        // DEMANDA PENDENTE = demanda enviada ao prestador de serviços específico, aguardando a resposta do mesmo
        return criarDemanda(cliente, trabalhador, obra, StatusDemanda.PENDENTE);
    }

    // MÉTODOS PRIVADOS USADOS SOMENTE NESTA CLASSE

    private Obra criarObra(Cliente c, Servico s, String endereco, String descricao, BigDecimal orcamento) {

        Obra obra = new Obra();
        obra.setCliente(c);
        obra.setServico(s);
        obra.setEndereco(endereco);
        obra.setDescricao(descricao);
        obra.setOrcamento(orcamento);

        Optional<double[]> coords = geocodingService.buscarCoordenadas(endereco);
        if (coords.isPresent()) {
            obra.setLatitude(coords.get()[0]);
            obra.setLongitude(coords.get()[1]);
        }

        return obraService.salvar(obra);
    }

    private Demanda criarDemanda(Cliente cliente, Trabalhador trabalhador, Obra obra, StatusDemanda status) {
        Demanda demanda = new Demanda();
        demanda.setCliente(cliente);
        demanda.setTrabalhador(trabalhador); //null se for demanda pública
        demanda.setObra(obra);
        demanda.setStatus(status);
        demanda.setDataSolicitacao(LocalDateTime.now());

        return demandaRepository.save(demanda);
    }

    public List<Demanda> buscarAbertas() {
        return demandaRepository.findByStatusOrderByDataSolicitacaoDesc(StatusDemanda.ABERTA);
    }

    public List<Demanda> buscarSolicitacoesPendentes(Long idTrabalhador) {
        return demandaRepository.findByTrabalhadorIdAndStatusOrderByDataSolicitacaoDesc(idTrabalhador, StatusDemanda.PENDENTE);
    }

    public boolean temServicoEmAndamento(Pessoa pessoa) {
        if (pessoa instanceof Cliente)
            return demandaRepository.existsByClienteIdAndStatus(pessoa.getId(), StatusDemanda.ACEITA);
        if (pessoa instanceof Trabalhador)
            return demandaRepository.existsByTrabalhadorIdAndStatus(pessoa.getId(), StatusDemanda.ACEITA);
        return false;
    }

    @Transactional
    public void removerDadosDoCliente(Long idCliente) {
        demandaRepository.deleteByClienteId(idCliente);
        obraService.excluirDoCliente(idCliente);
    }

    @Transactional
    public void desvincularTrabalhador(Long idTrabalhador) {
        for (Demanda d : demandaRepository.findByTrabalhadorId(idTrabalhador)) {
            if (d.getStatus() == StatusDemanda.PENDENTE)
                d.setStatus(StatusDemanda.RECUSADA);
            d.setTrabalhador(null);
        }
        obraService.desvincularTrabalhador(idTrabalhador);
    }
}
