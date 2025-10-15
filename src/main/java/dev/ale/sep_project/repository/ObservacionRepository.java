package dev.ale.sep_project.repository;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.Observacion;

public interface ObservacionRepository extends CrudRepository<Observacion, Long> {

    public Long countByFechaAfter(LocalDate fecha);
}
