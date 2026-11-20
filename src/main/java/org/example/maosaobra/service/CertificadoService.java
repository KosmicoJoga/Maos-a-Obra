package org.example.maosaobra.service;

import org.example.maosaobra.model.Certificado;
import org.example.maosaobra.repository.CertificadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CertificadoService {

    private final CertificadoRepository certificadoRepository;

    public CertificadoService(CertificadoRepository certificadoRepository) {
        this.certificadoRepository = certificadoRepository;
    }

    public Certificado salvar(Certificado certificado) {
        return certificadoRepository.save(certificado);
    }

    @Transactional
    public List<Certificado> buscarPorTrabalhador(Long idTrabalhador) {
        return certificadoRepository.findAllByTrabalhador_IdOrderByIdDesc(idTrabalhador);
    }

    public Certificado buscarPorId(Long id) {
        return certificadoRepository.findById(id).orElse(null);
    }

    public void excluir(Long idCertificado) {
        certificadoRepository.deleteById(idCertificado);
    }

}
