package com.fatec.vagasFatec.controller;

import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoAtualizarPerfilDTo;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoCadastroDTO;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoMostrarDTO;
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
import com.fatec.vagasFatec.utils.ConverterFotoPerfil;
import com.fatec.vagasFatec.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/candidatos")
@RequiredArgsConstructor
@SecurityRequirement(name = SecurityConfig.SECURITY)
@Tag(name = "Candidato", description = "Endpoints para gestão de perfis, currículos e administração de candidatos")
public class CandidatoController {

    private final CandidatoService candidatoService;
    private final ConverterCurriculo converterCurriculo;
    private final ConverterFotoPerfil converterFotoPerfil;
    private final CandidatoRepository candidatoRepository;
    private final EmpresaRepository empresaRepository;
    private final CandidaturaRepository candidaturaRepository;

    @PostMapping
    @Operation(summary = "Realizar cadastro inicial", description = "Cria um novo registro de candidato/aluno no sistema. Acesso público.")
    @ApiResponse(responseCode = "201", description = "Candidato registrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou candidato já existente")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    public ResponseEntity<CandidatoResponseDTO> cadastroInicial(@RequestBody @Valid CandidatoCadastroDTO cadastroDTO) {
        CandidatoResponseDTO candidatoResponseDTO = candidatoService.criarCandidato(cadastroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(candidatoResponseDTO);
    }

    @GetMapping
    @Operation(summary = "Listar todos os candidatos", description = "Retorna uma lista de todos os candidatos cadastrados. Requer privilégios de Administrador.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado - Permissão insuficiente")
    public ResponseEntity<List<CandidatoResponseDTO>> listarTodosCandidatos() {
        List<CandidatoResponseDTO> responseEntities = candidatoService.listarTodosCandidatos();
        return ResponseEntity.ok().body(responseEntities);
    }

    @GetMapping("/perfil")
    @Operation(summary = "Consultar perfil próprio", description = "Recupera os dados detalhados do candidato autenticado na sessão atual.")
    @ApiResponse(responseCode = "200", description = "Dados do perfil retornados com sucesso")
    public ResponseEntity<CandidatoResponseDTO> listarDadosAlunoPorRa() {
        Long candidatoLogado = SecurityUtil.getCurrentUserId();
        CandidatoResponseDTO candidatoResponseDTO = candidatoService.listarDadosAlunoPorRa(candidatoLogado);
        return ResponseEntity.ok().body(candidatoResponseDTO);
    }

    @PatchMapping("/perfil")
    @Operation(summary = "Atualizar informações de perfil", description = "Atualiza campos específicos do perfil do candidato logado.")
    @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso")
    public ResponseEntity<CandidatoResponseDTO> atualizarDadosCandidato(@RequestBody @Valid CandidatoAtualizarPerfilDTo dto) {
        Long candidatoLogado = SecurityUtil.getCurrentUserId();
        CandidatoResponseDTO candidatoResponseDTO = candidatoService.atualizarDadosPerfil(dto, candidatoLogado);
        return ResponseEntity.ok().body(candidatoResponseDTO);
    }


    @PatchMapping(value = "/perfil/curriculo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Fazer upload de currículo", description = "Envia ou substitui o arquivo de currículo (PDF) do candidato autenticado. O arquivo é salvo no banco de dados.")
    @ApiResponse(responseCode = "204", description = "Arquivo processado e salvo com sucesso")
    @ApiResponse(responseCode = "400", description = "Arquivo inválido ou ausente")
    public ResponseEntity<Void> atualizarCurriculo(@RequestParam("curriculo") MultipartFile curriculo) {
        converterCurriculo.salvarCurriculo(curriculo);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/perfil/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Fazer upload de foto de perfil", description = "Envia ou substitui o arquivo de foto de perfil do candidato autenticado. O arquivo é salvo no banco de dados.")
    @ApiResponse(responseCode = "204", description = "Arquivo processado e salvo com sucesso")
    @ApiResponse(responseCode = "400", description = "Arquivo inválido ou ausente")
    public ResponseEntity<Void> atualizarFotoPerfil(@RequestParam ("fotoPerfil") MultipartFile fotoPerfil){
        converterFotoPerfil.salvarFotoPerfil(fotoPerfil);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/perfil/curriculo/visualizar")
    @Operation(summary = "Visualizar currículo pessoal", description = "Retorna o PDF do currículo do candidato autenticado diretamente do banco de dados.")
    @ApiResponse(responseCode = "200", description = "Arquivo carregado com sucesso")
    @ApiResponse(responseCode = "204", description = "Candidato não possui currículo cadastrado")
    public ResponseEntity<byte[]> visualizarMeuCurriculo() {
        Long id = SecurityUtil.getCurrentUserId();
        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new DadosNaoEncontrados("Candidato não encontrado"));

        return converterCurriculo.visualizarCurriculo(candidato);
    }


    @GetMapping("/perfil/foto/visualizar")
    @Operation(summary = "Visualizar foto pessoal", description = "Retorna a foto do candidato autenticado diretamente do banco de dados.")
    @ApiResponse(responseCode = "200", description = "Arquivo carregado com sucesso")
    @ApiResponse(responseCode = "204", description = "Candidato não possui foto cadastrada")
    public ResponseEntity<byte[]> visualizarMinhaFoto() {
        Long id = SecurityUtil.getCurrentUserId();
        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new DadosNaoEncontrados("Candidato não encontrado"));

        return converterFotoPerfil.visualizarFotoPerfil(candidato);
    }



    @GetMapping("/perfil/curriculo/visualizar/{idCandidatura}/candidatura")
    @Operation(
            summary = "Visualizar currículo de candidato (Empresa)",
            description = "Permite que a empresa visualize o currículo vinculado a uma candidatura específica de suas vagas."
    )
    @ApiResponse(responseCode = "200", description = "Visualização permitida e arquivo carregado")
    @ApiResponse(responseCode = "204", description = "Candidato não possui currículo cadastrado")
    @ApiResponse(responseCode = "403", description = "Empresa não possui permissão para visualizar este currículo")
    @ApiResponse(responseCode = "404", description = "Candidatura, empresa ou candidato não encontrado")
    public ResponseEntity<byte[]> visualizarCurriculoCandidato(@PathVariable Long idCandidatura) {
        Long empresaLogadaId = SecurityUtil.getCurrentUserId();

        Candidatura candidatura = candidaturaRepository.findById(idCandidatura)
                .orElseThrow(() -> new DadosNaoEncontrados("Candidatura não encontrada"));

        Empresa empresa = empresaRepository.findById(empresaLogadaId)
                .orElseThrow(() -> new DadosNaoEncontrados("Empresa não encontrada"));


        if (!empresa.getId().equals(candidatura.getVaga().getEmpresa().getId())) {
            throw new RegraDeNegocioVioladaException("Empresa não é dona desta vaga");
        }

        Candidato candidato = candidatoRepository.findById(candidatura.getCandidato().getId())
                .orElseThrow(() -> new DadosNaoEncontrados("Candidato não encontrado"));

        return converterCurriculo.visualizarCurriculo(candidato);
    }










    @GetMapping("/perfil/foto/visualizar/{idCandidatura}/candidatura")
    @Operation(
            summary = "Visualizar foto de candidato (Empresa)",
            description = "Permite que a empresa visualize a foto  vinculada a uma candidatura específica de suas vagas."
    )
    @ApiResponse(responseCode = "200", description = "Visualização permitida e arquivo carregado")
    @ApiResponse(responseCode = "204", description = "Candidato não possui foto cadastrada")
    @ApiResponse(responseCode = "403", description = "Empresa não possui permissão para visualizar esta foto")
    @ApiResponse(responseCode = "404", description = "Candidatura, empresa ou candidato não encontrado")
    public ResponseEntity<byte[]> visualiarFotoCandidato(@PathVariable Long idCandidatura) {
        Long empresaLogadaId = SecurityUtil.getCurrentUserId();

        Candidatura candidatura = candidaturaRepository.findById(idCandidatura)
                .orElseThrow(() -> new DadosNaoEncontrados("Candidatura não encontrada"));

        Empresa empresa = empresaRepository.findById(empresaLogadaId)
                .orElseThrow(() -> new DadosNaoEncontrados("Empresa não encontrada"));


        if (!empresa.getId().equals(candidatura.getVaga().getEmpresa().getId())) {
            throw new RegraDeNegocioVioladaException("Empresa não é dona desta vaga");
        }

        Candidato candidato = candidatoRepository.findById(candidatura.getCandidato().getId())
                .orElseThrow(() -> new DadosNaoEncontrados("Candidato não encontrado"));

        return converterFotoPerfil.visualizarFotoPerfil(candidato);
    }

    // -----------------------------------------------------------------
    // Endpoints administrativos
    // -----------------------------------------------------------------
    @PatchMapping("/admin/{ra}/desativar")
    @Operation(summary = "Inativar candidato", description = "Desativa o acesso de um aluno ao sistema via RA. Requer permissão de Admin.")
    @ApiResponse(responseCode = "200", description = "Candidato desativado com sucesso")
    public ResponseEntity<Void> desativarCandidato(@PathVariable("ra") String raAluno) {
        candidatoService.desativarCandidato(raAluno);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/{ra}/reativar")
    @Operation(summary = "Reativar candidato", description = "Restaura o acesso de um aluno ao sistema via RA. Requer permissão de Admin.")
    @ApiResponse(responseCode = "200", description = "Candidato reativado com sucesso")
    public ResponseEntity<Void> reativarCandidato(@PathVariable("ra") String raAluno) {
        candidatoService.reativarCandidato(raAluno);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/perfil/todos/{id}")
    public ResponseEntity<CandidatoMostrarDTO> mostrarDadosCandidato(@Valid @PathVariable Long id) {
        CandidatoMostrarDTO candidatoMostrarDTO = candidatoService.mostrarDadosCandidato(id);
        return ResponseEntity.ok(candidatoMostrarDTO);
    }


}