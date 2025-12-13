package dev.ale.sep_project.dtos.maestros;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaestroAsignarCicloDTO {
    private Long idMaestro;
    private Long idCiclo;
}

