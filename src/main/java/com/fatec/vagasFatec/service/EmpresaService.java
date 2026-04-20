package com.fatec.vagasFatec.service;

import com.fatec.vagasFatec.Dto.EmpresaDTO.EmpreRequestDTO;
import com.fatec.vagasFatec.Dto.EmpresaDTO.EmpresaAtualizarDTO;
import com.fatec.vagasFatec.Dto.EmpresaDTO.EmpresaResponseDTO;
import com.fatec.vagasFatec.exceptions.DadosInvalidosException;
import com.fatec.vagasFatec.exceptions.DadosNaoEncontrados;
import com.fatec.vagasFatec.exceptions.EntidadeJaExistenteException;
import com.fatec.vagasFatec.exceptions.RegraDeNegocioVioladaException;
import com.fatec.vagasFatec.model.Empresa;
import com.fatec.vagasFatec.model.Enum.Role;
import com.fatec.vagasFatec.model.Enum.StatusEmpresa;
import com.fatec.vagasFatec.repository.EmpresaRepository;
import com.fatec.vagasFatec.utils.EmailSender;
import com.fatec.vagasFatec.utils.SecurityUtil;
import com.fatec.vagasFatec.utils.VerificationCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender enviarEmail;
    private final VerificationCodeGenerator gerarCodigo;

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

    //Listar Todos os dados da empresa
    public EmpresaAtualizarDTO listarDadosEmpresa(){
        Long empresaLogadaId = SecurityUtil.getCurrentUserId();
        Empresa empresa = empresaRepository.findById(empresaLogadaId).orElseThrow(() -> new DadosNaoEncontrados("Erro: Empresa não encontrada"));
        return new EmpresaAtualizarDTO(
                empresa.getRazaoSocial(),
                empresa.getNomeFantasia(),
                empresa.getEmail(),
                empresa.getCnpj(),
                empresa.getTelefone()
        );
    }

    // Criar empresa
    public EmpresaResponseDTO criarEmpresa(EmpreRequestDTO dto) {

        if(empresaRepository.existsByEmail(dto.email())){
            throw  new EntidadeJaExistenteException("E-mail já cadastrado");
        }
        if (dto.senha().length() < 8){
            throw new RegraDeNegocioVioladaException("A senha deve conter ao menos 8 caracteres");
        }


        Empresa salva = new Empresa();
        salva.setRazaoSocial(dto.razaoSocial());
        salva.setNomeFantasia(dto.nomeFantasia());
        salva.setEmail(dto.email());
        salva.setCnpj(dto.cnpj());
        salva.setTelefone(dto.telefone());
        salva.setSenha(passwordEncoder.encode(dto.senha()));
        salva.setRole(Role.ROLE_EMPRESA);
        empresaRepository.save(salva);


        String codigo = gerarCodigo.gerarCodigoValidacao(salva.getEmail());
        enviarEmail.enviarEmail(salva.getEmail(), "OLá, segue abaixo o código para validação de seu registro",codigo);

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
    public EmpresaResponseDTO atualizarDadosEmpresa(EmpresaAtualizarDTO dados, Long id_empresa){
        Empresa empresa = empresaRepository.findById(id_empresa).orElseThrow(() -> new DadosNaoEncontrados("Empresa não encontrada"));
        if (empresa.getStatusEmpresa() != StatusEmpresa.ATIVO){
            throw new RegraDeNegocioVioladaException("Empresa não está ativa");
        }
        if (!empresa.getId().equals(id_empresa)){
            throw new DadosInvalidosException("Empresa não é dona do perfil");
        }

        if (dados.razaoSocial() != null){
            empresa.setRazaoSocial(dados.razaoSocial());
        }
        if (dados.nomeFantasia() != null){
            empresa.setNomeFantasia(dados.nomeFantasia());
        }
        if (dados.email() != null){
            empresa.setEmail(dados.email());
        }
        if (dados.cnpj() != null){
            empresa.setCnpj(dados.cnpj());
        }
        if (dados.telefone() != null){
            empresa.setTelefone(dados.telefone());
        }

        empresaRepository.save(empresa);
        return converter(empresa);
    }
}