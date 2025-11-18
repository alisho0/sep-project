package dev.ale.sep_project.repository;

import dev.ale.sep_project.models.Grado;
import dev.ale.sep_project.models.GradoSeccionTurno;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GradoSeccionTurnoRepository extends CrudRepository<GradoSeccionTurno, Long> {

    Optional<GradoSeccionTurno> findByGrado_NroGradoAndSeccion_LetraAndTurno_NombreTurno(int nroGrado, String seccionGrado, String turnoGrado);

    boolean existsByGrado_NroGradoAndSeccion_LetraAndTurno_NombreTurno(int nroGrado, String seccionGrado, String turnoGrado);
}
