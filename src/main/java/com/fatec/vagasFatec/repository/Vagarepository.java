package com.fatec.vagasFatec.repository;

import com.fatec.vagasFatec.model.Enum.CursosEnum;
import com.fatec.vagasFatec.model.Enum.StatusVaga;
import com.fatec.vagasFatec.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Vagarepository extends JpaRepository <Vaga, Long>{
    List<Vaga> findByEmpresa_Id (Long idEmpresa);
    List<Vaga> findByStatusvaga (StatusVaga statusVaga);
    List<Vaga> findByStatusvagaAndCursoVaga(StatusVaga statusVaga, CursosEnum cursosEnum);
}
