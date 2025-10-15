package dev.ale.sep_project.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.ale.sep_project.services.MetricaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
    
}
