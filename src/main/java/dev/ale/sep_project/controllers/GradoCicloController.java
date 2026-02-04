package dev.ale.sep_project.controllers;

import dev.ale.sep_project.dtos.grados.AsignarAlumnoRequest;
import dev.ale.sep_project.dtos.maestros.MaestroAsignarCicloDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
        cicloGradoService.crearCiclo(cicloDto);
        return ResponseEntity.ok("Ciclo creado correctamente");
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> detalleCiclo(@PathVariable Long id) {
        return ResponseEntity.ok(cicloGradoService.listarCiclosGrado(id));
    }

    @GetMapping("/detalleCiclo/{id}")
    public ResponseEntity<?> detalleSeccion(@PathVariable Long id) {
        return ResponseEntity.ok(cicloGradoService.detalleSeccion(id));
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarCiclos() {
        return ResponseEntity.ok(cicloGradoService.getCiclosDisponibles());
    }
    @GetMapping("/listar-por-usuario")
    public ResponseEntity<?> listarCiclosPorUsuario(Authentication auth) {
        return ResponseEntity.ok(cicloGradoService.listarCiclosPorUsuario(auth));
    }
    /*
    * Lista a los grados disponibles para asignar a un alumno.
    * (modificar para que solo se muestren los que están activos)
    *  */
    @GetMapping("/disponibles")
    public ResponseEntity<?> listarDisponibles() {
        return ResponseEntity.ok(cicloGradoService.getCiclosGradoDisponible());
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> borrarGrado(@PathVariable Long id) {
        cicloGradoService.eliminarCicloGrado(id);
        return ResponseEntity.ok("Ciclo grado eliminado correctamente.");
    }

    @PostMapping("/{idCiclo}/maestros/{idMaestro}")
    public ResponseEntity<?> asignarMaestro(@PathVariable Long idCiclo, @PathVariable Long idMaestro) {
        cicloGradoService.asignarMaestroCiclo(idCiclo, idMaestro);
        return ResponseEntity.ok("Maestro asignado correctamente.");
    }

    @DeleteMapping("/{idCiclo}/maestros/{idMaestro}")
    public ResponseEntity<?> desvincularMaestro(@PathVariable Long idCiclo, @PathVariable Long idMaestro) {
        cicloGradoService.desvincularMaestroCiclo(idCiclo, idMaestro);
        return ResponseEntity.ok("Maestro desvinculado correctamente.");
    }

    @PostMapping("/{idCiclo}/alumno")
    public ResponseEntity<?> aniadirAlumno(@PathVariable Long idCiclo, @RequestBody AsignarAlumnoRequest request) {
        return ResponseEntity.ok(cicloGradoService.agregarAlumno(idCiclo, request));
    }

}
