package dev.ale.sep_project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.Grado;
import org.springframework.data.repository.query.Param;

public interface GradoRepository extends CrudRepository<Grado, Long> {
    @Query("SELECT DISTINCT g.nroGrado FROM grado g ORDER BY g.nroGrado DESC")
    List<Integer> findByNroGradoDesc();
    @Query("SELECT DISTINCT g.nroGrado FROM grado g ORDER BY g.nroGrado ASC")
    List<Integer> findByNroGradoAsc();
    Optional<Grado> findByNroGrado(int nroGrado);
}
