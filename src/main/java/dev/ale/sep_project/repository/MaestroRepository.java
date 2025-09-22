package dev.ale.sep_project.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.Maestro;

public interface MaestroRepository extends CrudRepository<Maestro, Long> {

    Optional<Maestro> findByDni(String dni);

}
