package com.fatec.vagasFatec.controller;


import com.fatec.vagasFatec.Dto.VagaDto.VagaUpdateDTO;
import com.fatec.vagasFatec.Dto.VagaDto.VagasResponseDTO;
import com.fatec.vagasFatec.model.Enum.CursosEnum;
import com.fatec.vagasFatec.model.Enum.StatusVaga;
import com.fatec.vagasFatec.model.Vaga;
import com.fatec.vagasFatec.service.VagasService;
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
    public ResponseEntity<VagasResponseDTO> criarVaga(@RequestBody Vaga vaga){
        VagasResponseDTO vagasResponseDTO = vagasService.criarVaga(vaga);
        return ResponseEntity.status(HttpStatus.CREATED).body(vagasResponseDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VagasResponseDTO> atualizarVaga(@RequestBody VagaUpdateDTO vaga, @PathVariable Long id){
        VagasResponseDTO dto = vagasService.editarVaga(vaga, id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PatchMapping("/{id}/encerrar")
    public ResponseEntity<Void> encerrarVaga(@PathVariable Long id){
        vagasService.encerrarVaga(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<Void> reabrirvaga(@PathVariable Long id){
        vagasService.reabrirVaga(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVaga(@PathVariable Long id){
        vagasService.deletarVaga(id);
        return ResponseEntity.ok().build();
    }

    //End point Publico para alunos, so vai retornar as vagas abertas
    @GetMapping
    public ResponseEntity<List<VagasResponseDTO>> listarTodasVagas(){

        List<VagasResponseDTO> vagas = vagasService.listarVagas();
        return ResponseEntity.ok(vagas);
    }

    //Endpoint somente para empresas e retorna as abertas e fechadas
    @GetMapping("/{id}/minhas")
    public ResponseEntity<List<VagasResponseDTO>> listarMinhasVagas(@PathVariable Long id){
        List<VagasResponseDTO> vagasResponseDTOS = vagasService.listarMinhasVagas(id);
        return ResponseEntity.ok(vagasResponseDTOS);
    }
}
