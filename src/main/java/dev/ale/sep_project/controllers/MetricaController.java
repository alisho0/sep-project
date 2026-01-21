package dev.ale.sep_project.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.ale.sep_project.services.MetricaService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/metrica")
@RequiredArgsConstructor
public class MetricaController {

    private final MetricaService metricaService;

    @GetMapping("/observacionesRecientes")
    public Long getObservacionesRecientes(@RequestParam(defaultValue = "7") Long dias) {
        return metricaService.observacionesRecientes(dias);
    }
    @GetMapping("/alumnosTotales")
    public Long getAlumnosTotales() {
        return metricaService.alumnosTotales();
    }

    @GetMapping("/cicloDetalle/{id}")
    public ResponseEntity<?> metricasCSG(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(metricaService.getMetricasPorGradoSeccion(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/grados-asignados/{id}")
    public ResponseEntity<?> mostrarGradosAsignadosPorAnio(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(metricaService.countGradosAsignados(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/alumnos-asignados/{id}")
    public ResponseEntity<?> mostrarAlumnosAsignadosPorAnio(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(metricaService.countAlumnosAsignados(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
