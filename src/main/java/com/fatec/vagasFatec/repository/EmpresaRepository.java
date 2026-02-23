package com.fatec.vagasFatec.repository;

import com.fatec.vagasFatec.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
