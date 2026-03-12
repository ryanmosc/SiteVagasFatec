package com.fatec.vagasFatec.controller;

import com.fatec.vagasFatec.Dto.VagaDto.VagaUpdateDTO;
import com.fatec.vagasFatec.Dto.VagaDto.VagasResponseDTO;
import com.fatec.vagasFatec.model.Empresa;
import com.fatec.vagasFatec.model.Enum.CursosEnum;
import com.fatec.vagasFatec.model.Vaga;
import com.fatec.vagasFatec.service.VagasService;
import com.fatec.vagasFatec.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vagas")
@RequiredArgsConstructor
@Tag(name = "Vagas", description = "Endpoints para publicação, manutenção e consulta de oportunidades de emprego")
public class VagasController {

    private final VagasService vagasService;

    @PostMapping
    @Operation(summary = "Anunciar nova vaga", description = "Cria uma nova oportunidade de vaga vinculada à empresa autenticada.")
    @ApiResponse(responseCode = "201", description = "Vaga publicada com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado - Apenas empresas podem publicar")
    public ResponseEntity<VagasResponseDTO> criarVaga(@RequestBody @Valid Vaga vaga){
        VagasResponseDTO vagasResponseDTO = vagasService.criarVaga(vaga);
        return ResponseEntity.status(HttpStatus.CREATED).body(vagasResponseDTO);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar dados da vaga", description = "Permite editar informações da vaga. O sistema valida se a empresa logada é a dona da vaga.")
    @ApiResponse(responseCode = "200", description = "Vaga atualizada com sucesso")
    public ResponseEntity<VagasResponseDTO> atualizarVaga(@PathVariable Long id, @RequestBody @Valid VagaUpdateDTO dto) {
        VagasResponseDTO response = vagasService.editarVaga(dto, id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/encerrar")
    @Operation(summary = "Encerrar vaga", description = "Altera o status da vaga para encerrado, impedindo novas candidaturas.")
    @ApiResponse(responseCode = "200", description = "Vaga encerrada com sucesso")
    public ResponseEntity<Void> encerrarVaga(@PathVariable Long id) {
        vagasService.encerrarVaga(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/reabrir")
    @Operation(summary = "Reabrir vaga", description = "Torna uma vaga encerrada disponível novamente para candidaturas.")
    @ApiResponse(responseCode = "200", description = "Vaga reaberta com sucesso")
    public ResponseEntity<Void> reabrirVaga(@PathVariable Long id) {
        vagasService.reabrirVaga(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover vaga", description = "Exclui permanentemente a vaga do sistema.")
    @ApiResponse(responseCode = "200", description = "Vaga deletada com sucesso")
    public ResponseEntity<Void> deletarVaga(@PathVariable Long id) {
        vagasService.deletarVaga(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Listar vagas disponíveis", description = "Retorna todas as vagas abertas. Permite filtragem opcional por curso da FATEC.")
    @ApiResponse(responseCode = "200", description = "Lista de vagas recuperada")
    public ResponseEntity<List<VagasResponseDTO>> listarTodasVagas(
            @Parameter(description = "Filtro opcional por curso (ex: ADS, DSM, GE)")
            @RequestParam(required = false) CursosEnum curso) {
        return ResponseEntity.ok(vagasService.listarVagas(curso));
    }

    @GetMapping("/minhas")
    @Operation(summary = "Listar minhas vagas (Empresa)", description = "Recupera todas as vagas publicadas exclusivamente pela empresa autenticada.")
    @ApiResponse(responseCode = "200", description = "Lista de vagas da empresa retornada")
    public ResponseEntity<List<VagasResponseDTO>> listarMinhasVagas() {
        Long idEmpresa = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(vagasService.listarMinhasVagas(idEmpresa));
    }
}