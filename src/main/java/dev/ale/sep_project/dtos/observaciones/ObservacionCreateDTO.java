package dev.ale.sep_project.dtos.observaciones;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ObservacionCreateDTO {
    private String contenido;
    private String nombreUsuario;
    private LocalDate fecha;
    private Long idRegistro;
}
