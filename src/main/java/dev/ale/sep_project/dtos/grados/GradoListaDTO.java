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

    // Creo que no van pq es un lío para ahcerlo y mejor hacerlo en cada ciclo del grado individual
}
