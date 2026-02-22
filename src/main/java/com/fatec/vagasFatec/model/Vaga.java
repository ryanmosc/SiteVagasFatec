package com.fatec.vagasFatec.model;

import com.fatec.vagasFatec.model.Enum.StatusVaga;
import com.fatec.vagasFatec.model.Enum.TipoVagaEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private  Long id;

    @Column(name = "id_empresa")
    private Long idEmpresa;

    @NotBlank(message = "Erro: É obrgatorio um titulo para a vaga")
    @Column(name = "tituloVaga")
    private String tituloVaga;

    @NotBlank(message = "Erro: É obrgatorio uma descrição para a vaga")
    @Column(columnDefinition = "TEXT")
    private String descricaoVaga;

    @NotBlank
    @Column(name = "cursoVaga")
    private String cursoVaga;

    @NotNull
    @Column(name = "tipoVaga")
    @Enumerated(EnumType.STRING)
    private TipoVagaEnum tipoVagaEnum;

    @Column(name = "empresa_nome")
    private String nomeEmpresa;

    @Column(name = "cidade")
    private String cidadeVaga;

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
