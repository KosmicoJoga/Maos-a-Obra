package org.example.maosaobra.service;

import org.example.maosaobra.dto.CadastroDTO;
import org.example.maosaobra.model.Cliente;
import org.example.maosaobra.model.Pessoa;
import org.example.maosaobra.model.Servico;
import org.example.maosaobra.model.Trabalhador;
import org.example.maosaobra.repository.PessoaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CadastroService {

    private final ClienteService clienteService;
    private final TrabalhadorService trabalhadorService;
    private final ServicoService servicoService;
    private final PessoaRepository pessoaRepository;

    public CadastroService(ClienteService c, TrabalhadorService t, ServicoService s, PessoaRepository p) {
        this.clienteService = c;
        this.trabalhadorService = t;
        this.servicoService = s;
        this.pessoaRepository = p;
    }

    public Cliente cadastrarCliente(CadastroDTO dto) {
        Cliente cliente = new Cliente();
        preencherDados(cliente, dto);

        try {
            return clienteService.salvar(cliente);
        } catch (DataIntegrityViolationException e) {
            throw emailJaCadastrado();
        }

    }

    public Trabalhador cadastrarTrabalhador(CadastroDTO dto) {

        if (dto.idServico() == null)
            throw new IllegalArgumentException("Escolha um tipo de serviço!");

        Servico servico = servicoService.buscarPorId(dto.idServico());

        if (servico == null)
            throw new IllegalArgumentException("Tipo de serviço inválido");

        Trabalhador trabalhador = new Trabalhador();
        preencherDados(trabalhador, dto);
        trabalhador.setServico(servico);

        try {
            return trabalhadorService.salvar(trabalhador);
        } catch (DataIntegrityViolationException e) {
            throw emailJaCadastrado();
        }

    }

    private void preencherDados(Pessoa p, CadastroDTO dto) {
        String email = dto.email().trim().toLowerCase();

        if (pessoaRepository.existsByEmail(email))
            throw emailJaCadastrado();

        p.setNome(dto.nome().trim());
        p.setEmail(email);
        p.setIdade(dto.idade());
        p.setSenha(dto.senha());
    }

    private ResponseStatusException emailJaCadastrado() {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Este email já está cadastrado!");
    }

}
