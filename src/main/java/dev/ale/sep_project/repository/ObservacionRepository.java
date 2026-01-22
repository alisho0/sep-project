package dev.ale.sep_project.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import dev.ale.sep_project.models.Observacion;
import org.springframework.data.repository.query.Param;

public interface ObservacionRepository extends CrudRepository<Observacion, Long> {

    public Long countByFechaAfter(LocalDate fecha);

    @Query("SELECT COUNT(DISTINCT o) " +
            "FROM Observacion o " +
            "WHERE o.usuario.id = :usuarioId AND o.registroAlumno.cicloGrado.anio = :anioActual")
    Long countObservacionesByUsuarioAndAnio(@Param("usuarioId") Long usuarioId,
                                            @Param("anioActual") int anioActual);

}
