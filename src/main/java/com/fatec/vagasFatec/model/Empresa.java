package com.fatec.vagasFatec.model;

import com.fatec.vagasFatec.model.Enum.StatusEmpresa;
import com.fatec.vagasFatec.model.Enum.StatusVaga;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "empresas")
public class Empresa {

    //Dados cadastrais das empresas que irão participar da paltaforma

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Erro: Razão social é obrigatorio")
    @Column(name = "razao_social")
    private String razaoSocial;

    @NotBlank(message = "Erro: Nome fantasia é orbigatorio")
    @Column(name = "nome_fantasia")
    private String nomeFantasia;

    @NotBlank(message = "Erro: E-mail é orbigatorio")
    @Email
    @Column(name = "email_empresa", unique = true)
    private String email;

    @Column(name = "cnpj", unique = true)
    @CNPJ(message = "CNPJ INVALIDO")
    private String cnpj;

    @NotBlank(message = "Erro: telefone é obrigatorio")
    @Column(name = "telefone")
    private String telefone;

    @NotNull(message = "Erro: Status é obrigatorio")
    @Column(name = "status_empresa")
    @Enumerated(EnumType.STRING)
    private StatusEmpresa statusEmpresa;

    // Uma empresa pode ter várias vagas
    @OneToMany(mappedBy = "empresa")
    private List<Vaga> vagas;

    @PrePersist
    public void prePersist(){
       statusEmpresa = StatusEmpresa.ATIVO;
    }
}