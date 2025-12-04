package dev.ale.sep_project.dtos.metricas;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MetricasCicloSeccionGradoDTO {
    private Long cantAlumnos;
    private Long cantDiscapacidad;
}
