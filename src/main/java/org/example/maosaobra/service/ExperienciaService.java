package org.example.maosaobra.service;

import org.example.maosaobra.model.Experiencia;
import org.example.maosaobra.repository.ExperienciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExperienciaService {

    private final ExperienciaRepository experienciaRepository;

    public ExperienciaService(ExperienciaRepository experienciaRepository) {
        this.experienciaRepository = experienciaRepository;
    }

    public Experiencia salvar(Experiencia experiencia) {
        return experienciaRepository.save(experiencia);
    }

    @Transactional
    public List<Experiencia> buscarPorTrabalhador(Long idTrabalhador) {
        return experienciaRepository.findAllByTrabalhador_IdOrderByDataInicioDesc(idTrabalhador);
    }

    public Experiencia buscarPorId(Long id) {
        return experienciaRepository.findById(id).orElse(null);
    }

    public void excluir(Long idExperiencia) {
        experienciaRepository.deleteById(idExperiencia);
    }

}
