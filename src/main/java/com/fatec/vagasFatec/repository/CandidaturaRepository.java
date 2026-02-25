package com.fatec.vagasFatec.repository;

import com.fatec.vagasFatec.Dto.CandidaturaDTO.CandidaturaResponseDTO;
import com.fatec.vagasFatec.model.Candidatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidaturaRepository extends JpaRepository<Candidatura, Long> {

    List<Candidatura> findByCandidatoId(Long candidatoId);
    boolean existsByCandidatoIdAndVagaId(Long candidatoId, Long vagaId);
}
