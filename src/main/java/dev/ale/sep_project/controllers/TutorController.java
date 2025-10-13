package dev.ale.sep_project.controllers;

import org.springframework.web.bind.annotation.RestController;

import dev.ale.sep_project.dtos.registros.TutorListaDTO;
import dev.ale.sep_project.dtos.tutor.TutorCreateDTO;
import dev.ale.sep_project.dtos.tutor.TutorDetalleDTO;
import dev.ale.sep_project.dtos.tutor.TutorRespuestaDTO;
import dev.ale.sep_project.models.Tutor;
import dev.ale.sep_project.services.TutorService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





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

    @PostMapping("/crearConAlumno")
    public ResponseEntity<String> createTutorConAlum(@RequestBody TutorCreateDTO tutorDTO) {
        Tutor tutor = tutorService.crearTutorConAlumno(tutorDTO);
        return ResponseEntity.ok("Tutor creado exitosamente");
    }


    @GetMapping("/listar")
    public ResponseEntity<List<TutorRespuestaDTO>> listarTutors() {
        List<TutorRespuestaDTO> tutores = tutorService.listarTutores();
        return ResponseEntity.ok(tutores);
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<List<TutorListaDTO>> listarTutoresPorId(@PathVariable Long id) {
        List<TutorListaDTO> tutores = tutorService.listarTutoresPorAlumno(id);
        return ResponseEntity.ok(tutores);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<TutorDetalleDTO> obtenerDetalleTutor(@PathVariable Long id) {
        TutorDetalleDTO tutor = tutorService.obtenerDetalleTutor(id);
        return ResponseEntity.ok(tutor);
    }
    
    // Falta, editar, eliminar y detalle
    @PutMapping("editar/{id}")
    public ResponseEntity<?> editarTutor(@PathVariable Long id, @RequestBody TutorCreateDTO tutorEdit) {
        try {
            tutorService.modificarTutor(id, tutorEdit);
            return ResponseEntity.ok("Tutor editado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo editar el tutor: " + e.getMessage());
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> borrarTutor(@PathVariable Long id) {
        tutorService.eliminarTutor(id);
        return ResponseEntity.ok("Tutor eliminado correctamente");
    }
}
