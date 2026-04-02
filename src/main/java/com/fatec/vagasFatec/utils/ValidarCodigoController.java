package com.fatec.vagasFatec.utils;

import com.fatec.vagasFatec.Dto.CandidatoDTO.ValidarCandidatoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/candidatos/validar")
public class ValidarCodigoController {
    private final VerificationCodeGenerator verificationCodeGenerator;

    @PostMapping
    public ResponseEntity<Void> validarCodigo(@RequestBody ValidarCandidatoDTO c){
        verificationCodeGenerator.validarCodigo(c);
        return ResponseEntity.noContent().build();

    }

    @PostMapping("/reenviar")
    public ResponseEntity<Void> reenviarCodigo(@RequestParam String email){
        verificationCodeGenerator.gerarCodigoValidacao(email);
        return ResponseEntity.noContent().build();
    }
}
