package com.fatec.vagasFatec.auth.controller;

import com.fatec.vagasFatec.auth.service.JwtService;
import com.fatec.vagasFatec.model.Candidato;
import com.fatec.vagasFatec.model.Empresa;
import com.fatec.vagasFatec.repository.CandidatoRepository;
import com.fatec.vagasFatec.repository.EmpresaRepository;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.senha()
                )
        );

        UserDetails user = (UserDetails) auth.getPrincipal();

        // Descobre se é candidato ou empresa
        Optional<Candidato> candidato = candidatoRepository.findByEmailCandidato(user.getUsername());
        if (candidato.isPresent()) {
            String token = jwtService.generateToken(
                    candidato.get().getId(),
                    candidato.get().getEmailCandidato(),
                    candidato.get().getRole().name()
            );
            return ResponseEntity.ok(new LoginResponseDTO(token));
        }

        Empresa empresa = empresaRepository.findByEmail(user.getUsername()).get();

        String token = jwtService.generateToken(
                empresa.getId(),
                empresa.getEmail(),
                empresa.getRole().name()
        );

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
