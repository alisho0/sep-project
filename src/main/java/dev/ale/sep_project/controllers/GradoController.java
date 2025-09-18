package dev.ale.sep_project.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.ale.sep_project.services.GradoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequiredArgsConstructor
@RequestMapping("/grado")
public class GradoController {

    private final GradoService gradoService;
    
    @GetMapping("/listar")
    public ResponseEntity<?> listarGrados() {
        try {
            return ResponseEntity.ok(gradoService.listarGrados());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al listar grados");
        }
    }
    
    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> gradoDetalle(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(gradoService.detalleGrado(id));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al listar los ciclos");
        }
    }
    
}
