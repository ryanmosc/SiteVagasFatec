package com.fatec.vagasFatec.repository;

import com.fatec.vagasFatec.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
