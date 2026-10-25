package org.example.maosaobra.service;

import org.example.maosaobra.model.*;
import org.example.maosaobra.repository.ConversaRepository;
import org.example.maosaobra.repository.MensagemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ConversaRepository conversaRepository;
    private final MensagemRepository mensagemRepository;
    private final ClienteService clienteService;
    private final TrabalhadorService trabalhadorService;

    public ChatService(ConversaRepository conversaRepository, MensagemRepository mensagemRepository, ClienteService clienteService, TrabalhadorService trabalhadorService) {
        this.conversaRepository = conversaRepository;
        this.mensagemRepository = mensagemRepository;
        this.clienteService = clienteService;
        this.trabalhadorService = trabalhadorService;
    }

    // o trabalhador inicia (ou retoma) a conversa com um cliente
    @Transactional
    public Conversa iniciarConversa(Pessoa logada, Pessoa outra) {

        if (outra == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada!");

        Cliente cliente;
        Trabalhador trabalhador;

        if (logada instanceof Cliente c && outra instanceof Trabalhador t) {
            cliente = c;
            trabalhador = t;
        } else if (logada instanceof Trabalhador t && outra instanceof Cliente c) {
            cliente = c;
            trabalhador = t;
        } else
            throw new IllegalArgumentException("A conversa precisa ser entre um cliente e um prestador de serviços!");

        return conversaRepository.findByClienteIdAndTrabalhadorId(cliente.getId(), trabalhador.getId())
                .orElseGet(() -> {
                    Conversa nova = new Conversa();
                    nova.setCliente(cliente);
                    nova.setTrabalhador(trabalhador);
                    nova.setDataCriacao(LocalDateTime.now());
                    return conversaRepository.save(nova);
                });
    }

    // devolve a conversa só se a pessoa participa dela; senão, 404
    public Conversa buscarSeParticipa(Long idConversa, Pessoa pessoa) {

        Conversa conversa = conversaRepository.findById(idConversa).orElse(null);

        boolean participa = conversa != null
                && (conversa.getCliente().getId().equals(pessoa.getId())
                || conversa.getTrabalhador().getId().equals(pessoa.getId()));

        if (!participa)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversa não encontrada!");

        return conversa;
    }

    @Transactional
    public Mensagem enviar(Long idConversa, Pessoa remetente, String conteudo) {

        Conversa conversa = buscarSeParticipa(idConversa, remetente);

        if (conteudo == null || conteudo.isBlank())
            throw new IllegalArgumentException("Digite uma mensagem!");
        if (conteudo.length() > 1000)
            throw new IllegalArgumentException("A mensagem pode ter no máximo 1000 caracteres.");

        Mensagem mensagem = new Mensagem();
        mensagem.setConversa(conversa);
        mensagem.setRemetente(remetente);
        mensagem.setConteudo(conteudo.trim());
        mensagem.setDataEnvio(LocalDateTime.now()); // o DEFAULT do banco não vale: o Hibernate manda null
        return mensagemRepository.save(mensagem);
    }

    public List<Mensagem> listarNovas(Long idConversa, Pessoa pessoa, Long depoisDe) {
        buscarSeParticipa(idConversa, pessoa);
        return mensagemRepository.findByConversaIdAndIdGreaterThanOrderByIdAsc(idConversa, depoisDe);
    }

    public List<Conversa> minhasConversas(Pessoa pessoa) {
        return conversaRepository.findByClienteIdOrTrabalhadorIdOrderByDataCriacaoDesc(pessoa.getId(), pessoa.getId());
    }

}
