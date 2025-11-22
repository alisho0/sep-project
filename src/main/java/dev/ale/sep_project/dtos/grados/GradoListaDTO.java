package dev.ale.sep_project.dtos.grados;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GradoListaDTO {

    private Long id;
    private int grado;
    private List<String> secciones;
    private Long cantAlumnos;

    // Creo que no van pq es un lío para ahcerlo y mejor hacerlo en cada ciclo del grado individual
}
