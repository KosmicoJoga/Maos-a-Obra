package org.example.maosaobra.service;

import org.example.maosaobra.dto.ExcluirContaDTO;
import org.example.maosaobra.model.Cliente;
import org.example.maosaobra.model.Pessoa;
import org.example.maosaobra.model.Trabalhador;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContaService {

    private final PessoaService pessoaService;
    private final DemandaService demandaService;
    private final PasswordEncoder passwordEncoder;

    public ContaService(PessoaService pessoaService, DemandaService demandaService, PasswordEncoder passwordEncoder) {
        this.pessoaService = pessoaService;
        this.demandaService = demandaService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void excluirConta(String email, ExcluirContaDTO dto) {

        Pessoa pessoa = pessoaService.buscarPorEmail(email);

        // confirma a identidade
        if (dto.senha() == null || !passwordEncoder.matches(dto.senha(), pessoa.getSenha()))
            throw new IllegalArgumentException("Senha incorreta!");

        // vê se tem demandas na conta em questão
        if (demandaService.temServicoEmAndamento(pessoa))
            throw new IllegalArgumentException("Você tem demandas em andamento. Conclua-as antes de excluir a conta!!");

        // deleta onde o bd não deleta por si só
        if (pessoa instanceof Cliente)
            demandaService.removerDadosDoCliente(pessoa.getId());
        else if (pessoa instanceof Trabalhador)
            demandaService.desvincularTrabalhador(pessoa.getId());

        // exclui a pessoa do bd, junto com tudo relacionado a ela (chats, avaliações, etc)
        pessoaService.excluir(pessoa);
    }

}
