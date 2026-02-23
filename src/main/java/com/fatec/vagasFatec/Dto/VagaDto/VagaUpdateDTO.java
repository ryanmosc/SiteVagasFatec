package com.fatec.vagasFatec.Dto.VagaDto;

import com.fatec.vagasFatec.model.Enum.CursosEnum;
import com.fatec.vagasFatec.model.Enum.ModalidadeVagaEnum;
import com.fatec.vagasFatec.model.Enum.StatusVaga;
import com.fatec.vagasFatec.model.Enum.TipoVagaEnum;

import java.time.LocalDateTime;

public record VagaUpdateDTO(

        String tituloVaga,
        String descricaoVaga,
        CursosEnum cursoVaga,
        TipoVagaEnum tipoVaga,
        ModalidadeVagaEnum modalidadeVaga,
        String cidadeVaga,
        Double bolsaAuxilio,
        LocalDateTime dataEncerramento
) {
}
