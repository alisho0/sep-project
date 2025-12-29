package dev.ale.sep_project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.Maestro;

public interface MaestroRepository extends CrudRepository<Maestro, Long> {

    Optional<Maestro> findByDni(String dni);
    boolean existsByDni(String dni);
    List<Maestro> findByCiclos_Id(Long id);
}
