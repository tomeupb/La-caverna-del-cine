package com.proyectoTfg.demo.repository;

import com.proyectoTfg.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository  extends JpaRepository<Usuario, Integer> {

    Usuario findUsuarioByEmail(String email);

    Optional<Usuario> findByEmail(String email);


}
