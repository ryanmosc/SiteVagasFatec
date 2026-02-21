package com.fatec.vagasFatec.service;

import com.fatec.vagasFatec.Dto.UsuarioDto.UsuarioResponseDTO;
import com.fatec.vagasFatec.model.Usuario;
import com.fatec.vagasFatec.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Usuarioservice {
    private final UsuarioRepository usuarioRepository;

    public UsuarioResponseDTO criarUsuario(Usuario u){

        Usuario usuario = usuarioRepository.save(u);

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}
