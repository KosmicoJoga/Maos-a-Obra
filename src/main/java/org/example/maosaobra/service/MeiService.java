package org.example.maosaobra.service;

import org.example.maosaobra.model.Mei;
import org.example.maosaobra.repository.MeiRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeiService {

    private final MeiRepository meiRepository;

    public MeiService(MeiRepository meiRepository) {
        this.meiRepository = meiRepository;
    }

    public Mei salvar(Mei mei) {
        return this.meiRepository.save(mei);
    }

    public void excluir(Mei mei) {
        this.meiRepository.delete(mei);
    }

    public List<Mei> buscarTodos() {
        return this.meiRepository.findAll();
    }

    public Mei buscarPorId(Long id) {
        return this.meiRepository.findById(id).orElse(null);
    }
}
