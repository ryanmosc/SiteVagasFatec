package com.fatec.vagasFatec.service;

import com.fatec.vagasFatec.Dto.VagaDto.VagaUpdateDTO;
import com.fatec.vagasFatec.Dto.VagaDto.VagasResponseDTO;
import com.fatec.vagasFatec.exceptions.DadosNaoEncontrados;
import com.fatec.vagasFatec.exceptions.RegraDeNegocioVioladaException;
import com.fatec.vagasFatec.model.Empresa;
import com.fatec.vagasFatec.model.Enum.CursosEnum;
import com.fatec.vagasFatec.model.Enum.StatusVaga;
import com.fatec.vagasFatec.model.Vaga;
import com.fatec.vagasFatec.repository.EmpresaRepository;
import com.fatec.vagasFatec.repository.Vagarepository;
import com.fatec.vagasFatec.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VagasService {
    private final Vagarepository vagarepository;
    private final EmpresaService empresaService;
    private final EmpresaRepository empresaRepository;



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
                vagaResponde.getEmpresa().getRazaoSocial(),
                vagaResponde.getCidadeVaga(),
                vagaResponde.getBolsaAuxilio(),
                vagaResponde.getDataPublicacaoVaga(),
                vagaResponde.getDataEncerramento(),
                vagaResponde.getStatusvaga()

        );
    }

    public VagasResponseDTO criarVaga(Vaga vaga) {
        Long idEmpresaLogada = SecurityUtil.getCurrentUserId();

        // 1. Busca a empresa pelo ID logado ANTES de qualquer acesso a vaga.empresa
        Empresa empresaLogada = empresaRepository.findById(idEmpresaLogada)
                .orElseThrow(() -> new DadosNaoEncontrados("Empresa não encontrada para o usuário logado"));

        // 2. Valida status da empresa logada (não da que veio no body, que é null)
        if (!empresaService.validarStatusEmpresa(idEmpresaLogada)) {
            throw new RegraDeNegocioVioladaException("Empresa está inativa");
        }

        // 3. Ignora qualquer empresa que venha no body e seta a correta
        vaga.setEmpresa(empresaLogada);

        // 4. Salva
        Vaga salva = vagarepository.save(vaga);

        return converteVagas(salva);
    }


    //EditarVaga
    public VagasResponseDTO editarVaga (VagaUpdateDTO dto, Long id_vaga){
        Vaga vaga = vagarepository.findById(id_vaga).orElseThrow(() -> new DadosNaoEncontrados("Erro, vaga não encontrada"));

        Long idEmpresaLogada = SecurityUtil.getCurrentUserId();
        if (!vaga.getEmpresa().getId().equals(idEmpresaLogada)) {
            throw new RegraDeNegocioVioladaException("Você não é dono dessa vaga");
        }
        boolean validarEmpresa = empresaService.validarStatusEmpresa(idEmpresaLogada);
        if(validarEmpresa == Boolean.FALSE) {
            throw  new RegraDeNegocioVioladaException("Status empresa é inativo");
        }

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

    // Encerrar
    public void encerrarVaga(Long idVaga) {
        Vaga vaga = vagarepository.findById(idVaga)
                .orElseThrow(() -> new DadosNaoEncontrados("Vaga não encontrada"));

        Long idEmpresaLogada = SecurityUtil.getCurrentUserId();
        if (!vaga.getEmpresa().getId().equals(idEmpresaLogada)) {
            throw new RegraDeNegocioVioladaException("Você não é dono dessa vaga");
        }
        if (!empresaService.validarStatusEmpresa(idEmpresaLogada)) {
            throw new RegraDeNegocioVioladaException("Empresa inativa");
        }

        vaga.setStatusvaga(StatusVaga.ENCERRADA);
        vagarepository.save(vaga);
    }

    //Reabrir Vaga
    public void reabrirVaga(Long idVaga){
        Vaga vaga = vagarepository.findById(idVaga).orElseThrow(() -> new DadosNaoEncontrados("Vaga não encontrada"));
        boolean validarEmpresa = empresaService.validarStatusEmpresa(vaga.getEmpresa().getId());
        if(validarEmpresa == Boolean.FALSE) {
            throw  new RegraDeNegocioVioladaException("Status empresa é inativo");
        }
        if (vaga.getEmpresa().getId() != SecurityUtil.getCurrentUserId()){
            throw new RegraDeNegocioVioladaException("Empresa não é dona da vaga");
        }
        vaga.setStatusvaga(StatusVaga.ABERTA);
        vagarepository.save(vaga);
    }

    //Deletar Vaga

    public void deletarVaga(Long id){
        Vaga vaga = vagarepository.findById(id).orElseThrow(() -> new DadosNaoEncontrados("Vaga não encontrada"));
        if (vaga.getEmpresa().getId() != SecurityUtil.getCurrentUserId()){
            throw new RegraDeNegocioVioladaException("Empresa não é dona da vaga");
        }
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
