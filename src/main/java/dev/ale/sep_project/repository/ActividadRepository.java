package dev.ale.sep_project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.Actividad;

public interface ActividadRepository extends CrudRepository<Actividad, Long> {
    Page<Actividad> findAllByOrderByFechaDesc(Pageable pageable);
}
