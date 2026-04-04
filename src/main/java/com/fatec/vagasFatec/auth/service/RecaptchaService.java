package com.fatec.vagasFatec.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RecaptchaService {

    @Value("${recaptcha.secret}")
    private String secret;
    public boolean validar(String token) {

        String url = "https://www.google.com/recaptcha/api/siteverify";

        RestTemplate restTemplate = new RestTemplate();

        String params = "?secret=" + secret + "&response=" + token;

        Map response = restTemplate.postForObject(url + params, null, Map.class);

        return (Boolean) response.get("success");
    }
}
