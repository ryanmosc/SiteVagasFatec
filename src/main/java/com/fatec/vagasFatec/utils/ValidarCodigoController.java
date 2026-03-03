package com.fatec.vagasFatec.utils;

import com.fatec.vagasFatec.Dto.CandidatoDTO.ValidarCandidatoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
