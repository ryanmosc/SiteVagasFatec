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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "razao_social")
    private String razaoSocial;

    @NotBlank
    @Column(name = "nome_fantasia")
    private String nomeFantasia;

    @NotBlank
    @Email
    @Column(name = "email_empresa", unique = true)
    private String email;

    @Column(name = "cnpj", unique = true)
    @CNPJ(message = "CNPJ INVALIDO")
    private String cnpj;

    @NotNull
    @Column(name = "telefone")
    private String telefone;

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