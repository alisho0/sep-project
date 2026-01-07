package dev.ale.sep_project.dtos.usuarios;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CambioPasswordDTO {
    private String contrasenia;
    private String nuevaContrasenia;
    private String confirmarContrasenia;
}
