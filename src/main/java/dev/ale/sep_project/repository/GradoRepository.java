package dev.ale.sep_project.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.Grado;

public interface GradoRepository extends CrudRepository<Grado, Long> {

    Optional<Grado> findByNroGradoAndSeccionAndTurno(int nroGrado, String seccionGrado, String turnoGrado);

}
