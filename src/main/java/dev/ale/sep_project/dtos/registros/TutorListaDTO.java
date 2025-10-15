package dev.ale.sep_project.dtos.registros;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TutorListaDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String domicilio;
}
