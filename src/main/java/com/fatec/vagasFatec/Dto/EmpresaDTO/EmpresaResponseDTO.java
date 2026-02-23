package com.fatec.vagasFatec.Dto.EmpresaDTO;


import com.fatec.vagasFatec.model.Enum.StatusEmpresa;

public record EmpresaResponseDTO(
        Long id,
        String razaoSocial,
        String nomeFantasia,
        String email,
        String cnpj,
        String telefone,
        StatusEmpresa statusEmpresa

) {}
