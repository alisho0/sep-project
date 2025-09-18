package dev.ale.sep_project.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.ale.sep_project.dtos.grados.CicloCreateDTO;
import dev.ale.sep_project.services.CicloGradoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


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
        
}
