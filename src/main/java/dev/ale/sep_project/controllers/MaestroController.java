package dev.ale.sep_project.controllers;

import dev.ale.sep_project.dtos.maestros.MaestroResponseDTO;
import dev.ale.sep_project.dtos.maestros.MaestrosAsignadosDTO;
import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import dev.ale.sep_project.services.MaestroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/maestro")
@RequiredArgsConstructor
public class MaestroController {
    private final MaestroService maestroService;

    @GetMapping("/listarAsignados/{id}")
    public ResponseEntity<?> listarMaestrosAsignados(@PathVariable Long id) {
        try {
            List<MaestrosAsignadosDTO> asignados = maestroService.listarMaestrosAsignadosPorCSG(id);
            return ResponseEntity.ok(asignados);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/listarDisponibles")
    public ResponseEntity<?> listarMaestros() {
        try {
            List<MaestroResponseDTO> asignados = maestroService.listarMaestros();
            return ResponseEntity.ok(asignados);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
