package org.example.maosaobra.controller;

import org.example.maosaobra.dto.ConversaDTO;
import org.example.maosaobra.model.*;
import org.example.maosaobra.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Controller
public class PaginaController {

    private final DemandaService demandaService;
    private final PessoaService pessoaService;
    private final ChatService chatService;
    private final TrabalhadorService trabalhadorService;
    private final AvaliacaoService avaliacaoService;

    public PaginaController(DemandaService demandaService, PessoaService pessoaService, ChatService chatService, TrabalhadorService trabalhadorService, AvaliacaoService avaliacaoService) {
        this.demandaService = demandaService;
        this.pessoaService = pessoaService;
        this.chatService = chatService;
        this.trabalhadorService = trabalhadorService;
        this.avaliacaoService = avaliacaoService;
    }

    @GetMapping("/inicioCliente")
    public String inicioCliente(Authentication auth, Model model) {
        Pessoa pessoa = pessoaLogada(auth);

        List<ConversaDTO> conversas = conversas(pessoa);

        model.addAttribute("nome",  pessoa.getNome());
        model.addAttribute("trabalhadores",  trabalhadorService.buscarTodosPorAvaliacao());
        model.addAttribute("ehTrabalhador", pessoa instanceof Trabalhador);
        model.addAttribute("conversas", conversas);

        return "inicioCliente"; // nome do arquivo na pasta templates, e sem o ".html"
    }

    @GetMapping("/inicioPrestador")
    public String inicioPrestador(Authentication auth, Model model) {
        Pessoa pessoa = pessoaLogada(auth);

        List<ConversaDTO> conversas = conversas(pessoa);

        model.addAttribute("nome",  pessoa.getNome());
        model.addAttribute("demandas", demandaService.buscarAbertas());
        model.addAttribute("ehTrabalhador", pessoa instanceof Trabalhador);
        model.addAttribute("conversas", conversas);

        return "inicioPrestador";
    }

    @GetMapping("/solicitacoesDemanda")
    public String solicitacoesDemanda(Authentication auth, Model model) {
        Pessoa pessoa = pessoaLogada(auth);

        List<ConversaDTO> conversas = conversas(pessoa);

        model.addAttribute("nome",  pessoa.getNome());
        model.addAttribute("demandas", demandaService.buscarSolicitacoesPendentes(pessoa.getId()));
        model.addAttribute("ehTrabalhador", pessoa instanceof Trabalhador);
        model.addAttribute("conversas", conversas);

        return "solicitacoesDemanda";
    }

