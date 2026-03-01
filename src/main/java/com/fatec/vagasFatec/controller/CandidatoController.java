package com.fatec.vagasFatec.controller;

import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoAtualizarPerfilDTo;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoCadastroDTO;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoResponseDTO;
import com.fatec.vagasFatec.service.CandidatoService;
import com.fatec.vagasFatec.utils.SecurityUtil;
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
        Long candidatoLogado = SecurityUtil.getCurrentUserId();
        CandidatoResponseDTO candidatoResponseDTO = candidatoService.listarDadosAlunoPorRa(candidatoLogado);
        return ResponseEntity.ok().body(candidatoResponseDTO);
    }

    @PatchMapping("/perfil")
    public ResponseEntity<CandidatoResponseDTO> atualizarDadosCandidato (@RequestBody@Valid CandidatoAtualizarPerfilDTo dto){
        Long candidatoLogado = SecurityUtil.getCurrentUserId();
        CandidatoResponseDTO candidatoResponseDTO = candidatoService.atualizarDadosPerfil(dto, candidatoLogado);
        return ResponseEntity.ok().body(candidatoResponseDTO);
    }

    @PatchMapping("/admin/{ra}/desativar")
    public ResponseEntity<Void> desativarCandidato (@PathVariable @Valid String raAluno){
        candidatoService.desativarCandidato(raAluno);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/{ra}/reativar")
    public ResponseEntity<Void> reativarCandidato (@PathVariable@Valid String raAluno){
        candidatoService.reativarCandidato(raAluno);
        return ResponseEntity.ok().build();
    }
}
