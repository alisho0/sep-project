package dev.ale.sep_project.controllers;

import dev.ale.sep_project.dtos.discapacidades.DiscapacidadesListDTO;
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

    @PostMapping("/crear")
    public ResponseEntity<?> crearDiscapacidad(@RequestBody String nombre) {
        try {
            DiscapacidadesListDTO res = discapacidadService.crearDiscapacidad(nombre);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear una discapacidad");
        }
    }
    
    @GetMapping("/listar")
    public ResponseEntity<?> listarDiscapacidades() {
        try {
            return ResponseEntity.ok(discapacidadService.listarDiscapacidades());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se encontraron discapacidades");
        }
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<?> eliminarDiscapacidad(@PathVariable Long id) {
        try {
            discapacidadService.borrarDiscapacidad(id);
            return ResponseEntity.ok("Discapacidad eliminada correctamente");
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar la discapacidad");
        }
    }
    
}
