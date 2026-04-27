package com.fatec.vagasFatec.utils;

import com.fatec.vagasFatec.Dto.CandidatoDTO.ValidarCandidatoDTO;
import com.fatec.vagasFatec.Dto.EmpresaDTO.ValidarEmpresaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/candidatos/validar")
public class ValidarCodigoController {
    private final VerificationCodeGenerator verificationCodeGenerator;
    private final EmailSender enviarEmail;

    @PostMapping
    public ResponseEntity<Void> validarCodigo(@RequestBody ValidarCandidatoDTO c){
        verificationCodeGenerator.validarCodigo(c);
        return ResponseEntity.noContent().build();

    }

    @PostMapping("/empresa")
    public ResponseEntity<Void> validarCodigoEmpresa(@RequestBody ValidarEmpresaDTO dto){
        verificationCodeGenerator.validarCodigoEmpresa(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reenviar")
    public ResponseEntity<Void> reenviarCodigo(@RequestParam String email){
        String codigo = verificationCodeGenerator.gerarCodigoValidacao(email);
        enviarEmail.enviarEmail(email, "OLá, segue abaixo o código para validação de seu registro",codigo);
        return ResponseEntity.noContent().build();
    }
}
