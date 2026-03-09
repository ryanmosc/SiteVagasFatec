package com.fatec.vagasFatec.Dto.EmpresaDTO;

public record EmpresaAtualizarDTO(
        String razaoSocial,
        String nomeFantasia,
        String email,
        String cnpj,
        String telefone
) {
}
