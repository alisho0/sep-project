package dev.ale.sep_project.dtos.registros;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.ale.sep_project.dtos.observaciones.ObservacionDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistroRespuestaDTO {
    
    private Long id;
    private int nroGrado;
    private String seccion;
    private String turno;
    private int anioCiclo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fechaInicio;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fechaFin;
    private List<ObservacionDTO> observaciones; // Lista de observaciones juntas
    
}
