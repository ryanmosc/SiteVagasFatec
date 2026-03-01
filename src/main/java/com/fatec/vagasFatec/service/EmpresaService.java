package com.fatec.vagasFatec.service;

import com.fatec.vagasFatec.Dto.EmpresaDTO.EmpreRequestDTO;
import com.fatec.vagasFatec.Dto.EmpresaDTO.EmpresaResponseDTO;
import com.fatec.vagasFatec.exceptions.DadosNaoEncontrados;
import com.fatec.vagasFatec.model.Empresa;
import com.fatec.vagasFatec.model.Enum.Role;
import com.fatec.vagasFatec.model.Enum.StatusEmpresa;
import com.fatec.vagasFatec.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;

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
        Empresa e = empresaRepository.findById(id_empresa).orElseThrow(() -> new DadosNaoEncontrados("Erro empresa não encontrada"));
        if(e.getStatusEmpresa() != StatusEmpresa.ATIVO){
            return false;
        }
        return true;
    }

    // Criar empresa
    public EmpresaResponseDTO criarEmpresa(EmpreRequestDTO dto) {
        Empresa salva = new Empresa();
        salva.setRazaoSocial(dto.razaoSocial());
        salva.setNomeFantasia(dto.nomeFantasia());
        salva.setEmail(dto.email());
        salva.setCnpj(dto.cnpj());
        salva.setTelefone(dto.telefone());
        salva.setSenha(passwordEncoder.encode(dto.senha()));
        salva.setRole(Role.ROLE_EMPRESA);
        empresaRepository.save(salva);
        return converter(salva);
    }

    // Listar todas
    public List<EmpresaResponseDTO> listarEmpresas() {
        return empresaRepository.findAll()
                .stream()
                .map(this::converter)
                .toList();
    }

    //Criar metodo de atualização de dados da empresa igual o do candidato
}