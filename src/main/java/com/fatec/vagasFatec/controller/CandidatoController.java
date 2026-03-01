package com.fatec.vagasFatec.controller;

import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoAtualizarPerfilDTo;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoCadastroDTO;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoResponseDTO;
import com.fatec.vagasFatec.service.CandidatoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidatos")
@RequiredArgsConstructor
public class CandidatoController {

    private final CandidatoService candidatoService;

    //Não precisa de role
    @PostMapping
    public ResponseEntity<CandidatoResponseDTO> cadastroInicial (@RequestBody @Valid CandidatoCadastroDTO cadastroDTO){
        CandidatoResponseDTO candidatoResponseDTO = candidatoService.criarCandidato(cadastroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(candidatoResponseDTO);
    }

    //Precisa de role admin
    @GetMapping
    public ResponseEntity<List<CandidatoResponseDTO>> listarTodoscandidatos (){
        List<CandidatoResponseDTO> responseEntities = candidatoService.listarTodosCandidatos();
        return ResponseEntity.ok().body(responseEntities);
    }

    //Precisa ter role ROLE_CANDIDATO e o id ser o certo
    @GetMapping("/perfil")
    public ResponseEntity<CandidatoResponseDTO> listarDadosAlunoPorRa(){
        CandidatoResponseDTO candidatoResponseDTO = candidatoService.listarDadosAlunoPorRa(id);
        return ResponseEntity.ok().body(candidatoResponseDTO);
    }

    @PatchMapping("/{ra}")
    public ResponseEntity<CandidatoResponseDTO> atualizarDadosCandidato (@PathVariable @Valid String raAluno, @RequestBody@Valid CandidatoAtualizarPerfilDTo dto){
        CandidatoResponseDTO candidatoResponseDTO = candidatoService.atualizarDadosPerfil(dto, raAluno);
        return ResponseEntity.ok().body(candidatoResponseDTO);
    }

    @PatchMapping("/{ra}/desativar")
    public ResponseEntity<Void> desativarCandidato (@PathVariable @Valid String raAluno){
        candidatoService.desativarCandidato(raAluno);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{ra}/reativar")
    public ResponseEntity<Void> reativarCandidato (@PathVariable@Valid String raAluno){
        candidatoService.reativarCandidato(raAluno);
        return ResponseEntity.ok().build();
    }
}
