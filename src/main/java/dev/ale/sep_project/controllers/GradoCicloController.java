package dev.ale.sep_project.controllers;

import dev.ale.sep_project.dtos.grados.AsignarAlumnoRequest;
import dev.ale.sep_project.dtos.maestros.MaestroAsignarCicloDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @PostMapping("/crearCiclo")
    public ResponseEntity<?> nuevoCiclo(@RequestBody CicloCreateDTO cicloDto) {
        cicloGradoService.crearCiclo(cicloDto);
        return ResponseEntity.ok("Ciclo creado correctamente");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> detalleCiclo(@PathVariable Long id) {
        return ResponseEntity.ok(cicloGradoService.listarCiclosGrado(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @GetMapping("/detalle-ciclo/{id}")
    public ResponseEntity<?> detalleSeccion(@PathVariable Long id) {
        return ResponseEntity.ok(cicloGradoService.detalleSeccion(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @GetMapping("/disponibles")
    public ResponseEntity<?> listarDisponibles() {
        return ResponseEntity.ok(cicloGradoService.getCiclosGradoDisponible());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> borrarGrado(@PathVariable Long id) {
        cicloGradoService.eliminarCicloGrado(id);
        return ResponseEntity.ok("Ciclo grado eliminado correctamente.");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @PostMapping("/{idCiclo}/maestros/{idMaestro}")
    public ResponseEntity<?> asignarMaestro(@PathVariable Long idCiclo, @PathVariable Long idMaestro) {
        cicloGradoService.asignarMaestroCiclo(idCiclo, idMaestro);
        return ResponseEntity.ok("Maestro asignado correctamente.");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @DeleteMapping("/{idCiclo}/maestros/{idMaestro}")
    public ResponseEntity<?> desvincularMaestro(@PathVariable Long idCiclo, @PathVariable Long idMaestro) {
        cicloGradoService.desvincularMaestroCiclo(idCiclo, idMaestro);
        return ResponseEntity.ok("Maestro desvinculado correctamente.");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @PostMapping("/{idCiclo}/alumno")
    public ResponseEntity<?> aniadirAlumno(@PathVariable Long idCiclo, @RequestBody AsignarAlumnoRequest request) {
        return ResponseEntity.ok(cicloGradoService.agregarAlumno(idCiclo, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @DeleteMapping("/{idCiclo}/alumno/{idAlumno}")
    public ResponseEntity<?> desvincularAlumno(@PathVariable Long idCiclo, @PathVariable Long idAlumno) {
        cicloGradoService.desvincularAlumno(idCiclo, idAlumno);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @PutMapping("/{idCiclo}/cerrar")
    public ResponseEntity<?> cerrarCiclo(@PathVariable Long idCiclo)  {
        cicloGradoService.cerrarCiclo(idCiclo);
        return ResponseEntity.ok("Ciclo cerrado correctamente");
    }
}
