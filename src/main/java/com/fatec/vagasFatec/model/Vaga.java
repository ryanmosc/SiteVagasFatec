package com.fatec.vagasFatec.model;

import com.fatec.vagasFatec.model.Enum.CursosEnum;
import com.fatec.vagasFatec.model.Enum.ModalidadeVagaEnum;
import com.fatec.vagasFatec.model.Enum.StatusVaga;
import com.fatec.vagasFatec.model.Enum.TipoVagaEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "vagas")
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @NotBlank(message = "Erro: É obrigatório um título para a vaga")
    @Column(name = "tituloVaga")
    private String tituloVaga;

    @NotBlank(message = "Erro: É obrigatório uma descrição para a vaga")
    @Column(columnDefinition = "TEXT")
    private String descricaoVaga;

    @NotNull(message = "Erro: O curso da vaga deve ser selecionado")
    @Enumerated(EnumType.STRING)
    @Column(name = "cursoVaga")
    private CursosEnum cursoVaga;

    @NotNull(message = "Erro: O tipo da vaga (Estágio/CLT/etc) é obrigatório")
    @Column(name = "tipoVaga")
    @Enumerated(EnumType.STRING)
    private TipoVagaEnum tipoVagaEnum;

    @NotNull(message = "Erro: A modalidade (Presencial/Remoto) é obrigatória")
    @Column(name = "modalidade_vaga")
    @Enumerated(EnumType.STRING)
    private ModalidadeVagaEnum modalidadeVaga;

    @NotBlank(message = "Erro: A cidade da vaga é obrigatória")
    @Column(name = "cidade")
    private String cidadeVaga;

    @NotNull(message = "Erro: O valor da bolsa auxílio deve ser informado")
    @PositiveOrZero(message = "Erro: O valor da bolsa não pode ser negativo")
    @Column(name = "bolsa")
    private Double bolsaAuxilio;

    @Column(name = "dataPublicacaoVaga")
    private LocalDateTime dataPublicacaoVaga;

    @Column(name = "data_encerramento")
    private LocalDateTime dataEncerramento;


    @Column(name = "statusVaga")
    @Enumerated(EnumType.STRING)
    private StatusVaga statusvaga;

    @PrePersist
    public void prePersist(){
        dataPublicacaoVaga = LocalDateTime.now();
        statusvaga = StatusVaga.ABERTA;
    }
}