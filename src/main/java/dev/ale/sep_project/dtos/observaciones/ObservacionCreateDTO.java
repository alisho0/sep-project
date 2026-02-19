package dev.ale.sep_project.dtos.observaciones;

import java.time.LocalDate;

import dev.ale.sep_project.models.Motivo;
import lombok.Data;

@Data
public class ObservacionCreateDTO {
    private String contenido;
    private Motivo motivo;
    private String nombreUsuario;
    private LocalDate fecha;
    private Long idRegistro;
}
