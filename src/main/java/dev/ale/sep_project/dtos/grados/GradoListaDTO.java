package dev.ale.sep_project.dtos.grados;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GradoListaDTO {

    private Long id;
    private int grado;
    private String seccion;
    private String turno;
    private Long año;
    private Long cantAlumnos;
}
