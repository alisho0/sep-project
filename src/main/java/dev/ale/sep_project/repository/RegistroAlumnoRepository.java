package dev.ale.sep_project.repository;

import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.RegistroAlumno;

import java.util.List;
import java.util.Optional;

public interface RegistroAlumnoRepository extends CrudRepository<RegistroAlumno, Long> {
    List<RegistroAlumno> findByCicloGrado_Id(Long cicloId);
    boolean existsByAlumnoIdAndCicloGradoId(Long alumnoId, Long cicloGradoId);
    Optional<RegistroAlumno> findByAlumnoIdAndCicloGradoId(Long alumnoId, Long cicloGradoId);
}
