package dev.ale.sep_project.repository;

import java.util.List;
import java.util.Optional;

import dev.ale.sep_project.models.EstadoCiclo;
import dev.ale.sep_project.models.GradoSeccionTurno;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.CicloGrado;
import dev.ale.sep_project.models.Grado;
import org.springframework.data.repository.query.Param;

public interface CicloGradoRepository extends CrudRepository<CicloGrado, Long> {

    Optional<CicloGrado> findByAnio(int anioCicloGrado);

    @Query("SELECT DISTINCT c.anio FROM CicloGrado c ORDER BY c.anio DESC")
    List<Long> findAniosDisponibles();

    Optional<CicloGrado> findByAnioAndGradoSeccionTurno(int anio, GradoSeccionTurno grado);

    boolean existsByAnioAndGradoSeccionTurno(int anio, GradoSeccionTurno grado);

    List<CicloGrado> findByGradoSeccionTurno_Grado_NroGrado(int nroGrado);

    List<CicloGrado> findByMaestros_Usuario_Id(Long usuarioId);

    List<CicloGrado> findAllByEstado(EstadoCiclo estado);

    @Query("SELECT COUNT(r.alumno) FROM CicloGrado cg JOIN cg.registros r WHERE cg.anio = :anio")
    Long countAlumnosByAnio(@Param("anio") int anio);
}
