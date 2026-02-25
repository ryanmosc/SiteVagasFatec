package com.fatec.vagasFatec.controller;

import com.fatec.vagasFatec.Dto.CandidaturaDTO.CandidaturaResponseDTO;
import com.fatec.vagasFatec.model.Candidatura;
import com.fatec.vagasFatec.service.CandidaturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/candidaturas")
public class CandidaturaController {
    private final CandidaturaService candidaturaService;

    @PostMapping("/{id_aluno}/{id_vaga}")
    public ResponseEntity<CandidaturaResponseDTO> criarCandidatura (@PathVariable  Long id_aluno, @PathVariable Long id_vaga){
        CandidaturaResponseDTO dto = candidaturaService.criarCandidatura(id_aluno, id_vaga);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<CandidaturaResponseDTO>> listarTodasAsCandidaturas(){
        List<CandidaturaResponseDTO> candidaturaResponseDTOS = candidaturaService.listarTodasCandidaturas();
        return ResponseEntity.ok().body(candidaturaResponseDTOS);
    }
}
