package dev.ale.sep_project.controllers;

import dev.ale.sep_project.dtos.alumnos.*;
import org.springframework.web.bind.annotation.RestController;

import dev.ale.sep_project.models.Alumno;
import dev.ale.sep_project.services.AlumnoService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;





@RequiredArgsConstructor
@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

    private final AlumnoService alumnoService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearAlumno(@RequestBody AlumnoCreateDTO alumno) {
        AlumnoResponseDTO alu = alumnoService.crearAlumno(alumno);
        return ResponseEntity.status(HttpStatus.CREATED).body(alu);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> obtenerDetalleAlumno(@PathVariable Long id) {
        AlumnoDetalleDTO alumnoDetalle = alumnoService.obtenerAlumno(id);
        return ResponseEntity.ok(alumnoDetalle);
    }
    
    @GetMapping("/listar")
    public ResponseEntity<?> listarAlumnos() {
        List<AlumnoResponseDTO> alumnos = alumnoService.obtenerAlumnos();
        return ResponseEntity.ok(alumnos);
    }

    @GetMapping("listarPorCSG/{id}")
    public ResponseEntity<?> listarPorCSG(@PathVariable Long id) {
        return ResponseEntity.ok(alumnoService.listarAlumnosPorCSG(id));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarAlumno(@PathVariable Long id, @RequestBody AlumnoUpdateDTO alumnoDto) {
        alumnoService.actualizarAlumno(id, alumnoDto);
        return ResponseEntity.ok("Alumno actualizado correctamente");

    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarAlumno(@PathVariable Long id) {
        alumnoService.eliminarAlumno(id);
        return ResponseEntity.ok("Alumno eliminado correctamente");
    }

    @PutMapping("/asignarTutor/{idAlumno}/{idTutor}")
    public ResponseEntity<?> asignarTutor(@PathVariable Long idAlumno, @PathVariable Long idTutor) {
        alumnoService.agregarTutor(idAlumno, idTutor);
        return ResponseEntity.ok("Tutor asignado al alumno correctamente");
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarAlumno(@RequestParam String nombre) {
        List<AlumnoResponseDTO> alumnos = alumnoService.searchAlumnos(nombre);
        return ResponseEntity.ok(alumnos);
    }
}
