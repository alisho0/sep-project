package dev.ale.sep_project.services;

import java.time.LocalDate;

import dev.ale.sep_project.dtos.metricas.MetricasCicloSeccionGradoDTO;
import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import dev.ale.sep_project.models.CicloGrado;
import dev.ale.sep_project.repository.CicloGradoRepository;
import org.springframework.stereotype.Service;

import dev.ale.sep_project.repository.AlumnoRepository;
import dev.ale.sep_project.repository.ObservacionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MetricaService {

    private final ObservacionRepository observacionRepository;
    private final AlumnoRepository alumnoRepository;
    public final CicloGradoRepository cicloGradoRepository;

    public Long observacionesRecientes(Long dias) {
        try {
            LocalDate fechaLimite = LocalDate.now().minusDays(dias);
            return observacionRepository.countByFechaAfter(fechaLimite);

        } catch (Exception e) {
            new Exception("No se pudieron obtener las observaciones recientes" + " - " + e.getMessage());
            return 0L;
        }
    }

    public Long alumnosTotales() {
        return alumnoRepository.count();
    }

    public MetricasCicloSeccionGradoDTO getMetricasPorGradoSeccion(Long id) {
        try {
            CicloGrado cicloGrado = cicloGradoRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("CicloGrado", id));
            System.out.println(cicloGrado.getId());
            Long cantDisc = cicloGrado.getRegistros().stream()
                    .filter(r -> r.getAlumno().getDiscapacidad())
                    .count();

            System.out.println(cantDisc);
            return MetricasCicloSeccionGradoDTO.builder()
                    .cantAlumnos((long) cicloGrado.getRegistros().size())
                    .cantDiscapacidad(cantDisc)
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
