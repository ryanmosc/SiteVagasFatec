package com.fatec.vagasFatec.service;

import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoAtualizarPerfilDTo;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoCadastroDTO;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoMostrarDTO;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoResponseDTO;
import com.fatec.vagasFatec.auth.service.RecaptchaService;
import com.fatec.vagasFatec.exceptions.DadosNaoEncontrados;
import com.fatec.vagasFatec.exceptions.EntidadeJaExistenteException;
import com.fatec.vagasFatec.exceptions.RegraDeNegocioVioladaException;
import com.fatec.vagasFatec.model.Candidato;
import com.fatec.vagasFatec.model.Enum.Role;
import com.fatec.vagasFatec.model.Enum.StatusCandidato;
import com.fatec.vagasFatec.repository.CandidatoRepository;
import com.fatec.vagasFatec.utils.EmailSender;
import com.fatec.vagasFatec.utils.VerificationCodeGenerator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidatoService {

    private final CandidatoRepository candidatoRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender enviarEmail;
    private final VerificationCodeGenerator gerarCodigo;
    private final RecaptchaService recaptchaService;


    //Metodo Auxiliar para conversao de dados
    private CandidatoResponseDTO converterCandidatos (Candidato candidato){
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
                 candidato.getStatusCandidato(),
                candidato.getCaminhoCurriculo(),
                candidato.getNomeCurriculo()
        );
    }

    //Metodo Auxiliar para validar Status Candidato
    public boolean validarStatusCandidato(Long candidatoLogado){
        Candidato c = candidatoRepository.findById(candidatoLogado).orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        if (c.getStatusCandidato() == StatusCandidato.ATIVO){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }



    //Criar Candidato
    public  CandidatoResponseDTO criarCandidato(CandidatoCadastroDTO dto){

        if (dto.captcha() == null || dto.captcha().isBlank()) {
            throw new RegraDeNegocioVioladaException("Token do reCAPTCHA não fornecido.");
        }

        boolean captchaValido = recaptchaService.validar(dto.captcha());
        if (!captchaValido) {
            throw new RegraDeNegocioVioladaException("reCAPTCHA inválido. Por favor, tente novamente.");
        }

        if(candidatoRepository.existsByEmailCandidato(dto.emailCandidato())){
            throw  new EntidadeJaExistenteException("E-mail já cadastrado");
        }
        if(candidatoRepository.existsByRaAluno(dto.raAluno())){
            throw new EntidadeJaExistenteException("RA já cadastrado");
        }

        Candidato candidato = new Candidato();
                candidato.setNomeCompleto(dto.nomeCompleto());
                candidato.setRaAluno(dto.raAluno());
                candidato.setEmailCandidato(dto.emailCandidato());
                candidato.setSenha(passwordEncoder.encode(dto.senha()));
                candidato.setRole(Role.ROLE_CANDIDATO);

        candidatoRepository.save(candidato);
        String codigo = gerarCodigo.gerarCodigoValidacao(candidato.getEmailCandidato());

        enviarEmail.enviarEmail(candidato.getEmailCandidato(), "OLá, segue abaixo o código para validação de seu registro",codigo);


        return converterCandidatos(candidato);
    }

    //Atualizar dados faltantes
    public CandidatoResponseDTO atualizarDadosPerfil (CandidatoAtualizarPerfilDTo dadosPerfil, Long candidatoLogado){

        boolean validarCandidato = validarStatusCandidato(candidatoLogado);
        if(validarCandidato == Boolean.FALSE){
            throw new RegraDeNegocioVioladaException("Candidato Inativo");
        }

        Candidato candidato = candidatoRepository.findById(candidatoLogado).orElseThrow(() -> new DadosNaoEncontrados("Candidato nao encontrado"));

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
    public CandidatoResponseDTO listarDadosAlunoPorRa (Long id){
        Candidato c = candidatoRepository.findById(id).orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
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

    //Listar Todos os dados do Candidato(Teste)
    public CandidatoMostrarDTO mostrarDadosCandidato(Long id){
        Candidato candidato = candidatoRepository.findById(id).orElseThrow(() -> new RuntimeException("Candidato não encontrado"));
        return new CandidatoMostrarDTO(

                candidato.getId(),
                candidato.getNomeCompleto(),
                candidato.getEmailCandidato(),
                candidato.getTelefone(),
                candidato.getCidade(),
                candidato.getLinkLinkedin(),
                candidato.getLinkGithub(),
                candidato.getBioCandidato()
        );
    }
}
