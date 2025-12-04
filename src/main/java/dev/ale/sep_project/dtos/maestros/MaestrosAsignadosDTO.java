package dev.ale.sep_project.dtos.maestros;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaestrosAsignadosDTO {
    private Long id;
    private String usuario;
    private String correo;
}
