package org.example.maosaobra.service;

import org.example.maosaobra.model.Pessoa;
import org.example.maosaobra.repository.PessoaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder passwordEncoder;

    public PessoaService(PessoaRepository pessoaRepository, PasswordEncoder passwordEncoder) {
        this.pessoaRepository = pessoaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Pessoa salvar(Pessoa pessoa){
        return this.pessoaRepository.save(pessoa);
    }

    public void excluir(Pessoa pessoa){
        this.pessoaRepository.delete(pessoa);
    }

    public List<Pessoa> buscarTodos() {
        return this.pessoaRepository.findAll();
    }

    public Pessoa buscarPorId(Long id){
        return this.pessoaRepository.findById(id).orElse(null);
    }
}
