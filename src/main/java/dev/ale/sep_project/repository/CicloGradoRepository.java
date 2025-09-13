package dev.ale.sep_project.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.CicloGrado;

public interface CicloGradoRepository extends CrudRepository<CicloGrado, Long> {

    Optional<CicloGrado> findByAnio(int anioCicloGrado);

}
