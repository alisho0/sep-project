package dev.ale.sep_project.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.ale.sep_project.dtos.observaciones.ObservacionCreateDTO;
import dev.ale.sep_project.dtos.observaciones.ObservacionDTO;
import dev.ale.sep_project.exceptions.BusinessLogicException;
import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import dev.ale.sep_project.services.ObservacionService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/observacion")
@RequiredArgsConstructor
public class ObservacionController {

    private final ObservacionService observacionService;
    
    @PostMapping("/crear")
    public ResponseEntity<?> crearObservacion(@RequestBody ObservacionCreateDTO observacionDTO) {
        try {
            observacionService.nuevaObservacion(observacionDTO);
            return ResponseEntity.ok("Observación creada exitosamente");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BusinessLogicException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> detalleObservacion(@PathVariable Long id) {
        try {
            ObservacionDTO observacionDTO = observacionService.traerObservacion(id);
            return ResponseEntity.ok(observacionDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarObservacion(@PathVariable Long id) {
        try {
            observacionService.eliminarObservacion(id);
            return ResponseEntity.ok("Observación eliminada exitosamente");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BusinessLogicException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    }
