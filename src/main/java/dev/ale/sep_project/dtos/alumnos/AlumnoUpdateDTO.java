package dev.ale.sep_project.dtos.alumnos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlumnoUpdateDTO {
    private String nombre;
    private String apellido;
    private Boolean discapacidad;
    private String detalleDiscap;
    private String domicilio;
    private String dni;

    // Getters and Setters
}
