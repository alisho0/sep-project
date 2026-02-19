package dev.ale.sep_project.dtos.observaciones;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObservacionDTO {
    private Long id;
    private Long idAlumno;
    private String contenido;
    private String nombreUsuario;
    private LocalDateTime fecha;
    private String alumno;
    private String motivo;
}
