package dev.ale.sep_project.dtos.alumnos;

import lombok.Builder;

@Builder
public record AlumnoInscriptoDTO(Long id, String nombre, String dni, Long idRegistro) {
}
