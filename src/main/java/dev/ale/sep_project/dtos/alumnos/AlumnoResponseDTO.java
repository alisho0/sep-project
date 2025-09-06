package dev.ale.sep_project.dtos.alumnos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlumnoResponseDTO {
    private String nombre;
    private String apellido;
    private String dni;
    private int ultGrado;
    private String seccionGrado;
    private String turno;
}
