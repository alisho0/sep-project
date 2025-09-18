package dev.ale.sep_project.dtos.grados;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GradoCiclosDTO {
    private Long id; 
    private int año;
    private int cantAlumnos;
}
