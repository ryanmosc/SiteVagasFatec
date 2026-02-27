package com.fatec.vagasFatec.controller;

import com.fatec.vagasFatec.Dto.CandidaturaDTO.CandidaturaObservacaoDTO;
import com.fatec.vagasFatec.Dto.CandidaturaDTO.CandidaturaResponseDTO;
import com.fatec.vagasFatec.model.Candidatura;
import com.fatec.vagasFatec.model.Enum.StatusCandidatura;
import com.fatec.vagasFatec.service.CandidaturaService;
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

    @PostMapping("/aluno/{id_aluno}/vaga/{id_vaga}")
    public ResponseEntity<CandidaturaResponseDTO> criarCandidatura (@PathVariable @Valid Long id_aluno, @PathVariable @Valid Long id_vaga){
        CandidaturaResponseDTO dto = candidaturaService.criarCandidatura(id_aluno, id_vaga);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<CandidaturaResponseDTO>> listarTodasAsCandidaturas(){
        List<CandidaturaResponseDTO> candidaturaResponseDTOS = candidaturaService.listarTodasCandidaturas();
        return ResponseEntity.ok().body(candidaturaResponseDTOS);
    }

    @GetMapping("/aluno/{id_aluno}")
    private ResponseEntity<List<CandidaturaResponseDTO>> listarVagasDoCandidato(@PathVariable @Valid Long id_aluno){
        List<CandidaturaResponseDTO> lista = candidaturaService.listarTodasCandidaturasAluno(id_aluno);
        return ResponseEntity.ok().body(lista);
    }

    @PatchMapping("/vaga/{vagaId}/aluno/{alunoId}/desistir")
    public ResponseEntity<Void> desistirVaga(@PathVariable @Valid Long vagaId, @PathVariable @Valid Long alunoId){
        candidaturaService.desistirCandidatura(alunoId, vagaId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/empresa/{id_empresa}/candidatura/{id_candidatura}/novo_status")
    public ResponseEntity<Void> alterarStatusCandidatura(@PathVariable @Valid Long id_empresa, @PathVariable @Valid Long id_candidatura, @RequestParam @Valid StatusCandidatura status){
        candidaturaService.alterarStatusCandidatura(id_candidatura, id_empresa, status);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("empresa/{id_empresa}/candidatura/{id_candidatura}/observacoes")
    public ResponseEntity<Void> adicionarObservacaoCandidatura(@PathVariable @Valid Long id_empresa, @PathVariable @Valid Long id_candidatura, @RequestBody @Valid CandidaturaObservacaoDTO observacaoDTO){
        candidaturaService.adicionarComentariosCandidatura(id_empresa, id_candidatura, observacaoDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/empresa/{idEmpresa}/vaga/{idVaga}/candidaturas_vaga")
    public ResponseEntity<List<CandidaturaResponseDTO>> listarPorVaga(
            @PathVariable @Valid Long idEmpresa,
            @PathVariable @Valid Long idVaga
    ) {
        return ResponseEntity.ok(
                candidaturaService.listarCandidaturasPorVaga(idVaga, idEmpresa)
        );
    }
}
