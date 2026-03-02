package com.fatec.vagasFatec.utils;

import com.fatec.vagasFatec.exceptions.DadosInvalidosException;
import com.fatec.vagasFatec.exceptions.DadosNaoEncontrados;
import com.fatec.vagasFatec.model.Candidato;
import com.fatec.vagasFatec.repository.CandidatoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ConverterCurriculo {

    @Value("${app.upload.curriculos-dir}")
    private String caminhoCurriculo;

    @PostConstruct
    public  void init(){
        new File(caminhoCurriculo).mkdirs();
    }

    private  final CandidatoRepository candidatoRepository;


    public void  salvarCurriculo(MultipartFile arquivo){
        Long candidatoLogado = SecurityUtil.getCurrentUserId();

        if (arquivo == null || arquivo.isEmpty()) {
            throw new DadosInvalidosException("Nenhum arquivo de currículo enviado");
        }

        // Validação: só PDF e tamanho máximo (ex: 5MB)
        if (!"application/pdf".equals(arquivo.getContentType())) {
            throw new DadosInvalidosException("Apenas arquivos PDF são permitidos");
        }
        if (arquivo.getSize() > 5 * 1024 * 1024) { // 5MB
            throw new DadosInvalidosException("Arquivo muito grande. Máximo 5MB");
        }

        String originalFilename = arquivo.getOriginalFilename();
        String extensao = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".pdf";

        // Nome único + timestamp para evitar conflitos
        String nomeUnico = candidatoLogado + "-" + System.currentTimeMillis() + extensao;
        String caminhoCompleto = caminhoCurriculo + File.separator + nomeUnico;

        try {
            arquivo.transferTo(new File(caminhoCompleto));

            // Atualiza o candidato no banco
            Candidato candidato = candidatoRepository.findById(candidatoLogado)
                    .orElseThrow(() -> new DadosNaoEncontrados("Candidato não encontrado"));

            candidato.setCaminhoCurriculo("/uploads/curriculos/" + nomeUnico);
            candidato.setNomeCurriculo(originalFilename);
            candidato.setDataAlteracaoCurriculo(LocalDateTime.now());

            candidatoRepository.save(candidato);
        } catch (IOException e) {
            throw new DadosInvalidosException("Erro ao salvar o currículo: " + e.getMessage());
        }
    }



    public ResponseEntity<Resource> visualizarCurriculo(String nomeArquivoNoBanco) {
        try {
            Path arquivoPath = Paths.get(caminhoCurriculo).resolve(nomeArquivoNoBanco).normalize();
            UrlResource resource = new UrlResource(arquivoPath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body((Resource) resource);
            } else {
                throw new DadosNaoEncontrados("Arquivo não encontrado");
            }
        } catch (Exception e) {
            throw new DadosInvalidosException("Erro ao carregar PDF: " + e.getMessage());
        }
    }
}
