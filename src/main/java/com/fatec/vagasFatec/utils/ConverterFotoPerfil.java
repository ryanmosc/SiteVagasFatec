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
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ConverterFotoPerfil {

    private final CandidatoRepository candidatoRepository;

    public void salvarFotoPerfil(MultipartFile foto){
        Long candidatoId = SecurityUtil.getCurrentUserId();

        if (foto == null || foto.isEmpty()){
            throw new DadosInvalidosException("Nenhum arquivo de foto enviada");
        }

        long maxSize = 5 * 1024 * 1024;
        if (foto.getSize() > maxSize) {
            throw new DadosInvalidosException("A imagem deve ter no máximo 5MB");
        }


        String contentType = foto.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/jpeg") &&
                        !contentType.equals("image/png") &&
                        !contentType.equals("image/jpg"))) {

            throw new DadosInvalidosException("Formato inválido. Apenas JPG e PNG são permitidos");
        }


        String nomeArquivo = foto.getOriginalFilename();
        if (nomeArquivo == null ||
                (!nomeArquivo.toLowerCase().endsWith(".jpg") &&
                        !nomeArquivo.toLowerCase().endsWith(".jpeg") &&
                        !nomeArquivo.toLowerCase().endsWith(".png"))) {

            throw new DadosInvalidosException("Extensão do arquivo inválida");
        }

        Candidato candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new DadosNaoEncontrados("Candidato não encontrado"));


    try{
        candidato.setFotoPerfil(foto.getBytes());
        candidato.setNomeFoto(foto.getOriginalFilename());
        candidato.setDataAlteracaoFoto(LocalDateTime.now());
        candidatoRepository.save(candidato);
    } catch (IOException e){
        throw new DadosInvalidosException("Erro ao ler o arquivo de currículo: " + e.getMessage());
    }
    }

    public ResponseEntity<byte[]> visualizarFotoPerfil(Candidato candidato) {
        byte[] dados = candidato.getFotoPerfil();

        if (dados == null || dados.length == 0) {
            return ResponseEntity.noContent().build();
        }

        String nomeFoto = candidato.getNomeFoto() != null ? candidato.getNomeFoto() : "foto.jpg";

        MediaType mediaType = nomeFoto.toLowerCase().endsWith(".png")
                ? MediaType.IMAGE_PNG
                : MediaType.IMAGE_JPEG;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nomeFoto + "\"")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(dados.length))
                .body(dados);
    }


    }









