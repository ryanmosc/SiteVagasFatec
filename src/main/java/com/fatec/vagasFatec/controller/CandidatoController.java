package com.fatec.vagasFatec.controller;

import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoAtualizarPerfilDTo;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoCadastroDTO;
import com.fatec.vagasFatec.Dto.CandidatoDTO.CandidatoResponseDTO;
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
public class CandidatoController {

    private final CandidatoService candidatoService;
    private final ConverterCurriculo converterCurriculo;
    private final CandidatoRepository candidatoRepository;
    private final EmpresaRepository empresaRepository;
    private final CandidaturaRepository candidaturaRepository;

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
//aaaaa
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


    //Enviar curriculo, opção disponivel somente apos o cadastro
    @PatchMapping(value = "/perfil/curriculo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> atualizarCurriculo(@RequestParam("curriculo") MultipartFile curriculo) {
        converterCurriculo.salvarCurriculo(curriculo);
        return ResponseEntity.noContent().build();
    }

    //Somente o candidato vizualiza
    @GetMapping("/perfil/curriculo/visualizar")
    public ResponseEntity<Resource> visualizarMeuCurriculo() throws MalformedURLException {
        Long id = SecurityUtil.getCurrentUserId();
        Candidato c = candidatoRepository.findById(id)
                .orElseThrow(() -> new DadosNaoEncontrados("Candidato não encontrado"));

        if (c.getCaminhoCurriculo() == null) {
            return ResponseEntity.noContent().build(); // ou 404, você decide
        }

        return converterCurriculo.visualizarCurriculo(c.getCaminhoCurriculo());
    }

    //Empresa vizualisa na candidatura
    @GetMapping("/perfil/curriculo/visualizar/{id_candidatura}/candidatura")
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