    @GetMapping("/perfil/logado")
    public String perfilLogado(Authentication auth, Model model) {
        Pessoa pessoa = pessoaLogada(auth);

        List<ConversaDTO> conversas = conversas(pessoa);

        model.addAttribute("nome",  pessoa.getNome());
        model.addAttribute("ehTrabalhador", pessoa instanceof Trabalhador);
        model.addAttribute("pessoa",  pessoa);
        model.addAttribute("conversas", conversas);

        return "perfilLogado";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil(Authentication auth, Model model) {
        Pessoa pessoa = pessoaLogada(auth);

        model.addAttribute("pessoa", pessoa);
        model.addAttribute("ehTrabalhador", pessoa instanceof Trabalhador);

        return "formEditarPerfil";
    }

    @GetMapping("/perfil/senha")
    public String alterarSenha(Authentication auth, Model model) {

        model.addAttribute("nome", pessoaLogada(auth).getNome());

        return "formAlterarSenha";
    }

    // {id:\\d+} restringe o id apenas a números, para não dar conflito com outros caminhos de /perfil
    @GetMapping("/perfil/{id:\\d+}")
    public String perfilPublico(@PathVariable Long id, Authentication auth, Model model) {

        Pessoa logada = pessoaLogada(auth);

        List<ConversaDTO> conversas = conversas(logada);

        if (logada.getId().equals(id)) {
            return "redirect:/perfil/logado";
        }

        Pessoa perfil = pessoaService.buscarPorId(id);

        if (perfil == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado!");

        model.addAttribute("nome", logada.getNome());
        model.addAttribute("perfil", perfil);
        model.addAttribute("ehTrabalhador", perfil instanceof Trabalhador);
        model.addAttribute("ehTrabalhadorLogado", logada instanceof Trabalhador);
        model.addAttribute("podeConversar", (logada instanceof Cliente) != (perfil instanceof Cliente));
        model.addAttribute("podeSolicitar", logada instanceof Cliente && perfil instanceof Trabalhador);
        model.addAttribute("conversas", conversas);

        return "perfilPublico";
    }

    @GetMapping("/chat/{id:\\d+}")
    public String chat(@PathVariable Long id, Authentication auth, Model model) {
        Pessoa pessoa = pessoaLogada(auth);
        Conversa conversa = chatService.buscarSeParticipa(id, pessoa);  // 404 se não participa
        List<ConversaDTO> conversas = conversas(pessoa);

        String outra = conversa.getCliente().getId().equals(pessoa.getId())
                ? conversa.getTrabalhador().getNome()
                : conversa.getCliente().getNome();

        model.addAttribute("nome", pessoa.getNome());
        model.addAttribute("idConversa", conversa.getId());
        model.addAttribute("ehTrabalhador", pessoa instanceof Trabalhador);
        model.addAttribute("outraPessoa", outra);
        model.addAttribute("conversas", conversas);

        return "chat";
    }

    @GetMapping("/solicitarDemanda/{id:\\d+}")
    public String solicitarDemanda(@PathVariable Long id, Authentication auth, Model model) {

        Trabalhador trabalhador = trabalhadorService.buscarPorId(id);
        if (trabalhador == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trabalhador não encontrado!");

        model.addAttribute("nome", pessoaLogada(auth).getNome());
        model.addAttribute("trabalhador", trabalhador);

        return "formSolicitarDemanda";
    }

    @GetMapping("/perfil/excluir")
    public String excluirConta() {
        return "formExcluirConta";
    }

    @GetMapping("/demandasAceitas")
    public String demandasAceitas(Authentication auth, Model model) {
        Pessoa pessoa = pessoaLogada(auth);

        List<Demanda> demandas = demandaService.buscarServicosAceitos(pessoa.getId());

        List<Demanda> concluidas = demandaService.buscarDemandasConcluidas(pessoa.getId());

        Set<Long> avaliadas = avaliacaoService.idsJaAvaliadas(concluidas, Avaliador.TRABALHADOR);

        List<Demanda> aguardandoAvaliacao = concluidas.stream()
                .filter(d -> !avaliadas.contains(d.getId()))
                .toList();

        model.addAttribute("nome", pessoa.getNome());
        model.addAttribute("demandas", demandas);
        model.addAttribute("ehTrabalhador", pessoa instanceof Trabalhador);
        model.addAttribute("demandasConcluidas", aguardandoAvaliacao);
        model.addAttribute("conversas", conversas(pessoa)); // para sidebar de chats

        return "demandasAceitas";
    }

    @GetMapping("/minhasDemandas")
    public String minhasDemandas(Authentication auth, Model model) {
        Pessoa pessoa = pessoaLogada(auth);

        List<Demanda> demandas = demandaService.buscarTodasCliente(pessoa.getId());
        model.addAttribute("nome", pessoa.getNome());
        model.addAttribute("demandas", demandas);
        model.addAttribute("avaliadas", avaliacaoService.idsJaAvaliadas(demandas, Avaliador.CLIENTE));
        model.addAttribute("ehTrabalhador", pessoa instanceof Trabalhador);
        model.addAttribute("conversas", conversas(pessoa));

        return "minhasDemandas";
    }

    @GetMapping("/avaliar/{idDemanda:\\d+}")
    public String avaliar(@PathVariable Long idDemanda, Authentication auth, Model model) {
        Pessoa pessoa = pessoaLogada(auth);
        String voltar = pessoa instanceof Cliente ? "/minhasDemandas" : "/demandasAceitas";

        try {
            Demanda demanda = avaliacaoService.podeAvaliar(idDemanda, pessoa);

            // quem é avaliado é a outra parte
            String nomeAvaliado = pessoa instanceof Cliente
                    ? demanda.getTrabalhador().getNome()
                    : demanda.getCliente().getNome();

            model.addAttribute("idDemanda", idDemanda);
            model.addAttribute("nomeAvaliado", nomeAvaliado);
            model.addAttribute("voltar", voltar);

            return "formAvaliacao";
        } catch (IllegalArgumentException e) {
            return "redirect:" + voltar; // ainda não concluída, ou você já avaliou
        }
    }

    @GetMapping("/avaliar/listar")
    public String listarAvaliacoesPorAvaliador(Authentication auth, Model model) {
        Pessoa pessoa = pessoaLogada(auth);

        List<Avaliacao> avaliacoes;

        if (pessoa instanceof Cliente)
            avaliacoes = avaliacaoService.buscarTodosPeloAvaliador(pessoa.getId(), Avaliador.CLIENTE);
        else
            avaliacoes = avaliacaoService.buscarTodosPeloAvaliador(pessoa.getId(), Avaliador.TRABALHADOR);

        model.addAttribute("avaliacoes", avaliacoes);
        model.addAttribute("ehTrabalhador", pessoa instanceof Trabalhador);
        model.addAttribute("nome", pessoa.getNome());

        return "historicoAvaliacoes";
    }

    // função paga pegar a conta do usuário logada
    private Pessoa pessoaLogada(Authentication auth) {
        return pessoaService.buscarPorEmail(auth.getName());
    }

    // função para carregar as conversas em diversas páginas
    private List<ConversaDTO> conversas(Pessoa pessoa) {

        return chatService.minhasConversas(pessoa).stream()
                .map(c -> new ConversaDTO(c.getId(), c.getCliente().getId().equals(pessoa.getId())
                        ? c.getTrabalhador().getNome()
                        : c.getCliente().getNome()))
                .toList();
    }
}
