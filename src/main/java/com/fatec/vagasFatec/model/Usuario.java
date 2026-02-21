package com.fatec.vagasFatec.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Erro: O nome é orbigatorio")
    @Column(name = "nome")
    private String nome;

    @NotBlank(message = "Erro: O e-mail é orbigatorio")
    @Column(name = "email")
    @Email
    private String email;

    @NotBlank(message = "Erro: A senha é orbigatoria")
    @Column(name = "senha")
    private String senha;

    // Adicionar a Role somente após começar a utilizar o spring security
}
