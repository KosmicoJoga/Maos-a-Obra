package org.example.maosaobra.controller;

import org.example.maosaobra.model.Trabalhador;
import org.example.maosaobra.service.AvaliacaoService;
import org.example.maosaobra.service.CertificadoService;
import org.example.maosaobra.service.DemandaService;
import org.example.maosaobra.service.ExperienciaService;
import org.example.maosaobra.service.ServicoService;
import org.example.maosaobra.service.TrabalhadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

// Páginas abertas a visitantes (sem login): tela inicial com busca/filtros e perfil público de trabalhadores
@Controller
public class PublicoController {

    private final TrabalhadorService trabalhadorService;
    private final ServicoService servicoService;
    private final AvaliacaoService avaliacaoService;
    private final CertificadoService certificadoService;
    private final ExperienciaService experienciaService;
    private final DemandaService demandaService;

    public PublicoController(TrabalhadorService trabalhadorService, ServicoService servicoService,
                             AvaliacaoService avaliacaoService, CertificadoService certificadoService,
                             ExperienciaService experienciaService, DemandaService demandaService) {
        this.trabalhadorService = trabalhadorService;
        this.servicoService = servicoService;
        this.avaliacaoService = avaliacaoService;
        this.certificadoService = certificadoService;
        this.experienciaService = experienciaService;
        this.demandaService = demandaService;
    }

    @GetMapping({"/", "/index.html"})
    public String inicio(Authentication auth, Model model,
                         @RequestParam(required = false) String pesquisa,
                         @RequestParam(required = false) List<String> servicos) {

        // quem já está logado vai direto para a sua tela inicial
        if (estaLogado(auth)) {
            boolean trabalhador = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_TRABALHADOR"));
            return trabalhador ? "redirect:/inicioPrestador" : "redirect:/inicioCliente";
        }

        if (servicos == null)
            servicos = new ArrayList<>();

        model.addAttribute("trabalhadores", trabalhadorService.buscarComFiltros(pesquisa, servicos));
        model.addAttribute("listaServicos", servicoService.buscarPorOrdemAlfabetico());
        model.addAttribute("servicosSelecionados", servicos);
        model.addAttribute("pesquisa", pesquisa);

        return "index";
    }

    @GetMapping("/trabalhador/{id:\\d+}")
    public String perfilVisitante(@PathVariable Long id, Authentication auth, Model model) {

        if (estaLogado(auth))
            return "redirect:/perfil/" + id;

        Trabalhador trabalhador = trabalhadorService.buscarPorId(id);

        if (trabalhador == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado!");

        model.addAttribute("perfil", trabalhador);
        model.addAttribute("certificados", certificadoService.buscarPorTrabalhador(id));
        model.addAttribute("experiencias", experienciaService.buscarPorTrabalhador(id));
        model.addAttribute("avaliacoesRecebidas", avaliacaoService.buscarRecebidasPeloTrabalhador(id));
        model.addAttribute("historicoPrestador", demandaService.buscarDemandasConcluidas(id));

        return "perfilVisitante";
    }

    // foto de perfil pública: só de trabalhadores (a foto de clientes continua exigindo login)
    @GetMapping("/publico/foto/{id:\\d+}")
    public ResponseEntity<byte[]> fotoTrabalhador(@PathVariable Long id) {

        Trabalhador trabalhador = trabalhadorService.buscarPorId(id);

        if (trabalhador == null || trabalhador.getFotoPerfil() == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Foto de perfil não encontrada.");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(trabalhador.getTipoFotoPerfil()))
                .body(trabalhador.getFotoPerfil());
    }

    private boolean estaLogado(Authentication auth) {
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }
}
