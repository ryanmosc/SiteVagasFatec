package com.fatec.vagasFatec.controller;

import com.fatec.vagasFatec.Dto.EmpresaDTO.EmpreRequestDTO;
import com.fatec.vagasFatec.Dto.EmpresaDTO.EmpresaResponseDTO;
import com.fatec.vagasFatec.model.Empresa;
import com.fatec.vagasFatec.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    // Criar empresa
    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> criarEmpresa(@RequestBody @Valid EmpreRequestDTO empresa) {
        EmpresaResponseDTO response = empresaService.criarEmpresa(empresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar todas
    @GetMapping
    public ResponseEntity<List<EmpresaResponseDTO>> listarEmpresas() {
        return ResponseEntity.ok(empresaService.listarEmpresas());
    }
}