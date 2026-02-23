package com.fatec.vagasFatec.model;

import com.fatec.vagasFatec.model.Enum.StatusCandidato;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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


    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @Column(name = "ra_candidato", unique = true, nullable = false)
    private String raAluno;

    @Column(name = "email", unique = true, nullable = false)
    @Email
    private String emailCandidato;

    @Column(name = "senha_candidato", nullable = false)
    private String senha;

    @Column(name = "data_nascimento")
    @PastOrPresent
    private LocalDate dataNascimento;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "link_linkedin", columnDefinition = "TEXT")
    private String linkLinkedin;

    @Column(name = "link_github", columnDefinition = "TEXT")
    private  String linkGithub;

    @Column(name = "bio_candidato", columnDefinition = "TEXT")
    private String bioCandidato;

    @Column(name = "status_candidato")
    @Enumerated(EnumType.STRING)
    private StatusCandidato statusCandidato;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;


    @PrePersist
    public void prePersist(){
        dataNascimento = null;
        telefone = null;
        cidade = null;
        linkLinkedin = null;
        linkGithub = null;
        bioCandidato = null;
        statusCandidato = StatusCandidato.ATIVO;
        dataCadastro = LocalDateTime.now();

    }
}
