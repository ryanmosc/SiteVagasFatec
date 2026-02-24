package com.fatec.vagasFatec.repository;

import com.fatec.vagasFatec.model.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    boolean existsByEmailCandidato(String email);
    boolean existsByRaAluno(String raAluno);
    Optional<Candidato> findByRaAluno(String raAluno);
}
