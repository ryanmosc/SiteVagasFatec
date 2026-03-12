package com.fatec.vagasFatec.controller;

import com.fatec.vagasFatec.Dto.CandidaturaDTO.CandidaturaObservacaoDTO;
import com.fatec.vagasFatec.Dto.CandidaturaDTO.CandidaturaResponseDTO;
import com.fatec.vagasFatec.model.Candidatura;
import com.fatec.vagasFatec.model.Enum.StatusCandidatura;
import com.fatec.vagasFatec.service.CandidaturaService;
import com.fatec.vagasFatec.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/candidaturas")
@Tag(name = "Candidatura", description = "Gerenciamento de aplicações em vagas, histórico e status")
public class CandidaturaController {
    private final CandidaturaService candidaturaService;

    @PostMapping("/vaga/{id_vaga}")
    @Operation(summary = "Aplicar para uma vaga", description = "Cria uma nova candidatura para o aluno autenticado na vaga especificada.")
    @ApiResponse(responseCode = "201", description = "Candidatura realizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Candidatura duplicada ou vaga inexistente")
    public ResponseEntity<CandidaturaResponseDTO> criarCandidatura (@PathVariable @Valid Long id_vaga){
        Long candidatoLogado = SecurityUtil.getCurrentUserId();
        CandidaturaResponseDTO dto = candidaturaService.criarCandidatura(candidatoLogado, id_vaga);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    @Operation(summary = "Listar todas as candidaturas (Admin)", description = "Retorna o histórico global de candidaturas do sistema. Requer ROLE_ADMIN.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    public ResponseEntity<List<CandidaturaResponseDTO>> listarTodasAsCandidaturas(){
        List<CandidaturaResponseDTO> candidaturaResponseDTOS = candidaturaService.listarTodasCandidaturas();
        return ResponseEntity.ok().body(candidaturaResponseDTOS);
    }

    @GetMapping("/minhas")
    @Operation(summary = "Ver minhas candidaturas", description = "Retorna todas as candidaturas realizadas pelo aluno autenticado.")
    @ApiResponse(responseCode = "200", description = "Lista de candidaturas do aluno retornada")
    public ResponseEntity<List<CandidaturaResponseDTO>> listarVagasDoCandidato(){
        Long candidatoLogado = SecurityUtil.getCurrentUserId();
        List<CandidaturaResponseDTO> lista = candidaturaService.listarTodasCandidaturasAluno(candidatoLogado);
        return ResponseEntity.ok().body(lista);
    }

    @PatchMapping("/vaga/{vagaId}/desistir")
    @Operation(summary = "Desistir de candidatura", description = "Permite ao candidato cancelar sua participação no processo seletivo de uma vaga.")
    @ApiResponse(responseCode = "200", description = "Desistência processada com sucesso")
    public ResponseEntity<Void> desistirVaga(@PathVariable @Valid Long vagaId){
        Long candidatoLogado = SecurityUtil.getCurrentUserId();
        candidaturaService.desistirCandidatura(candidatoLogado, vagaId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/empresa/candidatura/{id_candidatura}/novo_status")
    @Operation(summary = "Alterar status da candidatura", description = "Permite à empresa atualizar o status (Ex: Em análise, Aprovado, Rejeitado).")
    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado: Empresa não é dona da vaga")
    public ResponseEntity<Void> alterarStatusCandidatura(@PathVariable @Valid Long id_candidatura, @RequestParam @Valid StatusCandidatura status){
        Long empresaLogada = SecurityUtil.getCurrentUserId();
        candidaturaService.alterarStatusCandidatura(id_candidatura, empresaLogada, status);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("empresa/candidatura/{id_candidatura}/observacoes")
    @Operation(summary = "Adicionar observações internas", description = "Permite à empresa adicionar comentários sobre o candidato para controle interno.")
    @ApiResponse(responseCode = "204", description = "Observação adicionada com sucesso")
    public ResponseEntity<Void> adicionarObservacaoCandidatura(@PathVariable @Valid Long id_candidatura, @RequestBody @Valid CandidaturaObservacaoDTO observacaoDTO){
        Long candidatoLogado = SecurityUtil.getCurrentUserId();
        candidaturaService.adicionarComentariosCandidatura(candidatoLogado, id_candidatura, observacaoDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/empresa/vaga/{idVaga}/candidaturas_vaga")
    @Operation(summary = "Listar candidatos por vaga", description = "Retorna todos os candidatos inscritos em uma vaga específica da empresa.")
    @ApiResponse(responseCode = "200", description = "Lista de inscritos retornada")
    public ResponseEntity<List<CandidaturaResponseDTO>> listarPorVaga(
            @PathVariable @Valid Long idVaga
    ) {
        Long candidatoLogado = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(
                candidaturaService.listarCandidaturasPorVaga(idVaga, candidatoLogado)
        );
    }
}