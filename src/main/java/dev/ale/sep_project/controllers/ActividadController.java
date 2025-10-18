package dev.ale.sep_project.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.ale.sep_project.models.Actividad;
import dev.ale.sep_project.services.ActividadService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/actividad")
@RequiredArgsConstructor
public class ActividadController {
    private final ActividadService actividadService;

    @GetMapping("/recientes")
    public ResponseEntity<List<Actividad>> getUltimas(@RequestParam(defaultValue = "10") int limite) {
        List<Actividad> actividades = actividadService.obtenerUltimas(limite);
        return ResponseEntity.ok(actividades);
    }
}
