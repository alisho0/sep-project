package dev.ale.sep_project.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @GetMapping("/listar")
    public ResponseEntity<?> listarGrados() {
        return ResponseEntity.ok(gradoService.listarGrados());
    }

    /*
    * Trae el array de Secciones con los ciclos para cada sección.
    * */
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> gradoDetalle(@PathVariable Long id) {
        return ResponseEntity.ok(gradoService.detalleGrado(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @GetMapping("/disponibles")
    public ResponseEntity<?> listarDisponibles() {
        return ResponseEntity.ok(gradoService.getGrados());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @GetMapping("/secciones")
    public ResponseEntity<?> listarSecciones() {
        return ResponseEntity.ok(gradoService.getSecciones());
    }
    
}
