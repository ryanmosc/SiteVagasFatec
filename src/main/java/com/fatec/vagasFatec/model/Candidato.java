package com.fatec.vagasFatec.model;

import com.fatec.vagasFatec.model.Enum.StatusCandidato;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "candidatos")
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Erro: O nome completo é obrigatório")
    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @NotBlank(message = "Erro: O RA do aluno é obrigatório")
    @Column(name = "ra_candidato", unique = true, nullable = false)
    private String raAluno;

    @NotBlank(message = "Erro: O e-mail é obrigatório")
    @Email(message = "Erro: O formato do e-mail é inválido")
    @Column(name = "email", unique = true, nullable = false)
    private String emailCandidato;

    @NotBlank(message = "Erro: A senha é obrigatória")
    @Column(name = "senha_candidato", nullable = false)
    private String senha;

    @NotNull(message = "Erro: A data de nascimento é obrigatória")
    @PastOrPresent(message = "Erro: A data de nascimento não pode ser uma data futura")
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @NotBlank(message = "Erro: O telefone de contato é obrigatório")
    @Column(name = "telefone")
    private String telefone;

    @NotBlank(message = "Erro: A cidade é obrigatória")
    @Column(name = "cidade")
    private String cidade;

    @NotBlank(message = "Erro: Informe o link do seu LinkedIn")
    @Column(name = "link_linkedin", columnDefinition = "TEXT")
    private String linkLinkedin;

    @NotBlank(message = "Erro: Informe o link do seu GitHub")
    @Column(name = "link_github", columnDefinition = "TEXT")
    private String linkGithub;

    @NotBlank(message = "Erro: Escreva uma breve bio sobre você")
    @Column(name = "bio_candidato", columnDefinition = "TEXT")
    private String bioCandidato;

    @NotNull(message = "Erro: O status do candidato é obrigatório")
    @Column(name = "status_candidato")
    @Enumerated(EnumType.STRING)
    private StatusCandidato statusCandidato;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @PrePersist
    public void prePersist(){
        statusCandidato = StatusCandidato.ATIVO;
        dataCadastro = LocalDateTime.now();
    }
}