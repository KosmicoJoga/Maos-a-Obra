package org.example.maosaobra.service;

import org.example.maosaobra.model.Contratacao;
import org.example.maosaobra.repository.ContratacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContratacaoService {

    private final ContratacaoRepository contratacaoRepository;

    public ContratacaoService(ContratacaoRepository contratacaoRepository) {
        this.contratacaoRepository = contratacaoRepository;
    }

    public Contratacao salvar(Contratacao contratacao) {
        return this.contratacaoRepository.save(contratacao);
    }

    public void excluir(Contratacao contratacao) {
        this.contratacaoRepository.delete(contratacao);
    }

    public List<Contratacao> buscarTodos() {
        return this.contratacaoRepository.findAll();
    }

    public Contratacao buscarPorId(Long id) {
        return this.contratacaoRepository.findById(id).orElse(null);
    }
}
