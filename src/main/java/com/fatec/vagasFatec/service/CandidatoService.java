package com.fatec.vagasFatec.service;

import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoCadastroDTO;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoResponseDTO;
import com.fatec.vagasFatec.model.Candidato;
import com.fatec.vagasFatec.model.Enum.StatusCandidato;
import com.fatec.vagasFatec.repository.CandidatoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CandidatoService {

    private final CandidatoRepository candidatoRepository;


    //Metodo Auxiliar para conversao de dados
    public CandidatoResponseDTO converterCandidatos (Candidato candidato){
        return  new CandidatoResponseDTO(
                 candidato.getId(),
                 candidato.getNomeCompleto(),
                 candidato.getRaAluno(),
                 candidato.getEmailCandidato(),
                 candidato.getDataNascimento(),
                 candidato.getTelefone(),
                 candidato.getCidade(),
                 candidato.getLinkLinkedin(),
                 candidato.getLinkGithub(),
                 candidato.getBioCandidato(),
                 candidato.getStatusCandidato()
        );
    }



    //Criar Candidato
    public  CandidatoResponseDTO criarCandidato(CandidatoCadastroDTO dto){

        if(candidatoRepository.existsByEmailCandidato(dto.email())){
            throw  new RuntimeException("E-mail já cadastrado");
        }
        if(candidatoRepository.existsByRaAluno(dto.ra())){
            throw new RuntimeException("RA já cadastrado");
        }

        Candidato candidato = new Candidato();
                candidato.setNomeCompleto(dto.nome());
                candidato.setRaAluno(dto.ra());
                candidato.setEmailCandidato(dto.email());
                candidato.setSenha(dto.senha());

        candidatoRepository.save(candidato);

        return converterCandidatos(candidato);
    }

}
