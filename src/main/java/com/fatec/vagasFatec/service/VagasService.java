package com.fatec.vagasFatec.service;

import com.fatec.vagasFatec.Dto.VagaDto.VagasResponseDTO;
import com.fatec.vagasFatec.model.Vaga;
import com.fatec.vagasFatec.repository.Vagarepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VagasService {
    private final Vagarepository vagarepository;

    public VagasResponseDTO criarVaga (Vaga vaga){
        Vaga vagaResponde = vagarepository.save(vaga);
        return  new VagasResponseDTO(
                vagaResponde.getId(),
                vagaResponde.getTituloVaga(),
                vagaResponde.getDescricaoVaga(),
                vagaResponde.getCursoVaga(),
                vagaResponde.getTipoVagaEnum(),
                vagaResponde.getNomeEmpresa(),
                vagaResponde.getCidadeVaga(),
                vagaResponde.getBolsaAuxilio(),
                vagaResponde.getDataPublicacaoVaga(),
                vagaResponde.getDataEncerramento(),
                vagaResponde.getStatusvaga()

        );
    }

}
