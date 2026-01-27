package dev.ale.sep_project.repository;

import java.util.List;
import java.util.Optional;

import dev.ale.sep_project.models.GradoSeccionTurno;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.CicloGrado;
import dev.ale.sep_project.models.Grado;

public interface CicloGradoRepository extends CrudRepository<CicloGrado, Long> {

    Optional<CicloGrado> findByAnio(int anioCicloGrado);

    @Query("SELECT DISTINCT c.anio FROM CicloGrado c ORDER BY c.anio DESC")
    List<Long> findAniosDisponibles();

    Optional<CicloGrado> findByAnioAndGradoSeccionTurno(int anio, GradoSeccionTurno grado);

    boolean existsByAnioAndGradoSeccionTurno(int anio, GradoSeccionTurno grado);

    List<CicloGrado> findByGradoSeccionTurno_Grado_NroGrado(int nroGrado);

    List<CicloGrado> findByMaestros_Usuario_Id(Long usuarioId);
}
