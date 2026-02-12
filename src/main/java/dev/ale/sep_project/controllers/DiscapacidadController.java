package dev.ale.sep_project.controllers;

import dev.ale.sep_project.dtos.discapacidades.DiscapacidadesListDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import dev.ale.sep_project.services.DiscapacidadService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/discapacidad")
@RequiredArgsConstructor
public class DiscapacidadController {

    private final DiscapacidadService discapacidadService;

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @PostMapping("/crear")
    public ResponseEntity<?> crearDiscapacidad(@RequestBody String nombre) {
        DiscapacidadesListDTO res = discapacidadService.crearDiscapacidad(nombre);
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @GetMapping("/listar")
    public ResponseEntity<?> listarDiscapacidades() {
        return ResponseEntity.ok(discapacidadService.listarDiscapacidades());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<?> eliminarDiscapacidad(@PathVariable Long id) {
        discapacidadService.borrarDiscapacidad(id);
        return ResponseEntity.ok("Discapacidad eliminada correctamente");
    }
    
}
