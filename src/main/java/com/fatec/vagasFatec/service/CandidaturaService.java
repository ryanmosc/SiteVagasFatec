package com.fatec.vagasFatec.service;

import com.fatec.vagasFatec.Dto.CandidaturaDTO.CandidaturaObservacaoDTO;
import com.fatec.vagasFatec.Dto.CandidaturaDTO.CandidaturaResponseDTO;
import com.fatec.vagasFatec.model.Candidato;
import com.fatec.vagasFatec.model.Candidatura;
import com.fatec.vagasFatec.model.Empresa;
import com.fatec.vagasFatec.model.Enum.StatusCandidato;
import com.fatec.vagasFatec.model.Enum.StatusCandidatura;
import com.fatec.vagasFatec.model.Enum.StatusEmpresa;
import com.fatec.vagasFatec.model.Enum.StatusVaga;
import com.fatec.vagasFatec.model.Vaga;
import com.fatec.vagasFatec.repository.CandidatoRepository;
import com.fatec.vagasFatec.repository.CandidaturaRepository;
import com.fatec.vagasFatec.repository.EmpresaRepository;
import com.fatec.vagasFatec.repository.Vagarepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidaturaService {
    private final CandidatoRepository candidatoRepository;
    private final Vagarepository vagarepository;
    private final CandidaturaRepository candidaturaRepository;
    private final EmpresaRepository empresaRepository;


    //Metodo auxiliar para conversao
    public CandidaturaResponseDTO converterCandidatura (Candidatura candidatura){
        return new CandidaturaResponseDTO(

                candidatura.getId(),
                candidatura.getCandidato().getId(),
                candidatura.getVaga().getId(),
                candidatura.getVaga().getTituloVaga(),
                candidatura.getVaga().getEmpresa().getRazaoSocial(),
                candidatura.getStatus(),
                candidatura.getObservacaoEmpresa(),
                candidatura.getDataInscricao()


        );
    }

    //CriarCandidatura
    public CandidaturaResponseDTO criarCandidatura (Long id_aluno, Long id_vaga){
        Candidato candidato = candidatoRepository.findById(id_aluno).orElseThrow(() -> new RuntimeException("Candidato não existente"));
        Vaga vaga = vagarepository.findById(id_vaga).orElseThrow(() -> new RuntimeException("Erro, vaga não encontrada"));

        if (candidato.getStatusCandidato() != StatusCandidato.ATIVO){
            throw  new RuntimeException("Candidato está inativo");
        }
        if (vaga.getStatusvaga() != StatusVaga.ABERTA){
            throw new RuntimeException("Vaga já encerrada");
        }

        if (candidaturaRepository.existsByCandidatoIdAndVagaId(id_aluno, id_vaga)){
            throw new RuntimeException("Voce ja se inscreveu nesta vaga");
        }

        Candidatura candidatura =new Candidatura();
        candidatura.setCandidato(candidato);
        candidatura.setVaga(vaga);

        candidaturaRepository.save(candidatura);

        return converterCandidatura(candidatura);
    }

    //Listar Todas as candidaturas do aluno
    public List<CandidaturaResponseDTO> listarTodasCandidaturasAluno (Long id_aluno){
        return candidaturaRepository.findByCandidatoId(id_aluno).stream().map(this::converterCandidatura).toList();
    }

    //Listar Todas as candidaturas de tod mundo (Somente adm para depuração)
    public List<CandidaturaResponseDTO> listarTodasCandidaturas(){
        return candidaturaRepository.findAll().stream().map(this::converterCandidatura).toList();
    }

    //Metodo para aluno desistir da candidatura
    public void desistirCandidatura (Long id_aluno, Long id_vaga){
        Candidatura candidatura = candidaturaRepository.findByCandidato_IdAndVaga_Id(id_aluno,id_vaga).orElseThrow(() -> new RuntimeException("Vaga não encontrada"));

        if (candidatura.getVaga().getStatusvaga() == StatusVaga.ENCERRADA){
            throw new RuntimeException("Vaga já encerrada");
        }
        if (candidatura.getStatus() == StatusCandidatura.REJEITADO || candidatura.getStatus() == StatusCandidatura.APROVADO) {
            throw new RuntimeException("Processo seletivo já finalizado");
        }
        if (candidatura.getStatus() == StatusCandidatura.DESISTIU){
            throw new RuntimeException("Voce ja desistiu desta vaga");
        }

        candidatura.setStatus(StatusCandidatura.DESISTIU);
        candidaturaRepository.save(candidatura);
    }

    //Empresa alterar Status candidatura (APROVADO / REJEITADO) (Somente empressas podem)
    public void alterarStatusCandidatura (Long idCandidatura, Long idEmpresa, StatusCandidatura novoStatus){
        Candidatura candidatura = candidaturaRepository.findById(idCandidatura).orElseThrow(() -> new RuntimeException("Candidatura não encontrada"));
        Candidato candidato = candidatoRepository.findById(candidatura.getCandidato().getId()).orElseThrow(() -> new RuntimeException("Candidato não encontrado"));
        Empresa empresa = empresaRepository.findById(idEmpresa).orElseThrow(() -> new RuntimeException("Empresa nao encontrada"));

        if (candidato.getStatusCandidato() == StatusCandidato.INATIVO){
            throw new RuntimeException("Candidato está inativo");
        }
        if (!candidatura.getVaga().getEmpresa().getId().equals(idEmpresa)){
            throw new RuntimeException("Empresa não é dona da vaga");
        }

        if (empresa.getStatusEmpresa() != StatusEmpresa.ATIVO){
            throw new RuntimeException("Empresa está inativa");
        }

        if (candidatura.getStatus() == StatusCandidatura.APROVADO ||
                candidatura.getStatus() == StatusCandidatura.REJEITADO) {
            throw new RuntimeException("Processo já finalizado");
        }

        if(novoStatus != StatusCandidatura.APROVADO && novoStatus != StatusCandidatura.REJEITADO){
            throw new RuntimeException("Status invalido");
                                }

        candidatura.setStatus(novoStatus);
        candidaturaRepository.save(candidatura);


    }


    //Empresa adicionar comentarias a candidaturas
    public void adicionarComentariosCandidatura(Long id_candidatura, Long id_empresa, CandidaturaObservacaoDTO observacaoDTO){
        Candidatura candidatura = candidaturaRepository.findById(id_candidatura).orElseThrow(() -> new RuntimeException("Candidatura não encontrada"));
        Empresa empresa = empresaRepository.findById(id_empresa).orElseThrow(() -> new RuntimeException("Empresa nao encontrada"));
        Candidato candidato = candidatoRepository.findById(candidatura.getCandidato().getId()).orElseThrow(() -> new RuntimeException("Candidato não encontrado"));


        if (!candidatura.getVaga().getEmpresa().getId().equals(id_empresa)){
            throw new RuntimeException("Empresa não é dona da vaga");
        }
        if (empresa.getStatusEmpresa() != StatusEmpresa.ATIVO){
            throw new RuntimeException("Empresa está inativa");
        }
        if (candidato.getStatusCandidato() == StatusCandidato.INATIVO){
            throw new RuntimeException("Candidato está inativo");
        }
        if (candidatura.getStatus() == StatusCandidatura.APROVADO ||
                candidatura.getStatus() == StatusCandidatura.REJEITADO) {
            throw new RuntimeException("Processo já finalizado");
        }
        if (observacaoDTO.observacaoCandidatura().length() > 150){
            throw new RuntimeException("mensagem muito grande máximo de 150 caracteres");
        }
        candidatura.setObservacaoEmpresa(observacaoDTO.observacaoCandidatura());
        candidaturaRepository.save(candidatura);



    }

    //Listar candidaturas por vaga
    public List<CandidaturaResponseDTO> listarCandidaturasPorVaga(Long id_vaga, Long id_empresa){
        Vaga vaga = vagarepository.findById(id_vaga).orElseThrow(() -> new RuntimeException("Erro, vaga não encontrada"));
        if (vaga.getEmpresa().getStatusEmpresa() != StatusEmpresa.ATIVO){
            throw new RuntimeException("Empresa está inativa");
        }
        if (!vaga.getEmpresa().getId().equals(id_empresa)){
            throw new RuntimeException("Empresa não é dona da vaga");
        }

        return candidaturaRepository.findByVaga_Id(id_vaga).stream().map(this::converterCandidatura).toList();

    }

}
