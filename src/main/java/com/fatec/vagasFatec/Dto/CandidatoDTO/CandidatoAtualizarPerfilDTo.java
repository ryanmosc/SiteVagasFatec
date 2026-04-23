package com.fatec.vagasFatec.Dto.CandidatoDTO;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CandidatoAtualizarPerfilDTo(
        String telefone,
        String cidade,
        String linkedin,
        String  github,
        String portifolio,
        String bio,
        LocalDate dataNascimento,
        MultipartFile curriculo
) {
}
