package com.fatec.vagasFatec.utils;

import com.fatec.vagasFatec.Dto.CandidatoDTO.ValidarCandidatoDTO;
import com.fatec.vagasFatec.Dto.EmpresaDTO.ValidarEmpresaDTO;
import com.fatec.vagasFatec.exceptions.DadosInvalidosException;
import com.fatec.vagasFatec.exceptions.DadosNaoEncontrados;
import com.fatec.vagasFatec.exceptions.OperacaoNaoPermitidaException;
import com.fatec.vagasFatec.model.Candidato;
import com.fatec.vagasFatec.model.Empresa;
import com.fatec.vagasFatec.model.Enum.StatusCandidato;
import com.fatec.vagasFatec.model.Enum.StatusEmpresa;
import com.fatec.vagasFatec.repository.CandidatoRepository;
import com.fatec.vagasFatec.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
//Sempre colocar uma anotação de @Service, nestas classes que precisam mexer com componentes, e se aonde você for usar tiver uma anotation do Lombok sem service ele cai
public class VerificationCodeGenerator {

    public static final String CHARS = "0123456789";
    public static final SecureRandom RANDOM = new SecureRandom();
    public static final int CODE_LENGHT = 6;
    @Autowired
    private CandidatoRepository candidatoRepository;
    @Autowired
    private EmpresaRepository empresaRepository;


    public  String gerarCodigoValidacao(String email) {
        StringBuilder stringBuilder = new StringBuilder(CODE_LENGHT);
        Optional<Candidato> candidatoOpt = candidatoRepository.findByEmailCandidato(email);

        if (candidatoOpt.isPresent()) {
            Candidato candidato = candidatoOpt.get();
            for (int i = 0; i < CODE_LENGHT; i++) {
                int index = RANDOM.nextInt(CHARS.length());
                stringBuilder.append(CHARS.charAt(index));

            }
            System.out.println("Cadastro de usuario");
            System.out.println(stringBuilder.toString());
            candidato.setToken(stringBuilder.toString());
            candidato.setCreatedAt(LocalDateTime.now());
            candidato.setExpiresAt(candidato.getCreatedAt().plusMinutes(2));
            candidatoRepository.save(candidato);
            return stringBuilder.toString();
        }

        Optional<Empresa> empresaOpt = empresaRepository.findByEmail(email);
        if (empresaOpt.isPresent()) {
            Empresa empresa = empresaOpt.get();
            for (int i = 0; i < CODE_LENGHT; i++) {
                int index = RANDOM.nextInt(CHARS.length());
                stringBuilder.append(CHARS.charAt(index));
            }
            System.out.println("Cadastro de empresa");
            System.out.println(stringBuilder.toString());
            empresa.setToken(stringBuilder.toString());
            empresa.setCreatedAt(LocalDateTime.now());
            empresa.setExpiresAt(empresa.getCreatedAt().plusMinutes(2));
            empresaRepository.save(empresa);
            return stringBuilder.toString();
        }
        throw new DadosNaoEncontrados("Email não encontrado nem como candidato nem como empresa");
    }



    public String validarCodigo(ValidarCandidatoDTO validarCandidatoDTO){
        Candidato candidato = candidatoRepository.findByEmailCandidato(validarCandidatoDTO.email()).orElseThrow(() -> new DadosNaoEncontrados("Candidato nao encontrado"));

        if(!validarCandidatoDTO.codigoEmail().equals(candidato.getToken())){
            throw new DadosInvalidosException("Token está errado");
        }
        if (LocalDateTime.now().isAfter(candidato.getExpiresAt())) {
            throw new DadosInvalidosException("Código expirado. Solicite um novo.");
        }
        if (candidato.getToken() == null) {
            throw new DadosInvalidosException("Nenhum código de validação pendente");
        }
        candidato.setStatusCandidato(StatusCandidato.ATIVO);
        candidatoRepository.save(candidato);
        return "Candidato ativado";


    }

    public String validarCodigoEmpresa(ValidarEmpresaDTO validarEmpresaDTO){
        Empresa empresa = empresaRepository.findByEmail(validarEmpresaDTO.email()).orElseThrow(() -> new DadosNaoEncontrados("Empresa nao encontrada"));

        if(!validarEmpresaDTO.codigoEmail().equals(empresa.getToken())){
            throw new DadosInvalidosException("Token está errado");
        }
        if (LocalDateTime.now().isAfter(empresa.getExpiresAt())) {
            throw new DadosInvalidosException("Código expirado. Solicite um novo.");
        }
        if (empresa.getToken() == null) {
            throw new DadosInvalidosException("Nenhum código de validação pendente");
        }
        empresa.setStatusEmpresa(StatusEmpresa.ATIVO);
        empresaRepository.save(empresa);
        return "Candidato ativado";


    }
}
