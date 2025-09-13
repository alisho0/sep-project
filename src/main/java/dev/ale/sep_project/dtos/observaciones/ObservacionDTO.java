package dev.ale.sep_project.dtos.observaciones;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObservacionDTO {
    private Long id;
    private String contenido;
    private String nombreUsuario;
    private LocalDate fecha;
}
