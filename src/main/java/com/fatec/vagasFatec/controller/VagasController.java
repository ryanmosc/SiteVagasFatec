package com.fatec.vagasFatec.controller;

import com.fatec.vagasFatec.Dto.VagaDto.VagaUpdateDTO;
import com.fatec.vagasFatec.Dto.VagaDto.VagasResponseDTO;
import com.fatec.vagasFatec.model.Empresa;
import com.fatec.vagasFatec.model.Enum.CursosEnum;
import com.fatec.vagasFatec.model.Vaga;
import com.fatec.vagasFatec.service.VagasService;
import com.fatec.vagasFatec.utils.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vagas")
@RequiredArgsConstructor
public class VagasController {

    private final VagasService vagasService;

    @PostMapping
    public ResponseEntity<VagasResponseDTO> criarVaga(@RequestBody @Valid Vaga vaga){
        VagasResponseDTO vagasResponseDTO = vagasService.criarVaga(vaga);
        return ResponseEntity.status(HttpStatus.CREATED).body(vagasResponseDTO);
    }

    // Atualizar vaga (deve validar ownership no service)
    @PatchMapping("/{id}")
    public ResponseEntity<VagasResponseDTO> atualizarVaga(@PathVariable Long id, @RequestBody @Valid VagaUpdateDTO dto) {
        VagasResponseDTO response = vagasService.editarVaga(dto, id);
        return ResponseEntity.ok(response);
    }

    // Ações de gerenciamento: padronizar sem {id} na rota quando possível, mas aqui ok manter
    @PatchMapping("/{id}/encerrar")
    public ResponseEntity<Void> encerrarVaga(@PathVariable Long id) {
        vagasService.encerrarVaga(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<Void> reabrirVaga(@PathVariable Long id) {
        vagasService.reabrirVaga(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVaga(@PathVariable Long id) {
        vagasService.deletarVaga(id);
        return ResponseEntity.ok().build();
    }

    // Público: vagas abertas
    @GetMapping
    public ResponseEntity<List<VagasResponseDTO>> listarTodasVagas(@RequestParam(required = false) CursosEnum curso) {
        return ResponseEntity.ok(vagasService.listarVagas(curso));
    }

    // Minhas vagas (empresa logada)
    @GetMapping("/minhas")
    public ResponseEntity<List<VagasResponseDTO>> listarMinhasVagas() {
        Long idEmpresa = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(vagasService.listarMinhasVagas(idEmpresa));
    }
}