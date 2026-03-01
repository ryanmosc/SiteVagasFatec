package com.fatec.vagasFatec.auth.service;

import com.fatec.vagasFatec.auth.model.CustomUserDetails;
import com.fatec.vagasFatec.exceptions.DadosNaoEncontrados;
import com.fatec.vagasFatec.model.Candidato;
import com.fatec.vagasFatec.model.Empresa;
import com.fatec.vagasFatec.repository.CandidatoRepository;
import com.fatec.vagasFatec.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CandidatoRepository candidatoRepository;
    private final EmpresaRepository empresaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws DadosNaoEncontrados {

        Optional<Candidato> candidato = candidatoRepository.findByEmailCandidato(email);
        if (candidato.isPresent()) {
            return buildUser(candidato.get().getId(), candidato.get().getEmailCandidato(), candidato.get().getSenha(), candidato.get().getRole().name());
        }

        Optional<Empresa> empresa = empresaRepository.findByEmail(email);
        if (empresa.isPresent()) {
            return buildUser(empresa.get().getId(), empresa.get().getEmail(), empresa.get().getSenha(), empresa.get().getRole().name());
        }

        throw new UsernameNotFoundException("Usuário não encontrado");
    }

    private UserDetails buildUser(Long id, String email, String senha, String role) {
        return new CustomUserDetails(
                id,
                email,
                senha,
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
