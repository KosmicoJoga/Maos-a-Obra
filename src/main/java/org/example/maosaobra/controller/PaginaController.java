package org.example.maosaobra.controller;

import org.example.maosaobra.dto.ConversaDTO;
import org.example.maosaobra.model.*;
import org.example.maosaobra.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class PaginaController {

    private final DemandaService demandaService;
    private final PessoaService pessoaService;
    private final ChatService chatService;
    private final TrabalhadorService trabalhadorService;
    private final AvaliacaoService avaliacaoService;
    private final CertificadoService certificadoService;
    private final ExperienciaService experienciaService;
    private final CandidaturaService candidaturaService;

    public PaginaController(DemandaService demandaService, CandidaturaService candidaturaService, PessoaService pessoaService, ChatService chatService, TrabalhadorService trabalhadorService, AvaliacaoService avaliacaoService, CertificadoService certificadoService, ExperienciaService experienciaService) {
        this.demandaService = demandaService;
        this.pessoaService = pessoaService;
        this.chatService = chatService;
        this.trabalhadorService = trabalhadorService;
        this.avaliacaoService = avaliacaoService;
        this.certificadoService = certificadoService;
        this.experienciaService = experienciaService;
        this.candidaturaService = candidaturaService;
    }

    @GetMapping("/inicioCliente")
    public String inicioCliente(Authentication auth, Model model, @RequestParam(required = false) String pesquisa, @RequestParam(required = false) List<String> servicos) {
        Pessoa pessoa = pessoaLogada(auth);

        List<ConversaDTO> conversas = conversas(pessoa);

        if (servicos == null) {
            servicos = new ArrayList<>();
        }

        model.addAttribute("nome", pessoa.getNome());
        model.addAttribute("trabalhadores", trabalhadorService.buscarComFiltros(pesquisa, servicos));
        model.addAttribute("pesquisa", pesquisa);
        model.addAttribute("servicosSelecionados", servicos);
        model.addAttribute("ehTrabalhador", pessoa instanceof Trabalhador);
        model.addAttribute("conversas", conversas);

        return "inicioCliente"; // nome do arquivo na pasta templates, e sem o ".html"
    }

    @GetMapping("/inicioPrestador")
    public String inicioPrestador(Authentication auth, Model model, @RequestParam(required = false) String pesquisa, @RequestParam(required = false) String ordenacao) {
        Pessoa pessoa = pessoaLogada(auth);

        List<ConversaDTO> conversas = conversas(pessoa);

        Trabalhador t = (Trabalhador) pessoa;

        List<Demanda> demandas;

        if (t.getServico() != null)
            demandas = demandaService.buscarDemandasPublicasPrestador(t.getServico().getId(), pesquisa, ordenacao);
        else
            demandas = List.of();

        model.addAttribute("nome", pessoa.getNome());
        model.addAttribute("pessoaLogada", pessoa);
        model.addAttribute("ehTrabalhador", true);
        model.addAttribute("conversas", conversas);
        model.addAttribute("demandas", demandas);
        model.addAttribute("candidatadas", candidaturaService.idsJaCandidatados(pessoa.getId(), demandas));
        model.addAttribute("pesquisa", pesquisa);
        model.addAttribute("ordenacao", ordenacao == null ? "recentes" : ordenacao);

        return "inicioPrestador";
    }

    @GetMapping("/solicitacoesDemanda")
    public String solicitacoesDemanda(Authentication auth, Model model) {
        Pessoa pessoa = pessoaLogada(auth);

        List<ConversaDTO> conversas = conversas(pessoa);

        model.addAttribute("nome", pessoa.getNome());
        model.addAttribute("demandas", demandaService.buscarSolicitacoesPendentes(pessoa.getId()));
        model.addAttribute("ehTrabalhador", pessoa instanceof Trabalhador);
        model.addAttribute("conversas", conversas);

        return "solicitacoesDemanda";
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

    @GetMapping("/perfil/logado")
    public String perfilLogado(Authentication auth, Model model) {
        Pessoa pessoa = pessoaLogada(auth);

        List<ConversaDTO> conversas = conversas(pessoa);

        model.addAttribute("nome", pessoa.getNome());
        model.addAttribute("ehTrabalhador", pessoa instanceof Trabalhador);
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("conversas", conversas);

        if (pessoa instanceof Trabalhador trabalhador) {
            model.addAttribute("certificados", certificadoService.buscarPorTrabalhador(trabalhador.getId()));
            model.addAttribute("experiencias", experienciaService.buscarPorTrabalhador(trabalhador.getId()));
        }

        return "perfilLogado";
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

        if (perfil instanceof Trabalhador trabalhador) {
            model.addAttribute("certificados", certificadoService.buscarPorTrabalhador(trabalhador.getId()));
            model.addAttribute("experiencias", experienciaService.buscarPorTrabalhador(trabalhador.getId()));
            model.addAttribute("avaliacoesRecebidas", avaliacaoService.buscarRecebidasPeloTrabalhador(trabalhador.getId()));
            model.addAttribute("historicoPrestador", demandaService.buscarDemandasConcluidas(trabalhador.getId()));
        }

        return "perfilPublico";
    }

    @GetMapping("/chat/{id:\\d+}")
    public String chat(@PathVariable Long id, Authentication auth, Model model) {
        Pessoa pessoa = pessoaLogada(auth);
        Conversa conversa = chatService.buscarSeParticipa(id, pessoa);  // 404 se não participa
        List<ConversaDTO> conversas = conversas(pessoa);

        Pessoa outra = conversa.getCliente().getId().equals(pessoa.getId())
                ? conversa.getTrabalhador()
                : conversa.getCliente();

        model.addAttribute("nome", pessoa.getNome());
        model.addAttribute("idConversa", conversa.getId());
        model.addAttribute("ehTrabalhador", pessoa instanceof Trabalhador);
        model.addAttribute("outraPessoa", outra.getNome());
        model.addAttribute("idOutraPessoa", outra.getId());
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

        List<Demanda> todasDemandas = demandaService.buscarTodasCliente(pessoa.getId());

        // Identifica as demandas avaliadas pelo cliente
        Set<Long> avaliadas = avaliacaoService.idsJaAvaliadas(todasDemandas, Avaliador.CLIENTE);

        // Remove da lista as demandas concluídas que já foram avaliadas pelo cliente
        List<Demanda> demandas = todasDemandas.stream().filter(d -> d.getStatus() != StatusDemanda.CONCLUIDA
                || !avaliadas.contains(d.getId())).toList();

        model.addAttribute("nome", pessoa.getNome());
        model.addAttribute("demandas", demandas);
        model.addAttribute("avaliadas", avaliacaoService.idsJaAvaliadas(demandas, Avaliador.CLIENTE));
        model.addAttribute("ehTrabalhador", pessoa instanceof Trabalhador);
        model.addAttribute("conversas", conversas(pessoa));
        model.addAttribute("candidatos", candidaturaService.candidatosPorDemanda(demandas));

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
        model.addAttribute("conversas", conversas(pessoa));

        return "historicoAvaliacoes";
    }

    @GetMapping("/historicoDemandas")
    public String historicoDemandas(Authentication auth, Model model) {
        Pessoa pessoa = pessoaLogada(auth);

        boolean ehTrabalhador = pessoa instanceof Trabalhador;

        Avaliador papel = ehTrabalhador ? Avaliador.TRABALHADOR : Avaliador.CLIENTE;

        model.addAttribute("historico", avaliacaoService.buscarTodosPeloAvaliador(pessoa.getId(), papel));
        model.addAttribute("ehTrabalhador", ehTrabalhador);
        model.addAttribute("nome", pessoa.getNome());
        model.addAttribute("conversas", conversas(pessoa));

        return "historicoDemandas";
    }

    @GetMapping("/perfil/experiencia/cadastrar")
    public String cadastrarExperiencia(Authentication auth, Model model) {

        Pessoa pessoa = pessoaLogada(auth);

        if (!(pessoa instanceof Trabalhador))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Somente trabalhadores podem cadastrar experiências!");

        model.addAttribute("nome", pessoa.getNome());

        return "formExperiencia";
    }

    @GetMapping("/perfil/certificado/cadastrar")
    public String cadastrarCertificado(Authentication auth, Model model) {

        Pessoa pessoa = pessoaLogada(auth);

        if (!(pessoa instanceof Trabalhador))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Somente trabalhadores podem cadastrar certificados!");

        model.addAttribute("nome", pessoa.getNome());

        return "formCertificado";
    }

    @ModelAttribute("pessoaLogada")
    public Pessoa adicionarPessoaLogada(Authentication auth) {
        if (auth == null)
            return null;

        return pessoaService.buscarPorEmail(auth.getName());
    }

    // FUNÇÕES PRIVADAS USADAS SOMENTE NESTE CONTROLLER

    // função paga pegar a conta do usuário logada
    private Pessoa pessoaLogada(Authentication auth) {
        return pessoaService.buscarPorEmail(auth.getName());
    }

    // função para carregar as conversas em diversas páginas
    private List<ConversaDTO> conversas(Pessoa pessoa) {

        return chatService.minhasConversas(pessoa).stream()
                .map(c -> {
                    Pessoa outra = c.getCliente().getId().equals(pessoa.getId())
                            ? c.getTrabalhador()
                            : c.getCliente();

                    return new ConversaDTO(
                            c.getId(),
                            outra.getId(),
                            outra.getNome()
                    );
                }).toList();
    }
}
