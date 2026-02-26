package com.fatec.vagasFatec.Dto.CandidaturaDTO;

import com.fatec.vagasFatec.model.Enum.StatusCandidatura;

import java.time.LocalDateTime;

public record CandidaturaResponseDTO(
        Long id,
        Long candidatoId,
        Long vagaId,
        String tituloVaga,
        String empresaNome,
        StatusCandidatura status,
        String observacaoEmpresa,
        LocalDateTime dataInscricao
) {}