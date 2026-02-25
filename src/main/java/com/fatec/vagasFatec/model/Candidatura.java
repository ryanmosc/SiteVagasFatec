package com.fatec.vagasFatec.model;

import com.fatec.vagasFatec.model.Enum.StatusCandidatura;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidaturas",
        uniqueConstraints = @UniqueConstraint(columnNames = {"candidato_id", "vaga_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Candidatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muitos candidatos podem se candidatar para uma vaga
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidato_id", nullable = false)
    private Candidato candidato;

    // Uma vaga pode ter várias candidaturas
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCandidatura status;

    @Column(nullable = false)
    private LocalDateTime dataInscricao;

    @Column(columnDefinition = "TEXT")
    private String observacaoEmpresa;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = StatusCandidatura.ENVIADO;
        }
        if (dataInscricao == null) {
            dataInscricao = LocalDateTime.now();
        }
    }
}