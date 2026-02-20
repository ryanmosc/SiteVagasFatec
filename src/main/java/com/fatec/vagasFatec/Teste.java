package com.fatec.vagasFatec;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Getter
@Setter
@RestController
@RequestMapping("/teste")

public class Teste {
    @GetMapping
    public String teste(){
        return "Aplicação no ar";
    }
}
