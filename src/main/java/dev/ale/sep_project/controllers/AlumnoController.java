package dev.ale.sep_project.controllers;

import org.springframework.web.bind.annotation.RestController;

import dev.ale.sep_project.dtos.alumnos.AlumnoCreateDTO;
import dev.ale.sep_project.models.Alumno;
import dev.ale.sep_project.services.AlumnoService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RequiredArgsConstructor
@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

    private final AlumnoService alumnoService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearAlumno(@RequestBody AlumnoCreateDTO alumno) {
        try {
            Alumno nuevoAlumno = alumnoService.crearAlumno(alumno);
            return ResponseEntity.ok(nuevoAlumno);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear alumno" + e.getMessage());
        }
    }
    

}
