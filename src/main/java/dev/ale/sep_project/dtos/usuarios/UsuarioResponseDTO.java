package dev.ale.sep_project.dtos.usuarios;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResponseDTO {
    private Long id;
    private String nombreCompleto;
    private String rol;
    private String username;
}
