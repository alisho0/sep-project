package dev.ale.sep_project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.CicloGrado;
import dev.ale.sep_project.models.Grado;

public interface CicloGradoRepository extends CrudRepository<CicloGrado, Long> {

    Optional<CicloGrado> findByAnio(int anioCicloGrado);

    @Query("SELECT DISTINCT c.anio FROM CicloGrado c ORDER BY c.anio DESC")
    List<Long> findAniosDisponibles();

    Optional<CicloGrado> findByAnioAndGrado(int anio, Grado grado);

    boolean existsByAnioAndGrado(int anio, Grado grado);

}
