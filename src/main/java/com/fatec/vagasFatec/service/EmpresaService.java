package com.fatec.vagasFatec.service;

import com.fatec.vagasFatec.Dto.EmpresaDTO.EmpresaResponseDTO;
import com.fatec.vagasFatec.model.Empresa;
import com.fatec.vagasFatec.model.Enum.StatusEmpresa;
import com.fatec.vagasFatec.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    // Metodo auxiliar de conversão
    private EmpresaResponseDTO converter(Empresa empresa) {
        return new EmpresaResponseDTO(
                empresa.getId(),
                empresa.getRazaoSocial(),
                empresa.getNomeFantasia(),
                empresa.getEmail(),
                empresa.getCnpj(),
                empresa.getTelefone(),
                empresa.getStatusEmpresa()

        );
    }


    // Metodo auxiliar para validar status
    public boolean validarStatusEmpresa(Long id_empresa){
        Empresa e = empresaRepository.findById(id_empresa).orElseThrow(() -> new RuntimeException("Erro empresa não encontrada"));
        if(e.getStatusEmpresa() != StatusEmpresa.ATIVO){
            return false;
        }
        return true;
    }

    // Criar empresa
    public EmpresaResponseDTO criarEmpresa(Empresa empresa) {
        Empresa salva = empresaRepository.save(empresa);
        return converter(salva);
    }

    // Listar todas
    public List<EmpresaResponseDTO> listarEmpresas() {
        return empresaRepository.findAll()
                .stream()
                .map(this::converter)
                .toList();
    }
}