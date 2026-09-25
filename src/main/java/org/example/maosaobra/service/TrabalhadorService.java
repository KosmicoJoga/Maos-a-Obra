package org.example.maosaobra.service;

import org.example.maosaobra.model.Trabalhador;
import org.example.maosaobra.repository.TrabalhadorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrabalhadorService {

    private final TrabalhadorRepository trabalhadorRepository;
    private final PasswordEncoder passwordEncoder;

    public TrabalhadorService(TrabalhadorRepository trabalhadorRepository,  PasswordEncoder passwordEncoder) {
        this.trabalhadorRepository = trabalhadorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Trabalhador salvar(Trabalhador trabalhador){
        trabalhador.setSenha(passwordEncoder.encode(trabalhador.getSenha()));
        return this.trabalhadorRepository.save(trabalhador);
    }

    public void excluir(Trabalhador trabalhador){
        this.trabalhadorRepository.delete(trabalhador);
    }

    public List<Trabalhador> buscarTodos(){
        return this.trabalhadorRepository.findAll();
    }

    public Trabalhador buscarPorId(Long id){
        return this.trabalhadorRepository.findById(id).orElse(null);
    }
}
