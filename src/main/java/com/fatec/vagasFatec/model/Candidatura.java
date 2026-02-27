package com.fatec.vagasFatec.model;

import com.fatec.vagasFatec.model.Enum.StatusCandidatura;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Erro: O candidato é obrigatório para realizar a inscrição")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidato_id", nullable = false)
    private Candidato candidato;

    @NotNull(message = "Erro: A vaga é obrigatória para realizar a inscrição")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @NotNull(message = "Erro: O status da candidatura deve ser informado")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCandidatura status;

    @Column(nullable = false)
    private LocalDateTime dataInscricao;

    //Campo opcional, empres ausa se quiser
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