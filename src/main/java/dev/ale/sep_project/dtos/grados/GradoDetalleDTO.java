package dev.ale.sep_project.dtos.grados;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GradoDetalleDTO {
    private Long id;
    private Long nro;
    private Long inscriptosActuales;
    private List<SeccionCiclo> seccionCiclos;
}
