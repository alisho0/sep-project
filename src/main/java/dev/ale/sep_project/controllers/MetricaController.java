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

    @GetMapping("/discapacidades-totales")
    public ResponseEntity<Long> getDiscapacidadesTotales(){
        return ResponseEntity.ok(metricaService.countDiscapacidadesTotales());
    }

    @GetMapping("/cicloDetalle/{id}")
    public ResponseEntity<?> metricasCSG(@PathVariable Long id) {
        return ResponseEntity.ok(metricaService.getMetricasPorGradoSeccion(id));
    }

    @GetMapping("/grados-asignados/{id}")
    public ResponseEntity<?> mostrarGradosAsignadosPorAnio(@PathVariable Long id) {
        return ResponseEntity.ok(metricaService.countGradosAsignadosInAnioActual(id));
    }

    @GetMapping("/alumnos-asignados/{id}")
    public ResponseEntity<?> mostrarAlumnosAsignadosPorAnio(@PathVariable Long id) {
        return ResponseEntity.ok(metricaService.countAlumnosAsignadosInAnioActual(id));
    }

    @GetMapping("/observaciones-por-año/{id}")
    public ResponseEntity<?> mostrarObservacionesRealizadasPorAnio(@PathVariable Long id) {
        return ResponseEntity.ok(metricaService.countObservacionesRealizadasInAnioActual(id));
    }
}
