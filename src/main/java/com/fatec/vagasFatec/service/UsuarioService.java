package com.fatec.vagasFatec.service;

import com.fatec.vagasFatec.Dto.UsuarioDto.UsuarioResponseDTO;
import com.fatec.vagasFatec.model.Usuario;
import com.fatec.vagasFatec.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioResponseDTO criarUsuario(Usuario u){

        Usuario usuario = usuarioRepository.save(u);

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }

    public List<UsuarioResponseDTO> listarUsuarios(){
        return usuarioRepository.findAll().stream().map(usuario -> new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        ))
                .toList();
    }
}
