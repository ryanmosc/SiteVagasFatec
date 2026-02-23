package com.fatec.vagasFatec.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false)
    private String nome;

    @Column(unique = true)
    private String email;

    private String cnpj;

    // Uma empresa pode ter várias vagas
    @OneToMany(mappedBy = "empresa")
    private List<Vaga> vagas;
}