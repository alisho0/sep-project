package dev.ale.sep_project.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.ale.sep_project.services.GradoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequiredArgsConstructor
@RequestMapping("/grado")
public class GradoController {

    private final GradoService gradoService;
    
    @GetMapping("/listar")
    public ResponseEntity<?> listarGrados() {
        return ResponseEntity.ok(gradoService.listarGrados());
    }
    
    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> gradoDetalle(@PathVariable Long id) {
        return ResponseEntity.ok(gradoService.detalleGrado(id));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<?> listarDisponibles() {
        return ResponseEntity.ok(gradoService.getGrados());
    }

    @GetMapping("/secciones")
    public ResponseEntity<?> listarSecciones() {
        return ResponseEntity.ok(gradoService.getSecciones());
    }
    
}
