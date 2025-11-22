package dev.ale.sep_project.repository;

import dev.ale.sep_project.models.Seccion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeccionRepository extends CrudRepository<Seccion, Long> {
    @Query("SELECT DISTINCT g.letra FROM seccion g ORDER BY g.letra DESC")
    List<String> findSeccionesDisponibles();

    Optional<Seccion> findByLetra(String letra);

    List<Seccion> findDistinctByCombinaciones_Grado_NroGrado(Long nroGrado);

}
