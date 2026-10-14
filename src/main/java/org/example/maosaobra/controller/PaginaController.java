package org.example.maosaobra.controller;

import org.example.maosaobra.dto.ConversaDTO;
import org.example.maosaobra.model.Conversa;
import org.example.maosaobra.model.Pessoa;
import org.example.maosaobra.model.Trabalhador;
import org.example.maosaobra.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PaginaController {

    private final DemandaService demandaService;
    private final PessoaService pessoaService;
    private final ChatService chatService;
    private final TrabalhadorService trabalhadorService;

    public PaginaController(DemandaService demandaService, PessoaService pessoaService, ChatService chatService, TrabalhadorService trabalhadorService) {
        this.demandaService = demandaService;
        this.pessoaService = pessoaService;
        this.chatService = chatService;
        this.trabalhadorService = trabalhadorService;
    }

    @GetMapping("/inicioCliente")
    public String inicioCliente(Authentication authentication, Model model) {
        Pessoa pessoa = pessoaLogada(authentication);

        model.addAttribute("nome",  pessoa.getNome());
        model.addAttribute("trabalhadores",  trabalhadorService.buscarTodosPorAvaliacao());

        return "inicioCliente"; // nome do arquivo na pasta templates, e sem o ".html"
    }

    @GetMapping("/inicioPrestador")
    public String inicioPrestador(Authentication authentication, Model model) {
        Pessoa pessoa = pessoaLogada(authentication);

        model.addAttribute("nome",  pessoa.getNome());
        model.addAttribute("demandas", demandaService.buscarAbertas());

        return "inicioPrestador";
    }

    @GetMapping("/solicitacoesDemanda")
    public String solicitacoesDemanda(Authentication authentication, Model model) {
        Pessoa pessoa = pessoaLogada(authentication);

        model.addAttribute("nome",  pessoa.getNome());
        model.addAttribute("demandas", demandaService.buscarAbertas());

        return "solicitacoesDemanda";
    }

    @GetMapping("/perfil/logado")
    public String perfilLogado(Authentication authentication, Model model) {
        Pessoa pessoa = pessoaLogada(authentication);

        model.addAttribute("nome",  pessoa.getNome());
        model.addAttribute("pessoa",  pessoa);

        return "perfilLogado";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil(Authentication authentication, Model model) {
        Pessoa pessoa = pessoaLogada(authentication);

        model.addAttribute("pessoa", pessoa);
        model.addAttribute("ehTrabalhador", pessoa instanceof Trabalhador);

        return "editarPerfil";
    }

    @GetMapping("/perfil/senha")
    public String alterarSenha(Authentication auth, Model model) {
        model.addAttribute("nome", pessoaLogada(auth).getNome());
        return "alterarSenha";
    }

    @GetMapping("/conversas")
    public String conversas(Authentication authentication, Model model) {
        Pessoa pessoa = pessoaLogada(authentication);

        List<ConversaDTO> conversas = chatService.minhasConversas(pessoa).stream()
                .map(c -> new ConversaDTO(c.getId(), c.getCliente().getId().equals(pessoa.getId())
                                ? c.getTrabalhador().getNome()
                                : c.getCliente().getNome()))
                .toList();

        model.addAttribute("nome", pessoa.getNome());
        model.addAttribute("conversas", conversas);
        return "conversas";
    }

    @GetMapping("/chat/{id}")
    public String chat(@PathVariable Long id, Authentication authentication, Model model) {
        Pessoa pessoa = pessoaLogada(authentication);
        Conversa conversa = chatService.buscarSeParticipa(id, pessoa);   // 404 se não participa

        String outra = conversa.getCliente().getId().equals(pessoa.getId())
                ? conversa.getTrabalhador().getNome()
                : conversa.getCliente().getNome();

        model.addAttribute("nome", pessoa.getNome());
        model.addAttribute("idConversa", conversa.getId());
        model.addAttribute("outraPessoa", outra);
        return "chat";
    }

    // função somente utilizada por essa própria classe
    private Pessoa pessoaLogada(Authentication authentication) {
        return pessoaService.buscarPorEmail(authentication.getName());
    }
}
