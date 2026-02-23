package com.fatec.vagasFatec.service;

import com.fatec.vagasFatec.Dto.VagaDto.VagaUpdateDTO;
import com.fatec.vagasFatec.Dto.VagaDto.VagasResponseDTO;
import com.fatec.vagasFatec.model.Enum.CursosEnum;
import com.fatec.vagasFatec.model.Enum.StatusVaga;
import com.fatec.vagasFatec.model.Vaga;
import com.fatec.vagasFatec.repository.Vagarepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VagasService {
    private final Vagarepository vagarepository;



    //Metodo Auxiliar para listarVagas

    private VagasResponseDTO converteVagas (Vaga vagaResponde){
        return  new VagasResponseDTO(
                vagaResponde.getId(),
                vagaResponde.getEmpresa().getId(),
                vagaResponde.getTituloVaga(),
                vagaResponde.getDescricaoVaga(),
                vagaResponde.getCursoVaga(),
                vagaResponde.getTipoVagaEnum(),
                vagaResponde.getModalidadeVaga(),
                vagaResponde.getEmpresa().getNome(),
                vagaResponde.getCidadeVaga(),
                vagaResponde.getBolsaAuxilio(),
                vagaResponde.getDataPublicacaoVaga(),
                vagaResponde.getDataEncerramento(),
                vagaResponde.getStatusvaga()

        );
    }



    //Criar Vaga
    public VagasResponseDTO criarVaga (Vaga vaga){
        Vaga vagaResponde = vagarepository.save(vaga);
        return converteVagas(vagaResponde);
    }

    //EditarVaga
    public VagasResponseDTO editarVaga (VagaUpdateDTO dto, Long id_vaga){
        Vaga vaga = vagarepository.findById(id_vaga).orElseThrow(() -> new RuntimeException("Erro, vaga não encontrada"));

        if (dto.tituloVaga() != null) {
            vaga.setTituloVaga(dto.tituloVaga());
        }
        if (dto.descricaoVaga() != null) {
            vaga.setDescricaoVaga(dto.descricaoVaga());
        }
        if (dto.cursoVaga() != null) {
            vaga.setCursoVaga(dto.cursoVaga());
        }
        if (dto.tipoVaga() != null) {
            vaga.setTipoVagaEnum(dto.tipoVaga());
        }

        if (dto.cidadeVaga() != null) {
            vaga.setCidadeVaga(dto.cidadeVaga());
        }
        if (dto.bolsaAuxilio() != null) {
            vaga.setBolsaAuxilio(dto.bolsaAuxilio());
        }
        if (dto.dataEncerramento() != null) {
            vaga.setDataEncerramento(dto.dataEncerramento());
        }

        vagarepository.save(vaga);
       return  converteVagas(vaga);
    }

    //Encerrar vaga

    public void encerrarVaga(Long idVaga){
        Vaga vaga = vagarepository.findById(idVaga).orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
        vaga.setStatusvaga(StatusVaga.ENCERRADA);
        vagarepository.save(vaga);
    }

    //Reabrir Vaga
    public void reabrirVaga(Long idVaga){
        Vaga vaga = vagarepository.findById(idVaga).orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
        vaga.setStatusvaga(StatusVaga.ABERTA);
        vagarepository.save(vaga);
    }

    //Deletar Vaga

    public void deletarVaga(Long id){
        vagarepository.deleteById(id);
    }

    //Listar Todas as vagas (menos as fechadas), e caso tiver alguma requisição de curso para filtro ele filtra
    public List<VagasResponseDTO> listarVagas(CursosEnum curso){

        List<Vaga> vagas;
        if (curso != null){
            vagas = vagarepository.findByStatusvagaAndCursoVaga(StatusVaga.ABERTA, curso);
        }
        else {
            vagas = vagarepository.findByStatusvaga(StatusVaga.ABERTA);
        }
        return vagas.stream().map(this::converteVagas
        ).toList();
    }





    //Listar Minhas Vagas (Lista todas as vagas da empresa em questão, ate as fechadas)

    public List<VagasResponseDTO> listarMinhasVagas(Long idEmpresa){
        return vagarepository.findByEmpresa_Id(idEmpresa).stream().map(this::converteVagas
        ).toList();
    }
}
