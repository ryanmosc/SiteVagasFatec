package com.fatec.vagasFatec.model;

import com.fatec.vagasFatec.model.Enum.TipoVagaEnum;
import jakarta.persistence.*;
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
@Table(name = "vaga")
public class Vaga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "tituloVaga")
    private String tituloVaga;

    @Column(name = "descricaoVaga")
    private String descricaoVaga;

    @Column(name = "cursoVaga")
    private String cursovaga;

    @Column(name = "tipoVaga")
    @Enumerated(EnumType.STRING)
    private TipoVagaEnum tipoVagaEnum;

    @Column(name = "dataPublicacaoVaga")
    private LocalDateTime dataPublicacaoVaga;

    @Column(name = "statusVaga")
    private Boolean statusvaga = Boolean.TRUE;


}
