package com.fatec.vagasFatec.Dto.VagaDto;

import com.fatec.vagasFatec.model.Enum.StatusVaga;
import com.fatec.vagasFatec.model.Enum.TipoVagaEnum;

import java.time.LocalDateTime;

public record VagaUpdateDTO(

        String tituloVaga,
        String descricaoVaga,
        String cursoVaga,
        TipoVagaEnum tipoVaga,
        String cidadeVaga,
        Double bolsaAuxilio,
        LocalDateTime dataEncerramento
) {
}
