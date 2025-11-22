package dev.ale.sep_project.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.Alumno;

public interface AlumnoRepository extends CrudRepository<Alumno, Long> {
    public Optional<Alumno> findByDni(String dni);
    Long countByRegistroAlumno_CicloGrado_GradoSeccionTurno_Grado_NroGrado(Long nroGrado);
}
