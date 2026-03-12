package com.fatec.vagasFatec.controller;

import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoAtualizarPerfilDTo;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoCadastroDTO;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoResponseDTO;
import com.fatec.vagasFatec.auth.service.SecurityConfig;
import com.fatec.vagasFatec.exceptions.DadosNaoEncontrados;
import com.fatec.vagasFatec.exceptions.RegraDeNegocioVioladaException;
import com.fatec.vagasFatec.model.Candidato;
import com.fatec.vagasFatec.model.Candidatura;
import com.fatec.vagasFatec.model.Empresa;
import com.fatec.vagasFatec.repository.CandidatoRepository;
import com.fatec.vagasFatec.repository.CandidaturaRepository;
import com.fatec.vagasFatec.repository.EmpresaRepository;
import com.fatec.vagasFatec.service.CandidatoService;
import com.fatec.vagasFatec.utils.ConverterCurriculo;
import com.fatec.vagasFatec.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/api/candidatos")
@RequiredArgsConstructor
@SecurityRequirement(name = SecurityConfig.SECURITY)
@Tag(name = "Candidato", description = "Endpoints para gestão de perfis, currículos e administração de candidatos")
public class CandidatoController {

    private final CandidatoService candidatoService;
    private final ConverterCurriculo converterCurriculo;
    private final CandidatoRepository candidatoRepository;
    private final EmpresaRepository empresaRepository;
    private final CandidaturaRepository candidaturaRepository;

    @PostMapping
    @Operation(summary = "Realizar cadastro inicial", description = "Cria um novo registro de candidato/aluno no sistema. Acesso público.")
    @ApiResponse(responseCode = "201", description = "Candidato registrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou candidato já existente")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    public ResponseEntity<CandidatoResponseDTO> cadastroInicial (@RequestBody @Valid CandidatoCadastroDTO cadastroDTO){
        CandidatoResponseDTO candidatoResponseDTO = candidatoService.criarCandidato(cadastroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(candidatoResponseDTO);
    }

    @GetMapping
    @Operation(summary = "Listar todos os candidatos", description = "Retorna uma lista de todos os candidatos cadastrados. Requer privilégios de Administrador.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado - Permissão insuficiente")
    public ResponseEntity<List<CandidatoResponseDTO>> listarTodoscandidatos (){
        List<CandidatoResponseDTO> responseEntities = candidatoService.listarTodosCandidatos();
        return ResponseEntity.ok().body(responseEntities);
    }

    @GetMapping("/perfil")
    @Operation(summary = "Consultar perfil próprio", description = "Recupera os dados detalhados do candidato autenticado na sessão atual.")
    @ApiResponse(responseCode = "200", description = "Dados do perfil retornados com sucesso")
    public ResponseEntity<CandidatoResponseDTO> listarDadosAlunoPorRa(){
        Long candidatoLogado = SecurityUtil.getCurrentUserId();
        CandidatoResponseDTO candidatoResponseDTO = candidatoService.listarDadosAlunoPorRa(candidatoLogado);
        return ResponseEntity.ok().body(candidatoResponseDTO);
    }

    @PatchMapping("/perfil")
    @Operation(summary = "Atualizar informações de perfil", description = "Atualiza campos específicos do perfil do candidato logado.")
    @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso")
    public ResponseEntity<CandidatoResponseDTO> atualizarDadosCandidato (@RequestBody @Valid CandidatoAtualizarPerfilDTo dto){
        Long candidatoLogado = SecurityUtil.getCurrentUserId();
        CandidatoResponseDTO candidatoResponseDTO = candidatoService.atualizarDadosPerfil(dto, candidatoLogado);
        return ResponseEntity.ok().body(candidatoResponseDTO);
    }

    @PatchMapping(value = "/perfil/curriculo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Fazer upload de currículo", description = "Envia ou substitui o arquivo de currículo (PDF) do candidato autenticado.")
    @ApiResponse(responseCode = "204", description = "Arquivo processado e salvo com sucesso")
    public ResponseEntity<Void> atualizarCurriculo(@RequestParam("curriculo") MultipartFile curriculo) {
        converterCurriculo.salvarCurriculo(curriculo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/perfil/curriculo/visualizar")
    @Operation(summary = "Visualizar currículo pessoal", description = "Gera o fluxo de visualização do arquivo de currículo do próprio candidato.")
    @ApiResponse(responseCode = "200", description = "Arquivo carregado com sucesso")
    @ApiResponse(responseCode = "204", description = "Candidato não possui currículo cadastrado")
    public ResponseEntity<Resource> visualizarMeuCurriculo() throws MalformedURLException {
        Long id = SecurityUtil.getCurrentUserId();
        Candidato c = candidatoRepository.findById(id)
                .orElseThrow(() -> new DadosNaoEncontrados("Candidato não encontrado"));

        if (c.getCaminhoCurriculo() == null) {
            return ResponseEntity.noContent().build();
        }

        return converterCurriculo.visualizarCurriculo(c.getCaminhoCurriculo());
    }

    @GetMapping("/perfil/curriculo/visualizar/{id_candidatura}/candidatura")
    @Operation(summary = "Visualizar currículo de candidato (Empresa)", description = "Permite que a empresa visualize o currículo vinculado a uma candidatura específica de suas vagas.")
    @ApiResponse(responseCode = "200", description = "Visualização permitida e arquivo carregado")
    @ApiResponse(responseCode = "403", description = "Empresa não possui permissão para visualizar este currículo")
    public ResponseEntity<Resource> vizualisarCurriculoCandidato(@PathVariable Long id_candidatura) throws MalformedURLException {
        Long id = SecurityUtil.getCurrentUserId();
        Candidatura candidatura = candidaturaRepository.findById(id_candidatura).orElseThrow(() -> new DadosNaoEncontrados("Candidatura não encontrada"));
        Empresa e = empresaRepository.findById(id).orElseThrow(() -> new DadosNaoEncontrados("Empresa não encontrada"));
        if (e.getId() != candidatura.getVaga().getEmpresa().getId()){
            throw new RegraDeNegocioVioladaException("Empresa não é dona da vaga");
        }
        Candidato c = candidatoRepository.findById(candidatura.getCandidato().getId())
                .orElseThrow(() -> new DadosNaoEncontrados("Candidato não encontrado"));
        if (c.getCaminhoCurriculo() == null) {
            return ResponseEntity.noContent().build();
        }
        return converterCurriculo.visualizarCurriculo(c.getCaminhoCurriculo());
    }

    @PatchMapping("/admin/{ra}/desativar")
    @Operation(summary = "Inativar candidato", description = "Desativa o acesso de um aluno ao sistema via RA. Requer permissão de Admin.")
    @ApiResponse(responseCode = "200", description = "Candidato desativado com sucesso")
    public ResponseEntity<Void> desativarCandidato (@PathVariable String raAluno){
        candidatoService.desativarCandidato(raAluno);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/{ra}/reativar")
    @Operation(summary = "Reativar candidato", description = "Restaura o acesso de um aluno ao sistema via RA. Requer permissão de Admin.")
    @ApiResponse(responseCode = "200", description = "Candidato reativado com sucesso")
    public ResponseEntity<Void> reativarCandidato (@PathVariable String raAluno){
        candidatoService.reativarCandidato(raAluno);
        return ResponseEntity.ok().build();
    }
}