package com.fatec.vagasFatec.utils;

import com.fatec.vagasFatec.exceptions.DadosInvalidosException;
import com.fatec.vagasFatec.exceptions.DadosNaoEncontrados;
import com.fatec.vagasFatec.model.Candidato;
import com.fatec.vagasFatec.repository.CandidatoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ConverterCurriculo {

    private final CandidatoRepository candidatoRepository;

    /**
     * Salva (ou substitui) o currículo do candidato autenticado diretamente no banco de dados.
     */
    public void salvarCurriculo(MultipartFile arquivo) {
        Long candidatoId = SecurityUtil.getCurrentUserId();

        if (arquivo == null || arquivo.isEmpty()) {
            throw new DadosInvalidosException("Nenhum arquivo de currículo enviado");
        }

        // Valida: somente PDF
        if (!"application/pdf".equals(arquivo.getContentType())) {
            throw new DadosInvalidosException("Apenas arquivos PDF são permitidos");
        }

        // Valida: tamanho máximo 5 MB
        if (arquivo.getSize() > 5L * 1024 * 1024) {
            throw new DadosInvalidosException("Arquivo muito grande. Máximo 5MB");
        }

        Candidato candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new DadosNaoEncontrados("Candidato não encontrado"));

        try {
            candidato.setCurriculo(arquivo.getBytes());
            candidato.setNomeCurriculo(arquivo.getOriginalFilename());
            candidato.setDataAlteracaoCurriculo(LocalDateTime.now());
            candidatoRepository.save(candidato);
        } catch (IOException e) {
            throw new DadosInvalidosException("Erro ao ler o arquivo de currículo: " + e.getMessage());
        }
    }

    /**
     * Retorna o currículo de um candidato como PDF para download/visualização inline.
     * Recebe diretamente o objeto Candidato para evitar uma segunda consulta ao banco.
     */
    public ResponseEntity<byte[]> visualizarCurriculo(Candidato candidato) {
        byte[] dados = candidato.getCurriculo();

        if (dados == null || dados.length == 0) {
            return ResponseEntity.noContent().build();
        }

        String nomeArquivo = candidato.getNomeCurriculo() != null
                ? candidato.getNomeCurriculo()
                : "curriculo.pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nomeArquivo + "\"")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(dados.length))
                .body(dados);
    }
}