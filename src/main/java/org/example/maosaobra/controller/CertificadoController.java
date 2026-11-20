package org.example.maosaobra.controller;

import org.example.maosaobra.model.Certificado;
import org.example.maosaobra.model.Experiencia;
import org.example.maosaobra.model.Pessoa;
import org.example.maosaobra.model.Trabalhador;
import org.example.maosaobra.service.CertificadoService;
import org.example.maosaobra.service.ExperienciaService;
import org.example.maosaobra.service.PessoaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/perfil")
public class CertificadoController {

    private final CertificadoService certificadoService;
    private final ExperienciaService experienciaService;
    private final PessoaService pessoaService;

    public CertificadoController(CertificadoService service, ExperienciaService experienciaService, PessoaService pessoaService) {
        this.certificadoService = service;
        this.experienciaService = experienciaService;
        this.pessoaService = pessoaService;
    }

    // CERTIFICADOS
    @PostMapping("/certificados")
    public ResponseEntity<?> cadastrarCertificado(@RequestParam String curso, @RequestParam MultipartFile arquivo, Authentication auth) throws IOException {

        Trabalhador trabalhador = trabalhadorLogado(auth);

        // se o curso estiver vazio
        if (curso == null || curso.isBlank())
            throw new IllegalArgumentException("Informe o nome do certificado!");

        // se o curso descrito tiver mais que 150 caracteres
        if (curso.trim().length() > 150)
            throw new IllegalArgumentException("O nome do certificado deve ter no máximo 150 caracteres!");

        // se o arquivo não tiver selecionado ou vazio
        if (arquivo.isEmpty())
            throw new IllegalArgumentException("Selecione um arquivo!");

        // se o arquivo tiver mais de 15MB (limite do mediumblob no banco de dados)
        if (arquivo.getSize() > 15 * 1024 * 1024)
            throw new IllegalArgumentException("O arquivo deve ter no máximo 15 MB!");

        String tipo = arquivo.getContentType();

        if (tipo == null || !(tipo.equals("application/pdf") || tipo.equals("image/png") || tipo.equals("image/jpeg")))
            throw new IllegalArgumentException("Envie um arquivo PDF, PNG ou JPEG!");

        Certificado certificado = new Certificado();
        certificado.setTrabalhador(trabalhador);
        certificado.setCurso(curso.trim());
        certificado.setArquivo(arquivo.getBytes());
        certificado.setNomeArquivo(arquivo.getOriginalFilename());
        certificado.setTipoConteudo(tipo);

        certificadoService.salvar(certificado);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensagem", "Certificado cadastrado com sucesso!"));
    }

    @GetMapping("certificados/{id}/arquivo")
    public ResponseEntity<byte[]> visualizarCertificado(@PathVariable Long id) {

        Certificado certificado = certificadoService.buscarPorId(id);

        if (certificado == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificado não encontrado!");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(certificado.getTipoConteudo()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline()
                                .filename(certificado.getNomeArquivo())
                                .build().toString())
                .body(certificado.getArquivo());
    }

    @DeleteMapping("/certificados/{id}")
    public ResponseEntity<?> excluirCertificado(@PathVariable Long id, Authentication auth) {

        Trabalhador trabalhador = trabalhadorLogado(auth);
        Certificado certificado = certificadoService.buscarPorId(id);

        if (certificado == null || !certificado.getTrabalhador().getId().equals(trabalhador.getId()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificado não encontrado!");

        certificadoService.excluir(id);

        return ResponseEntity.ok().build();
    }

    // EXPERIENCIAS
    @PostMapping("/experiencias")
    public ResponseEntity<?> cadastrarExperiencia(@RequestParam String cargo,
            @RequestParam(required = false) String empresa,
            @RequestParam(required = false) String descricao,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Authentication auth) {

        Trabalhador trabalhador = trabalhadorLogado(auth);

        // se o cargo informado estiver em branco ou vazio
        if (cargo == null || cargo.isBlank())
            throw new IllegalArgumentException("Informe o cargo ou função!");

        // se o cargo informado tiver mais que 150 caracteres
        if (cargo.trim().length() > 150)
            throw new IllegalArgumentException("O cargo deve ter no máximo 150 caracteres!");

        // se o nome da empresa informada tiver mais que 150 caracteres
        if (empresa != null && empresa.length() > 150)
            throw new IllegalArgumentException("O nome da empresa deve ter no máximo 150 caracteres!");

        // se a data de início informada for null
        if (dataInicio == null)
            throw new IllegalArgumentException("Informe a data de início!");

        // se a data de fim for uma data anterior à data de início
        if (dataFim != null && dataFim.isBefore(dataInicio))
            throw new IllegalArgumentException("A data de término não pode ser anterior à data de início!");

        Experiencia experiencia = new Experiencia();
        experiencia.setTrabalhador(trabalhador);
        experiencia.setCargo(cargo.trim());
        experiencia.setEmpresa(empresa == null || empresa.isBlank() ? null : empresa.trim());
        experiencia.setDescricao(descricao == null || descricao.isBlank() ? null : descricao.trim());
        experiencia.setDataInicio(dataInicio);
        experiencia.setDataFim(dataFim);

        experienciaService.salvar(experiencia);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("mensagem", "Experiência cadastrada com sucesso!")
        );
    }

    @DeleteMapping("/experiencias/{id}")
    public ResponseEntity<?> excluirExperiencia(
            @PathVariable Long id, Authentication auth) {

        Trabalhador trabalhador = trabalhadorLogado(auth);
        Experiencia experiencia = experienciaService.buscarPorId(id);

        if (experiencia == null ||
                !experiencia.getTrabalhador().getId().equals(trabalhador.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Experiência não encontrada!");
        }

        experienciaService.excluir(id);

        return ResponseEntity.ok().build();
    }

    private Trabalhador trabalhadorLogado(Authentication auth) {
        Pessoa pessoa = pessoaService.buscarPorEmail(auth.getName());

        if (!(pessoa instanceof Trabalhador trabalhador))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Somente trabalhadores podem realizar esta operação!");

        return trabalhador;
    }

}



