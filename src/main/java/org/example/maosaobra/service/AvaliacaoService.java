package org.example.maosaobra.service;

import org.example.maosaobra.model.Avaliacao;
import org.example.maosaobra.repository.AvaliacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public Avaliacao salvar(Avaliacao avaliacao){
        return this.avaliacaoRepository.save(avaliacao);
    }

    public void excluir(Avaliacao avaliacao){
        this.avaliacaoRepository.delete(avaliacao);
    }

    public List<Avaliacao> buscarTodos(){
        return this.avaliacaoRepository.findAll();
    }

    public Avaliacao buscarPorId(Long id){
        return this.avaliacaoRepository.findById(id).orElse(null);
    }
}
