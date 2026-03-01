package com.fatec.vagasFatec.controller;

import com.fatec.vagasFatec.Dto.CandidaturaDTO.CandidaturaObservacaoDTO;
import com.fatec.vagasFatec.Dto.CandidaturaDTO.CandidaturaResponseDTO;
import com.fatec.vagasFatec.model.Candidatura;
import com.fatec.vagasFatec.model.Enum.StatusCandidatura;
import com.fatec.vagasFatec.service.CandidaturaService;
import com.fatec.vagasFatec.utils.SecurityUtil;
import jakarta.validation.Valid;
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

    @PostMapping("/vaga/{id_vaga}")
    public ResponseEntity<CandidaturaResponseDTO> criarCandidatura (@PathVariable @Valid Long id_vaga){
        Long candidatoLogado = SecurityUtil.getCurrentUserId();
        CandidaturaResponseDTO dto = candidaturaService.criarCandidatura(candidatoLogado, id_vaga);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    //Precisa ter a rola de admin
    @GetMapping
    public ResponseEntity<List<CandidaturaResponseDTO>> listarTodasAsCandidaturas(){
        List<CandidaturaResponseDTO> candidaturaResponseDTOS = candidaturaService.listarTodasCandidaturas();
        return ResponseEntity.ok().body(candidaturaResponseDTOS);
    }

    //Precisa ter  a role de candidato
    @GetMapping("/vaga/minhas")
    public ResponseEntity<List<CandidaturaResponseDTO>> listarVagasDoCandidato(){
        Long candidatoLogado = SecurityUtil.getCurrentUserId();
        List<CandidaturaResponseDTO> lista = candidaturaService.listarTodasCandidaturasAluno(candidatoLogado);
        return ResponseEntity.ok().body(lista);
    }

    @PatchMapping("/vaga/{vagaId}/desistir")
    public ResponseEntity<Void> desistirVaga(@PathVariable @Valid Long vagaId){
        Long candidatoLogado = SecurityUtil.getCurrentUserId();
        candidaturaService.desistirCandidatura(candidatoLogado, vagaId);
        return ResponseEntity.ok().build();
    }

    //Precisa ter a role de empresa
    @PatchMapping("/empresa/candidatura/{id_candidatura}/novo_status")
    public ResponseEntity<Void> alterarStatusCandidatura(@PathVariable @Valid Long id_candidatura, @RequestParam @Valid StatusCandidatura status){
        Long empresaLogada = SecurityUtil.getCurrentUserId();
        candidaturaService.alterarStatusCandidatura(id_candidatura, empresaLogada, status);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("empresa/candidatura/{id_candidatura}/observacoes")
    public ResponseEntity<Void> adicionarObservacaoCandidatura(@PathVariable @Valid Long id_candidatura, @RequestBody @Valid CandidaturaObservacaoDTO observacaoDTO){
        Long candidatoLogado = SecurityUtil.getCurrentUserId();
        candidaturaService.adicionarComentariosCandidatura(candidatoLogado, id_candidatura, observacaoDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/empresa/vaga/{idVaga}/candidaturas_vaga")
    public ResponseEntity<List<CandidaturaResponseDTO>> listarPorVaga(
            @PathVariable @Valid Long idVaga
    ) {
        Long candidatoLogado = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(
                candidaturaService.listarCandidaturasPorVaga(idVaga, candidatoLogado)
        );
    }
}
