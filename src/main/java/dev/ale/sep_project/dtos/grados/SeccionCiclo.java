package dev.ale.sep_project.dtos.grados;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SeccionCiclo {
    private Long id;
    private String seccion;
    private String turno;
    private List<GradoCiclosDTO> gradoCiclos;
}
