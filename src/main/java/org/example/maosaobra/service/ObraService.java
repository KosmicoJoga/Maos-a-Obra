package org.example.maosaobra.service;

import org.example.maosaobra.model.Obra;
import org.example.maosaobra.repository.ObraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObraService {

    private final ObraRepository obraRepository;

    public ObraService(ObraRepository obraRepository) {
        this.obraRepository = obraRepository;
    }

    public Obra salvar(Obra obra) {
        return obraRepository.save(obra);
    }

    public void excluir(Obra obra) {
        obraRepository.delete(obra);
    }

    public List<Obra> buscarTodos(){
        return this.obraRepository.findAll();
    }

    public Obra buscarPorId(Long id){
        return this.obraRepository.findById(id).orElse(null);
    }
}
