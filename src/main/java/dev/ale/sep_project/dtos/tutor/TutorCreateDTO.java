package dev.ale.sep_project.dtos.tutor;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TutorCreateDTO {
    private String nombre;
    private String apellido;
    private String dni;
    private String domicilio;
    
    private String telefono;
    private String telAux;
}
