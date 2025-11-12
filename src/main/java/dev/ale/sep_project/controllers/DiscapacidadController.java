package dev.ale.sep_project.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.ale.sep_project.services.DiscapacidadService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/discapacidad")
@RequiredArgsConstructor
public class DiscapacidadController {

    private final DiscapacidadService discapacidadService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearDiscapacidad(@RequestBody String nombre) {
        try {
            discapacidadService.crearDiscapacidad(nombre);
            return ResponseEntity.ok("Discapacidad creada correctamente");
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
    
}
