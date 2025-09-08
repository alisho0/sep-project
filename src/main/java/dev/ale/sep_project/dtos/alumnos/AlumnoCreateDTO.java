package dev.ale.sep_project.dtos.alumnos;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlumnoCreateDTO {
    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;
    @NotNull(message = "El apellido no puede ser nulo")
    private String apellido;
    @NotNull(message = "El DNI no puede ser nulo")
    private String dni;
    @NotNull(message = "El domicilio no puede ser nulo")
    private String domicilio;
    private Boolean discapacidad;
    private String detalleDiscap;
    // Agregar lista de IDs de tutores, vendrían algo como esto: [1, 4, 3]
    List<Long> tutoresIds;
}
