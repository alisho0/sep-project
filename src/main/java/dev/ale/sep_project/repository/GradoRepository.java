package dev.ale.sep_project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.Grado;

public interface GradoRepository extends CrudRepository<Grado, Long> {

    Optional<Grado> findByNroGradoAndSeccionAndTurno(int nroGrado, String seccionGrado, String turnoGrado);

    boolean existsByNroGradoAndSeccionAndTurno(int nroGrado, String seccionGrado, String turnoGrado);

    @Query("SELECT DISTINCT g.nroGrado FROM Grado g ORDER BY g.nroGrado DESC")
    List<Integer> findGradoDisponibles();

    @Query("SELECT DISTINCT g.seccion FROM Grado g ORDER BY g.seccion DESC")
    List<String> findSeccionesDisponibles();
}
