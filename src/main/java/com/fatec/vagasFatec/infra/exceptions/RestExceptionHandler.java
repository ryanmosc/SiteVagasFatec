package com.fatec.vagasFatec.infra.exceptions;

import com.fatec.vagasFatec.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private RestErrorMessage buildError(
            HttpStatus status,
            String message,
            WebRequest request
    ) {
        return new RestErrorMessage(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getDescription(false).replace("uri=", "")
        );
    }

    // 400 - Dados inválidos
    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<RestErrorMessage> handleDadosInvalidos(
            DadosInvalidosException ex,
            WebRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request));
    }

    // 404 - Não encontrado
    @ExceptionHandler(DadosNaoEncontrados.class)
    public ResponseEntity<RestErrorMessage> handleNaoEncontrado(
            DadosNaoEncontrados ex,
            WebRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }

    // 403 - Operação não permitida
    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    public ResponseEntity<RestErrorMessage> handleOperacaoNaoPermitida(
            OperacaoNaoPermitidaException ex,
            WebRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(buildError(HttpStatus.FORBIDDEN, ex.getMessage(), request));
    }

    // 409 - Entidade já existe
    @ExceptionHandler(EntidadeJaExistenteException.class)
    public ResponseEntity<RestErrorMessage> handleConflito(
            EntidadeJaExistenteException ex,
            WebRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getMessage(), request));
    }

    // 422 - Regra de negócio
    @ExceptionHandler(RegraDeNegocioVioladaException.class)
    public ResponseEntity<RestErrorMessage> handleRegraNegocio(
            RegraDeNegocioVioladaException ex,
            WebRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(buildError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), request));
    }

    // Validação @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        RestErrorMessage errorResponse = new RestErrorMessage(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Erro de validação",
                request.getDescription(false).replace("uri=", "")
        );

        errorResponse.setFieldErrors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Fallback para erro inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorMessage> handleGeneric(
            Exception ex,
            WebRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Erro interno no servidor",
                        request
                ));
    }

}
