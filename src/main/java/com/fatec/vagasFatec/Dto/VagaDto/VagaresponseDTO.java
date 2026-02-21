package com.fatec.vagasFatec.Dto.VagaDto;

import com.fatec.vagasFatec.model.Enum.TipoVagaEnum;

import java.time.LocalDateTime;

public record VagaresponseDTO(
        Long id,
        String titulo,
        String descricao,
        String curso,
        TipoVagaEnum tipoVagaEnum,
        LocalDateTime dataPublicacaoVaga,
        Boolean statusvaga

) {
}
