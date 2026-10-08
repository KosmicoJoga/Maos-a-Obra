package org.example.maosaobra.service;

import org.example.maosaobra.dto.DemandaRequestDTO;
import org.example.maosaobra.model.*;
import org.example.maosaobra.repository.DemandaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DemandaService {

    private final DemandaRepository demandaRepository;
    private final ServicoService servicoService;
    private final ObraService obraService;
    private final GeocodingService geocodingService;

    public DemandaService(DemandaRepository demandaRepository, ServicoService servicoService, ObraService obraService, GeocodingService geocodingService) {
        this.demandaRepository = demandaRepository;
        this.servicoService = servicoService;
        this.obraService = obraService;
        this.geocodingService = geocodingService;
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

        Obra obra = new Obra();
        obra.setCliente(cliente);
        obra.setServico(servico);
        obra.setEndereco(dto.endereco());
        obra.setDescricao(dto.descricao());
        obra.setOrcamento(dto.orcamento());

        Optional<double[]> coordenadas = geocodingService.buscarCoordenadas(dto.endereco());

        if (coordenadas.isPresent()) {
            obra.setLatitude(coordenadas.get()[0]);
            obra.setLongitude(coordenadas.get()[1]);
        }

        obra = obraService.salvar(obra);

        Demanda demanda = new Demanda();
        demanda.setCliente(cliente);
        demanda.setObra(obra);
        demanda.setStatus(StatusDemanda.ABERTA);
        demanda.setDataSolicitacao(LocalDateTime.now());
        //trabalhador fica null até algum trabalhador aceitar a demanda

        return demandaRepository.save(demanda);
    }

    public List<Demanda> buscarAbertas() {
        return demandaRepository.findByStatusOrderByDataSolicitacaoDesc(StatusDemanda.ABERTA);
    }
}
