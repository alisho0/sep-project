package dev.ale.sep_project.dtos.alumnos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AlumnoUpdateDTO {
    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;

    @NotNull(message = "El apellido no puede ser nulo")
    private String apellido;

    @NotNull(message = "El domicilio no puede ser nulo")
    private String domicilio;

    @NotNull(message = "El dni no puede ser nulo")
    private String dni;

    private Boolean discapacidad;
    private List<Long> discapacidadesSeleccionadas;
    private String detalleDiscap;


    // Getters and Setters
}
