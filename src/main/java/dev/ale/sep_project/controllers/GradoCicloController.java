package dev.ale.sep_project.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.ale.sep_project.dtos.grados.CicloCreateDTO;
import dev.ale.sep_project.services.CicloGradoService;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/ciclo")
public class GradoCicloController {

    private final CicloGradoService cicloGradoService;

    @PostMapping("/crearCiclo")
    public ResponseEntity<?> nuevoCiclo(@RequestBody CicloCreateDTO cicloDto) {
        try {
            cicloGradoService.crearCiclo(cicloDto);
            return ResponseEntity.ok("Ciclo creado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear un nuevo ciclo");
        }
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> detalleCiclo(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(cicloGradoService.listarCiclosGrado(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hubo un problema al traer el ciclo");
        }
    }

    @GetMapping("/detalle_seccion/{id}")
    public ResponseEntity<?> detalleSeccion(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(cicloGradoService.detalleSeccion(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hubo un problema al traer el ciclo");
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarCiclos() {
        try {
            return ResponseEntity.ok(cicloGradoService.getCiclosDisponibles());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hubo un problema al listar los grados");
        }
    }
    
    @GetMapping("/disponibles")
    public ResponseEntity<?> listarDisponibles() {
        try {
            return ResponseEntity.ok(cicloGradoService.getCiclosGradoDisponible());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hubo un problema al listar los ciclos disponibles");
        }
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> borrarGrado(@PathVariable Long id) {
        try {
            cicloGradoService.eliminarCicloGrado(id);
            return ResponseEntity.ok("Ciclo grado eliminado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo eliminar el ciclo con el id " + id );
        }
    }
}
