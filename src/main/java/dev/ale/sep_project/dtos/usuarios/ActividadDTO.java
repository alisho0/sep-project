package dev.ale.sep_project.dtos.usuarios;

import dev.ale.sep_project.models.TipoActividad;
import dev.ale.sep_project.models.TipoEntidad;

import java.time.LocalDateTime;

public record ActividadDTO(Long id,
                           String descripcion,
                           String tipo,
                           LocalDateTime fecha,
                           String usuario,
                           String tipoActividad,
                           String entidad,
                           Long entidadId) {
}
