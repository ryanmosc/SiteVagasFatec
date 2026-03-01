package com.fatec.vagasFatec.Dto.EmpresaDTO;

import com.fatec.vagasFatec.model.Enum.StatusEmpresa;

public record EmpreRequestDTO(
        String razaoSocial,
        String nomeFantasia,
        String email,
        String senha,
        String cnpj,
        String telefone
) {
}
