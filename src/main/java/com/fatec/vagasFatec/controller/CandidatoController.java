package com.fatec.vagasFatec.controller;

import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoAtualizarPerfilDTo;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoCadastroDTO;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoResponseDTO;
import com.fatec.vagasFatec.service.CandidatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidato")
@RequiredArgsConstructor
public class CandidatoController {

    private final CandidatoService candidatoService;

    @PostMapping("/cadastro")
    public ResponseEntity<CandidatoResponseDTO> cadastroInicial (@RequestBody CandidatoCadastroDTO cadastroDTO){
        CandidatoResponseDTO candidatoResponseDTO = candidatoService.criarCandidato(cadastroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(candidatoResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<CandidatoResponseDTO>> listarTodoscandidatos (){
        List<CandidatoResponseDTO> responseEntities = candidatoService.listarTodosCandidatos();
        return ResponseEntity.ok().body(responseEntities);
    }

    @GetMapping("/cadastro/{raAluno}/meus_dados")
    public ResponseEntity<CandidatoResponseDTO> listarDadosAlunoPorRa(@PathVariable String raAluno){
        CandidatoResponseDTO candidatoResponseDTO = candidatoService.listarDadosAlunoPorRa(raAluno);
        return ResponseEntity.ok().body(candidatoResponseDTO);
    }

    @PostMapping("/cadastro/{raAluno}/meus_dados")
    public ResponseEntity<CandidatoResponseDTO> atualizarDadosCandidato (@PathVariable String raAluno, @RequestBody CandidatoAtualizarPerfilDTo dto){
        CandidatoResponseDTO candidatoResponseDTO = candidatoService.atualizarDadosPerfil(dto, raAluno);
        return ResponseEntity.ok().body(candidatoResponseDTO);
    }
}
