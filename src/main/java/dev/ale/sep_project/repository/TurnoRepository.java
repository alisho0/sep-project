package dev.ale.sep_project.repository;

import dev.ale.sep_project.models.Turno;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TurnoRepository extends CrudRepository<Turno, Long> {
    Optional<Turno> findByNombreTurno(String turno);
}
