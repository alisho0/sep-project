package dev.ale.sep_project.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

import java.util.List;


@RestController
@RequestMapping("/observacion")
@RequiredArgsConstructor
public class ObservacionController {

    private final ObservacionService observacionService;

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @PostMapping("/crear")
    public ResponseEntity<?> crearObservacion(@RequestBody ObservacionCreateDTO observacionDTO) {
        return ResponseEntity.ok(observacionService.nuevaObservacion(observacionDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> detalleObservacion(@PathVariable Long id) {
        ObservacionDTO observacionDTO = observacionService.traerObservacion(id);
        return ResponseEntity.ok(observacionDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarObservacion(@PathVariable Long id) {
        observacionService.eliminarObservacion(id);
        return ResponseEntity.ok("Observación eliminada exitosamente");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @GetMapping("/listar/{id}")
    public ResponseEntity<?> listarObsPorCicloGrado(@PathVariable Long id) {
        List<ObservacionDTO> obs = observacionService.listarObservacionesPorCicloGrado(id);
        return ResponseEntity.ok(obs);
    }
}
