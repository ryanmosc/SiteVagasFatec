package com.fatec.vagasFatec.repository;

import com.fatec.vagasFatec.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Vagarepository extends JpaRepository <Vaga, Long>{
}
