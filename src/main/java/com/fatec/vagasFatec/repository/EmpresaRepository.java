package com.fatec.vagasFatec.repository;

import com.fatec.vagasFatec.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Optional<Empresa> findByEmail (String email);
}
