package dev.ale.sep_project.dtos.usuarios;

import java.time.LocalDateTime;

public record ActividadDTO(Long id, String descripcion, String tipo, LocalDateTime fecha, String usuario) {
}
