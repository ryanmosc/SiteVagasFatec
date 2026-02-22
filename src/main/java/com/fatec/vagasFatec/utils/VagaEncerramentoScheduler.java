package com.fatec.vagasFatec.utils;

import com.fatec.vagasFatec.model.Enum.StatusVaga;
import com.fatec.vagasFatec.model.Vaga;
import com.fatec.vagasFatec.repository.Vagarepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VagaEncerramentoScheduler {
    private final Vagarepository vagarepository;

    @Scheduled(fixedRate = 60000)
    public void encerrarVagasAutomaticamente(){
        List<Vaga> vagas = vagarepository.findByStatusvaga(StatusVaga.ABERTA);

        for(Vaga vaga: vagas){
            if (vaga.getDataEncerramento() != null && vaga.getDataEncerramento().isBefore(LocalDateTime.now())){
                vaga.setStatusvaga(StatusVaga.ENCERRADA);
                vagarepository.save(vaga);
            }
        }
    }
}
