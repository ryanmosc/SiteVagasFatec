package com.fatec.vagasFatec.controller;

import com.fatec.vagasFatec.Dto.UsuarioDto.UsuarioResponseDTO;
import com.fatec.vagasFatec.model.Usuario;
import com.fatec.vagasFatec.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioservice;

    @PostMapping
    public UsuarioResponseDTO criarUsuario(@RequestBody Usuario usuario){
        return usuarioservice.criarUsuario(usuario);
    }

    @GetMapping
    public List<UsuarioResponseDTO> listarUsuarios(){
        return usuarioservice.listarUsuarios();
    }

    @GetMapping("/teste")
    public String teste() {
        return "ok";
    }
}
