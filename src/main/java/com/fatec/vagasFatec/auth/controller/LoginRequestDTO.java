package com.fatec.vagasFatec.auth.controller;

public record LoginRequestDTO(
        String email,
        String senha
) {
}
