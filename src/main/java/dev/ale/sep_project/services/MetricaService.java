package dev.ale.sep_project.services;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import dev.ale.sep_project.repository.AlumnoRepository;
import dev.ale.sep_project.repository.ObservacionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MetricaService {

    private final ObservacionRepository observacionRepository;
    private final AlumnoRepository alumnoRepository;

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
}
