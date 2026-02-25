package com.fatec.vagasFatec.service;

import com.fatec.vagasFatec.Dto.CandidaturaDTO.CandidaturaResponseDTO;
import com.fatec.vagasFatec.model.Candidato;
import com.fatec.vagasFatec.model.Candidatura;
import com.fatec.vagasFatec.model.Enum.StatusCandidato;
import com.fatec.vagasFatec.model.Enum.StatusCandidatura;
import com.fatec.vagasFatec.model.Enum.StatusVaga;
import com.fatec.vagasFatec.model.Vaga;
import com.fatec.vagasFatec.repository.CandidatoRepository;
import com.fatec.vagasFatec.repository.CandidaturaRepository;
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


    //Metodo auxiliar para conversao
    public CandidaturaResponseDTO converterCandidatura (Candidatura candidatura){
        return new CandidaturaResponseDTO(

                candidatura.getId(),
                candidatura.getCandidato().getId(),
                candidatura.getVaga().getId(),
                candidatura.getVaga().getTituloVaga(),
                candidatura.getVaga().getEmpresa().getRazaoSocial(),
                candidatura.getStatus(),
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

}
