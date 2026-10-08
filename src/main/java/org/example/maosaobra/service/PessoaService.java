package org.example.maosaobra.service;

import org.example.maosaobra.dto.PerfilUpdateDTO;
import org.example.maosaobra.dto.SenhaUptadeDTO;
import org.example.maosaobra.model.Pessoa;
import org.example.maosaobra.model.Trabalhador;
import org.example.maosaobra.repository.PessoaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServicoService servicoService;

    public PessoaService(PessoaRepository pessoaRepository, PasswordEncoder passwordEncoder, ServicoService servicoService) {
        this.pessoaRepository = pessoaRepository;
        this.passwordEncoder = passwordEncoder;
        this.servicoService = servicoService;
    }

    public Pessoa salvar(Pessoa pessoa) {
        pessoa.setSenha(passwordEncoder.encode(pessoa.getSenha()));
        return this.pessoaRepository.save(pessoa);
    }

    public void excluir(Pessoa pessoa) {
        this.pessoaRepository.delete(pessoa);
    }

    public List<Pessoa> buscarTodos() {
        return this.pessoaRepository.findAll();
    }

    public Pessoa buscarPorId(Long id) {
        return this.pessoaRepository.findById(id).orElse(null);
    }

    public Pessoa buscarPorEmail(String email) {
        return this.pessoaRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public Pessoa atualizarPerfil(String email, PerfilUpdateDTO dto) {

        Pessoa pessoa = this.buscarPorEmail(email);

        pessoa.setNome(dto.nome());
        pessoa.setIdade(dto.idade());

        String sobre = dto.sobreMim();
        pessoa.setSobreMim(sobre == null || sobre.isBlank() ? null : sobre.trim());

        if (pessoa instanceof Trabalhador trabalhador && dto.idServico() != null) {
            trabalhador.setServico(servicoService.buscarPorId(dto.idServico()));
        }

        return pessoaRepository.save(pessoa);
    }

    @Transactional
    public void alterarSenha(String email, SenhaUptadeDTO dto) {

        Pessoa pessoa = this.buscarPorEmail(email);

        // verificar se a senha escrita pelo usuário é a que está salva no banco
        if (dto.senhaAtual() == null || !passwordEncoder.matches(dto.senhaAtual(), pessoa.getSenha()))
            throw new IllegalArgumentException("Senha atual incorreta!");

        // verificar se a senha nova tem no mínimo 8 caracteres
        if (dto.senhaNova() == null || dto.senhaNova().length() < 8)
            throw new IllegalArgumentException("A nova senha deve ter no mínimo 8 caracteres!");

        // verificar se a senha nova bate com a senha confirmada
        if (!dto.senhaNova().equals(dto.senhaConfirmada()))
            throw new IllegalArgumentException("A confirmação não confere com a nova senha!");

        // verificar se a senha nova é exatamente igual à senha salva no banco (não faria sentido a senha nova ser a mesma do banco)
        if (passwordEncoder.matches(dto.senhaNova(), pessoa.getSenha()))
            throw new IllegalArgumentException("A nova senha deve ser diferente da atual!");

        pessoa.setSenha(passwordEncoder.encode(dto.senhaNova()));
        pessoaRepository.save(pessoa);
    }
}
