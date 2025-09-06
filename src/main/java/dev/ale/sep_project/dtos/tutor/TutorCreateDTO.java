package dev.ale.sep_project.dtos.tutor;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TutorCreateDTO {
    @NotNull
    private String nombre;
    @NotNull
    private String apellido;
    @NotNull
    private String dni;
    @NotNull
    private String domicilio;
    
    private String telefono;
    private String telAux;
}
