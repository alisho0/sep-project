package dev.ale.sep_project.dtos.registros;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistroCreateDTO {
    private Long idAlumno;
    private Long idCicloGrado;
}
