package com.fatec.vagasFatec.auth.controller;

import com.fatec.vagasFatec.auth.service.JwtService;
import com.fatec.vagasFatec.exceptions.RegraDeNegocioVioladaException;
import com.fatec.vagasFatec.model.Candidato;
import com.fatec.vagasFatec.model.Empresa;
import com.fatec.vagasFatec.model.Enum.StatusCandidato;
import com.fatec.vagasFatec.repository.CandidatoRepository;
import com.fatec.vagasFatec.repository.EmpresaRepository;
import com.fatec.vagasFatec.utils.EmailSender;
import com.fatec.vagasFatec.utils.VerificationCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CandidatoRepository candidatoRepository;
    private final EmpresaRepository empresaRepository;
    private final EmailSender enviarEmail;
    private final VerificationCodeGenerator gerarCodigo;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        // Autentica primeiro (email + senha)
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.senha()
                )
        );

        String email = auth.getName();  // email que foi autenticado com sucesso

        // Tenta encontrar como Candidato
        Optional<Candidato> optCandidato = candidatoRepository.findByEmailCandidato(email);

        if (optCandidato.isPresent()) {
            Candidato candidato = optCandidato.get();

            // Validações específicas de candidato
            if (candidato.getStatusCandidato() == StatusCandidato.AGUARDANDO) {
                String codigo = gerarCodigo.gerarCodigoValidacao(candidato.getEmailCandidato());
                enviarEmail.enviarEmail(
                        candidato.getEmailCandidato(),
                        "OLá, segue abaixo o código para validação de seu registro",
                        codigo
                );
                throw new RegraDeNegocioVioladaException(
                        "Favor validar sua conta. Verifique o e-mail: " +
                                candidato.getEmailCandidato() + " e confirme o código"
                );
            }

            if (candidato.getStatusCandidato() != StatusCandidato.ATIVO) {
                throw new RegraDeNegocioVioladaException("Usuário Inativo");
            }

            // Gera token para candidato
            String token = jwtService.generateToken(
                    candidato.getId(),
                    candidato.getEmailCandidato(),
                    candidato.getRole().name()
            );

            return ResponseEntity.ok(new LoginResponseDTO(token));
        }

        // Se não é candidato → tenta Empresa
        Optional<Empresa> optEmpresa = empresaRepository.findByEmail(email);

        if (optEmpresa.isPresent()) {
            Empresa empresa = optEmpresa.get();

            // Aqui você pode adicionar validações futuras para empresa, se precisar
            // Exemplo: if (empresa.getStatusEmpresa() != StatusEmpresa.ATIVO) { throw ... }

            // Gera token para empresa
            String token = jwtService.generateToken(
                    empresa.getId(),
                    empresa.getEmail(),
                    empresa.getRole().name()
            );

            return ResponseEntity.ok(new LoginResponseDTO(token));
        }

        // Caso raro: autenticou mas não achou em nenhum repositório
        throw new RegraDeNegocioVioladaException("Usuário não encontrado");
    }}
