package dev.ale.sep_project.dtos.alumnos;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlumnoCreateDTO {
    private String nombre;
    private String apellido;
    private String dni;
    private String domicilio;
    private boolean discapacidad;
    private String detalleDiscap;
    // Agregar lista de IDs de tutores
    List<Long> tutoresIds;
}
