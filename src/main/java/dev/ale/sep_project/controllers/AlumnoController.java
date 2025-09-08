package dev.ale.sep_project.controllers;

import org.springframework.web.bind.annotation.RestController;

import dev.ale.sep_project.dtos.alumnos.AlumnoCreateDTO;
import dev.ale.sep_project.dtos.alumnos.AlumnoResponseDTO;
import dev.ale.sep_project.models.Alumno;
import dev.ale.sep_project.services.AlumnoService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RequiredArgsConstructor
@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

    private final AlumnoService alumnoService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearAlumno(@RequestBody AlumnoCreateDTO alumno) {
        try {
            alumnoService.crearAlumno(alumno);
            return ResponseEntity.ok("Alumno creado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear alumno" + e.getMessage());
        }
    }
    
    @GetMapping("/listar")
    public ResponseEntity<?> listarAlumnos() {
        try {
            List<AlumnoResponseDTO> alumnos = alumnoService.obtenerAlumnos();
            return ResponseEntity.ok(alumnos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al listar alumnos: " + e.getMessage());
        }
    }
    
}
