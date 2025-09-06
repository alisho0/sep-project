package dev.ale.sep_project.controllers;

import org.springframework.web.bind.annotation.RestController;

import dev.ale.sep_project.dtos.tutor.TutorCreateDTO;
import dev.ale.sep_project.models.Tutor;
import dev.ale.sep_project.services.TutorService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok("Tutor creado exitosamente con estos datos: " + tutor.toString());
    }
    
}
