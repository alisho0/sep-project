package dev.ale.sep_project.dtos.tutor;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TutorRespuestaDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    List<String> tutorDe; // Nombres de los alumnos a cargo
}
