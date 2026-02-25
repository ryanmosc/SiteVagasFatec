package com.fatec.vagasFatec.repository;

import com.fatec.vagasFatec.Dto.CandidaturaDTO.CandidaturaResponseDTO;
import com.fatec.vagasFatec.model.Candidatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidaturaRepository extends JpaRepository<Candidatura, Long> {

    List<Candidatura> findByCandidatoId(Long candidatoId);
    boolean existsByCandidatoIdAndVagaId(Long candidatoId, Long vagaId);
    Optional<Candidatura> findByCandidato_IdAndVaga_Id(Long candidatoId, Long vagaId);
}
