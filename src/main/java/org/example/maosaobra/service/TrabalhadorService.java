package org.example.maosaobra.service;

import jakarta.persistence.criteria.Predicate;
import org.example.maosaobra.model.Trabalhador;
import org.example.maosaobra.repository.TrabalhadorRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrabalhadorService {

    private final TrabalhadorRepository trabalhadorRepository;
    private final PasswordEncoder passwordEncoder;

    public TrabalhadorService(TrabalhadorRepository trabalhadorRepository, PasswordEncoder passwordEncoder) {
        this.trabalhadorRepository = trabalhadorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Trabalhador salvar(Trabalhador trabalhador) {
        trabalhador.setSenha(passwordEncoder.encode(trabalhador.getSenha()));
        return this.trabalhadorRepository.save(trabalhador);
    }

    public void excluir(Trabalhador trabalhador) {
        this.trabalhadorRepository.delete(trabalhador);
    }

    public List<Trabalhador> buscarTodos() {
        return this.trabalhadorRepository.findAll();
    }

    public Trabalhador buscarPorId(Long id) {
        return this.trabalhadorRepository.findById(id).orElse(null);
    }

    public List<Trabalhador> buscarTodosPorAvaliacao() {
        return trabalhadorRepository.findAllByOrderByAvaliacaoDesc();
    }

    public List<Trabalhador> buscarComFiltros(String pesquisa, List<String> servicos) {

        Specification<Trabalhador> spec = (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Pesquisa pelo nome do trabalhador
            if (pesquisa != null && !pesquisa.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + pesquisa.trim().toLowerCase() + "%"));
            }

            // Filtra pelos tipos de serviço selecionados
            if (servicos != null && !servicos.isEmpty()) {
                predicates.add(
                        root.join("servico").get("tipo").in(servicos)
                );
            }

            // Mantém a ordenação por avaliação
            query.orderBy(cb.desc(root.get("avaliacao")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return trabalhadorRepository.findAll(spec);
    }
}
