package com.fatec.vagasFatec.controller;

import com.fatec.vagasFatec.Dto.UsuarioDto.UsuarioResponseDTO;
import com.fatec.vagasFatec.model.Usuario;
import com.fatec.vagasFatec.service.Usuarioservice;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final Usuarioservice usuarioservice;

    @PostMapping
    public UsuarioResponseDTO CriarUsuario(@RequestBody @Valid Usuario usuario){
        return usuarioservice.criarUsuario(usuario);
    }

    @GetMapping("/teste")
    public String teste() {
        return "ok";
    }
}
