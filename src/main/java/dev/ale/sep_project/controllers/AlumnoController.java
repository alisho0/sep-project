package dev.ale.sep_project.controllers;

import dev.ale.sep_project.dtos.alumnos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @PostMapping("/crear")
    public ResponseEntity<?> crearAlumno(@RequestBody AlumnoCreateDTO alumno) {
        AlumnoResponseDTO alu = alumnoService.crearAlumno(alumno);
        return ResponseEntity.status(HttpStatus.CREATED).body(alu);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> obtenerDetalleAlumno(@PathVariable Long id) {
        AlumnoDetalleDTO alumnoDetalle = alumnoService.obtenerAlumno(id);
        return ResponseEntity.ok(alumnoDetalle);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @GetMapping("/listar")
    public ResponseEntity<?> listarAlumnos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AlumnoResponseDTO> alumnos = alumnoService.obtenerAlumnos(pageable);
        return ResponseEntity.ok(alumnos);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @GetMapping("listarPorCSG/{id}")
    public ResponseEntity<?> listarPorCSG(@PathVariable Long id) {
        return ResponseEntity.ok(alumnoService.listarAlumnosPorCSG(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarAlumno(@PathVariable Long id, @RequestBody AlumnoUpdateDTO alumnoDto) {
        AlumnoDetalleDTO alu = alumnoService.actualizarAlumno(id, alumnoDto);
        return ResponseEntity.ok(alu);

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarAlumno(@PathVariable Long id) {
        alumnoService.eliminarAlumno(id);
        return ResponseEntity.ok("Alumno eliminado correctamente");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @PutMapping("/asignarTutor/{idAlumno}/{idTutor}")
    public ResponseEntity<?> asignarTutor(@PathVariable Long idAlumno, @PathVariable Long idTutor) {
        alumnoService.agregarTutor(idAlumno, idTutor);
        return ResponseEntity.ok("Tutor asignado al alumno correctamente");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarAlumno(@RequestParam String nombre) {
        List<AlumnoResponseDTO> alumnos = alumnoService.searchAlumnos(nombre);
        return ResponseEntity.ok(alumnos);
    }
}
