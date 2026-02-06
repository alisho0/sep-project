package dev.ale.sep_project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.ale.sep_project.models.Alumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    public Optional<Alumno> findByDni(String dni);
    Long countByRegistroAlumno_CicloGrado_GradoSeccionTurno_Grado_NroGrado(Long nroGrado);
    Page<Alumno> findByNombreContainingIgnoreCaseOrDniContaining(String nombre, String dni, Pageable pageable);
}
