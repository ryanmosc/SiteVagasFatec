package com.fatec.vagasFatec.service;

import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoAtualizarPerfilDTo;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoCadastroDTO;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoResponseDTO;
import com.fatec.vagasFatec.model.Candidato;
import com.fatec.vagasFatec.model.Enum.StatusCandidato;
import com.fatec.vagasFatec.repository.CandidatoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    //Metodo Auxiliar para validar Status Candidato
    public boolean validarStatusCandidato(String raAluno){
        Candidato c = candidatoRepository.findByRaAluno(raAluno).orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        if (c.getStatusCandidato() == StatusCandidato.ATIVO){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
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

    //Atualizar dados faltantes
    public CandidatoResponseDTO atualizarDadosPerfil (CandidatoAtualizarPerfilDTo dadosPerfil, String raAluno){

        boolean validarCandidato = validarStatusCandidato(raAluno);
        if(validarCandidato == Boolean.FALSE){
            throw new RuntimeException("Candidato Inativo");
        }

        Candidato candidato = candidatoRepository.findByRaAluno(raAluno).orElseThrow(() -> new RuntimeException("Candidato nao encontrado"));

        if(dadosPerfil.telefone() != null) {
            candidato.setTelefone(dadosPerfil.telefone());
        }
        if(dadosPerfil.cidade() != null) {
            candidato.setCidade(dadosPerfil.cidade());
        }
        if(dadosPerfil.linkedin() != null) {
            candidato.setLinkLinkedin(dadosPerfil.linkedin());
        }
        if(dadosPerfil.github() != null) {
            candidato.setLinkGithub(dadosPerfil.github());
        }
        if(dadosPerfil.bio() != null) {
            candidato.setBioCandidato(dadosPerfil.bio());
        }
        if(dadosPerfil.dataNascimento() != null) {
            candidato.setDataNascimento(dadosPerfil.dataNascimento());
        }

        candidatoRepository.save(candidato);
        return converterCandidatos(candidato);
    }



    //Listar dados do aluno da sessão, por RA
    public CandidatoResponseDTO listarDadosAlunoPorRa (String raAluno){
        Candidato c = candidatoRepository.findByRaAluno(raAluno).orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        return converterCandidatos(c);
    }





    //Listar Todos os candidatos (Somente admins)
    public List<CandidatoResponseDTO> listarTodosCandidatos(){
        return candidatoRepository.findAll().stream().map((this::converterCandidatos)).toList();
    }

    //Desativar Candidato (Somente adm)
    public void desativarCandidato(String raAluno){
        Candidato candidato = candidatoRepository.findByRaAluno(raAluno).orElseThrow(() -> new RuntimeException("Candidato não encontrado"));
        candidato.setStatusCandidato(StatusCandidato.INATIVO);
        candidatoRepository.save(candidato);
    }

    //Reativar / ativar Candidato (Somente adm)
    public void reativarCandidato(String raAluno){
        Candidato candidato = candidatoRepository.findByRaAluno(raAluno).orElseThrow(() -> new RuntimeException("Candidato não encontrado"));
        candidato.setStatusCandidato(StatusCandidato.ATIVO);
        candidatoRepository.save(candidato);
    }
}
