package dev.ale.sep_project.dtos.usuarios;

import lombok.Data;

@Data
public class UsuarioEditarDTO {
    private String nombre;
    private String apellido;
    private String username;
    private String dni;
    private String domicilio;
    private String rol;
}
