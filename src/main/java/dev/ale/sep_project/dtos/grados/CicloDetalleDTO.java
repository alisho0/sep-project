package dev.ale.sep_project.dtos.grados;

import dev.ale.sep_project.dtos.alumnos.AlumnoResponseDTO;

import java.util.List;

public record CicloDetalleDTO(Long id,
                              Long cantAlumnos,
                              Long ciclo,
                              Long grado
//                              List<AlumnoResponseDTO> alumnos
) {
}
