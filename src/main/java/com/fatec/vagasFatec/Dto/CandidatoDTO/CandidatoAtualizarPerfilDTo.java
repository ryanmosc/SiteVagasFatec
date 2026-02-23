package com.fatec.vagasFatec.Dto.CandidatoDTO;

import java.time.LocalDate;

public record CandidatoAtualizarPerfilDTo(
        String telefone,
        String cidade,
        String linkedin,
        String  github,
        String bio,
        LocalDate dataNascimento
) {
}
