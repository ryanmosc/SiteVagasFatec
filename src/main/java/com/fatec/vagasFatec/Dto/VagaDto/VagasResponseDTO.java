package com.fatec.vagasFatec.Dto.VagaDto;

import com.fatec.vagasFatec.model.Enum.StatusVaga;
import com.fatec.vagasFatec.model.Enum.TipoVagaEnum;
import java.time.LocalDateTime;


public record VagasResponseDTO (

        Long id,
     String tituloVaga,
     String descricaoVaga,
     String cursoVaga,
     TipoVagaEnum tipoVaga,
     String nomeEmpresa,
     String cidadeVaga,
     Double bolsaAuxilio,
     LocalDateTime dataPublicacaoVaga,
     LocalDateTime dataEncerramento,
     StatusVaga statusVaga
    ){
}
