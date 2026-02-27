package com.fatec.vagasFatec.infra.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;


@AllArgsConstructor
@Getter
@Setter
public class RestErrorMessage {

    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
    private Map<String, String> fieldErrors;

    public RestErrorMessage(LocalDateTime timestamp,
                            Integer status,
                            String error,
                            String message,
                            String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}


