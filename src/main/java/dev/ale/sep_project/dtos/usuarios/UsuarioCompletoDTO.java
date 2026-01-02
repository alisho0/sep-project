package dev.ale.sep_project.dtos.usuarios;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UsuarioCompletoDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String username;
    private String dni;
    private String domicilio;
    private String rol;
}
