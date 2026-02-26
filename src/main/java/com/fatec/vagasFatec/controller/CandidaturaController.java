package com.fatec.vagasFatec.controller;

import com.fatec.vagasFatec.Dto.CandidaturaDTO.CandidaturaObservacaoDTO;
import com.fatec.vagasFatec.Dto.CandidaturaDTO.CandidaturaResponseDTO;
import com.fatec.vagasFatec.model.Candidatura;
import com.fatec.vagasFatec.model.Enum.StatusCandidatura;
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

    @GetMapping("/{id_aluno}/minhas")
    private ResponseEntity<List<CandidaturaResponseDTO>> listarVagasDoCandidato(@PathVariable Long id_aluno){
        List<CandidaturaResponseDTO> lista = candidaturaService.listarTodasCandidaturasAluno(id_aluno);
        return ResponseEntity.ok().body(lista);
    }

    @PatchMapping("/desistir/{vagaId}/{alunoId}")
    public ResponseEntity<Void> desistirVaga(@PathVariable Long vagaId, @PathVariable Long alunoId){
        candidaturaService.desistirCandidatura(alunoId, vagaId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/alterar-status/{id_empresa}/{id_candidatura}/novo_status")
    public ResponseEntity<Void> alterarStatusCandidatura(@PathVariable Long id_empresa, @PathVariable Long id_candidatura, @RequestParam StatusCandidatura status){
        candidaturaService.alterarStatusCandidatura(id_candidatura, id_empresa, status);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id_empresa}/{id_candidatura}/observacoes")
    public ResponseEntity<Void> adicionarObservacaoCandidatura(@PathVariable Long id_empresa, @PathVariable Long id_candidatura, @RequestBody CandidaturaObservacaoDTO observacaoDTO){
        candidaturaService.adicionarComentariosCandidatura(id_empresa, id_candidatura, observacaoDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/empresas/{idEmpresa}/vagas/{idVaga}/candidaturas")
    public ResponseEntity<List<CandidaturaResponseDTO>> listarPorVaga(
            @PathVariable Long idEmpresa,
            @PathVariable Long idVaga
    ) {
        return ResponseEntity.ok(
                candidaturaService.listarCandidaturasPorVaga(idVaga, idEmpresa)
        );
    }
}
