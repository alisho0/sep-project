package dev.ale.sep_project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.Grado;

public interface GradoRepository extends CrudRepository<Grado, Long> {
    @Query("SELECT DISTINCT g.nroGrado FROM grado g ORDER BY g.nroGrado DESC")
    List<Integer> findByNroGradoDesc();
    Optional<Grado> findByNroGrado(int nroGrado);
}
