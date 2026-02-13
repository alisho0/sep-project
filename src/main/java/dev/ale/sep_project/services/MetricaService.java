package dev.ale.sep_project.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

import dev.ale.sep_project.dtos.metricas.MetricasCicloSeccionGradoDTO;
import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import dev.ale.sep_project.models.CicloGrado;
import dev.ale.sep_project.models.RegistroAlumno;
import dev.ale.sep_project.models.Usuario;
import dev.ale.sep_project.repository.*;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MetricaService {

    private final ObservacionRepository observacionRepository;
    private final AlumnoRepository alumnoRepository;
    private final CicloGradoRepository cicloGradoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DiscapacidadRepository discapacidadRepository;

    public Long observacionesRecientes(Long dias) {
        LocalDate fechaLimite = LocalDate.now().minusDays(dias);
        return observacionRepository.countByFechaAfter(fechaLimite);
    }

    public Long alumnosTotales() {
        return alumnoRepository.count();
    }

    public Long alumnosTotalesByLastCiclo() {
        int anioActual = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")).getYear();
        Long alumnosCount = cicloGradoRepository.countAlumnosByAnio(anioActual);
        return alumnosCount;
    }

    public MetricasCicloSeccionGradoDTO getMetricasPorGradoSeccion(Long id) {
        CicloGrado cicloGrado = cicloGradoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CicloGrado", id));
        Long cantDisc = cicloGrado.getRegistros().stream()
                .filter(r -> r.getAlumno().getDiscapacidad())
                .count();

        return MetricasCicloSeccionGradoDTO.builder()
                .cantAlumnos((long) cicloGrado.getRegistros().size())
                .cantDiscapacidad(cantDisc)
                .build();
    }

    public Long countGradosAsignadosInAnioActual(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        int anioActual = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")).getYear();
        return u.getMaestro()
                .getCiclos()
                .stream()
                .filter(c -> c.getAnio() == anioActual)
                .count();
    }

    public Long countAlumnosAsignadosInAnioActual(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        int anioActual = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")).getYear();

        return u.getMaestro()
                .getCiclos()
                .stream()
                .filter(c -> c.getAnio() == anioActual)
                .flatMap(cicloGrado -> cicloGrado.getRegistros().stream())
                .map(RegistroAlumno::getAlumno)
                .distinct()
                .count();
    }

    public Long countObservacionesRealizadasInAnioActual(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        int anioActual = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")).getYear();

        Long countObservaciones = observacionRepository.countObservacionesByUsuarioAndAnio(u.getId(), anioActual);
        return countObservaciones;
    }

    public Long countDiscapacidadesTotales() {
        return discapacidadRepository.count();
    }
}
