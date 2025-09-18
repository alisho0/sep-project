package dev.ale.sep_project.controllers;

import org.springframework.web.bind.annotation.RestController;

import dev.ale.sep_project.dtos.tutor.TutorCreateDTO;
import dev.ale.sep_project.dtos.tutor.TutorDetalleDTO;
import dev.ale.sep_project.dtos.tutor.TutorRespuestaDTO;
import dev.ale.sep_project.models.Tutor;
import dev.ale.sep_project.services.TutorService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/tutor")
@RequiredArgsConstructor
public class TutorController {
    private final TutorService tutorService;

    @PostMapping("/crear")
    public ResponseEntity<String> createTutor(@RequestBody TutorCreateDTO tutorDTO) {
        Tutor tutor = tutorService.crearTutor(tutorDTO);
        return ResponseEntity.ok("Tutor creado exitosamente");
    }

    @GetMapping("/listar")
    public ResponseEntity<List<TutorRespuestaDTO>> listarTutors() {
        List<TutorRespuestaDTO> tutores = tutorService.listarTutores();
        return ResponseEntity.ok(tutores);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<TutorDetalleDTO> obtenerDetalleTutor(@PathVariable Long id) {
        TutorDetalleDTO tutor = tutorService.obtenerDetalleTutor(id);
        return ResponseEntity.ok(tutor);
    }
    
    // Falta, editar, eliminar y detalle
}
