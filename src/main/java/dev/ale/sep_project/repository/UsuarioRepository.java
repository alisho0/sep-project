package dev.ale.sep_project.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
