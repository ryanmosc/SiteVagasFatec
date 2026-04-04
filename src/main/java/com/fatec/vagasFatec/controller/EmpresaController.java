package com.fatec.vagasFatec.controller;

import com.fatec.vagasFatec.Dto.EmpresaDTO.EmpreRequestDTO;
import com.fatec.vagasFatec.Dto.EmpresaDTO.EmpresaAtualizarDTO;
import com.fatec.vagasFatec.Dto.EmpresaDTO.EmpresaResponseDTO;
import com.fatec.vagasFatec.model.Empresa;
import com.fatec.vagasFatec.service.EmpresaService;
import com.fatec.vagasFatec.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
@Tag(name = "Empresa", description = "Endpoints para gestão de empresas e seus perfis corporativos")
public class EmpresaController {

    private final EmpresaService empresaService;

    @GetMapping("/perfil")
    @Operation(summary = "Listar Dados Empresa", description = "Lista todos os dados (Atualizaveis) da empresa logada")
    @ApiResponse(responseCode = "200", description = "Dados retornados com sucesso")
    public ResponseEntity<EmpresaAtualizarDTO> listarDadosEmpresa(){
        EmpresaAtualizarDTO dto = empresaService.listarDadosEmpresa();
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    @Operation(summary = "Cadastrar nova empresa", description = "Realiza o cadastro de uma nova organização no sistema. Acesso público.")
    @ApiResponse(responseCode = "201", description = "Empresa cadastrada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou CNPJ já cadastrado")
    public ResponseEntity<EmpresaResponseDTO> criarEmpresa(@RequestBody @Valid EmpreRequestDTO empresa) {
        EmpresaResponseDTO response = empresaService.criarEmpresa(empresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar todas as empresas (Admin)", description = "Retorna a listagem completa de empresas parceiras. Requer ROLE_ADMIN.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    public ResponseEntity<List<EmpresaResponseDTO>> listarEmpresas() {
        return ResponseEntity.ok(empresaService.listarEmpresas());
    }

    @PatchMapping("/perfil")
    @Operation(summary = "Atualizar dados da empresa", description = "Permite que a empresa autenticada atualize suas informações de perfil.")
    @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso")
    public ResponseEntity<EmpresaResponseDTO> atualizarDadosEmpresa(@RequestBody EmpresaAtualizarDTO dados){
        Long empresaLogada = SecurityUtil.getCurrentUserId();
        EmpresaResponseDTO dadosAtt = empresaService.atualizarDadosEmpresa(dados, empresaLogada);
        return ResponseEntity.ok(dadosAtt);
    }
}