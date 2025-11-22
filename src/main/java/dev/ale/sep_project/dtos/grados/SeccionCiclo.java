package dev.ale.sep_project.dtos.grados;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SeccionCiclo {
    private String seccion;
    private List<GradoCiclosDTO> gradoCiclos;
}
