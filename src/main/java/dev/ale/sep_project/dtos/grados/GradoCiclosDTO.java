package dev.ale.sep_project.dtos.grados;

import dev.ale.sep_project.models.EstadoCiclo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GradoCiclosDTO {
    private Long id; 
    private int anio;
    private int cantAlumnos;
    private EstadoCiclo estado;
}
