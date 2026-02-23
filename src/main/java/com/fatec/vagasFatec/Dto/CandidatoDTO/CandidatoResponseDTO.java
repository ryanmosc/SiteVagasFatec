package com.fatec.vagasFatec.Dto.CandidatoDTO;

import com.fatec.vagasFatec.model.Enum.StatusCandidato;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CandidatoResponseDTO(

        Long id,
        String nomeCompleto,
        String raAluno,
        String emailCandidato,
        LocalDate dataNascimento,
        String telefone,
        String cidade,
        String linkLinkedin,
        String linkGithub,
        String bioCandidato,
        StatusCandidato statusCandidato
) {
}
