package dev.ale.sep_project.dtos.grados;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GradoDetalleDTO {
    private GradoListaDTO gradoListaDTO;
    private List<GradoCiclosDTO> gradoCiclos;
}
