package dev.ale.sep_project.dtos.alumnos;

import java.util.List;

import dev.ale.sep_project.dtos.tutor.TutorRespuestaDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlumnoDetalleDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String domicilio;
    private Boolean discapacidad;
    private String detalleDiscap;
    private List<TutorRespuestaDTO> tutores;
    // private List<RegistroAlumnoDTO> registros;
}
