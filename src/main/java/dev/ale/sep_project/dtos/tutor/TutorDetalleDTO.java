package dev.ale.sep_project.dtos.tutor;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TutorDetalleDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String domicilio;
    private String telefono;
    private String telAux;
    private List<String> tutorDe;

    // Getters y Setters
}
