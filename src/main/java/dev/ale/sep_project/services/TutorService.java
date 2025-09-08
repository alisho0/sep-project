package dev.ale.sep_project.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.ale.sep_project.dtos.tutor.TutorCreateDTO;
import dev.ale.sep_project.dtos.tutor.TutorRespuestaDTO;
import dev.ale.sep_project.models.Tutor;
import dev.ale.sep_project.repository.TutorRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TutorService {
    private final TutorRepository tutorRepository;

    // Métodos del servicio (crear, actualizar, eliminar, buscar, etc.)
    public Tutor crearTutor(TutorCreateDTO tutorDTO) {
        if (tutorRepository.findByDni(tutorDTO.getDni()).isPresent()) {
            // Manejar el caso en que el tutor ya existe
            throw new RuntimeException("El tutor ya existe");
        }
        // Lógica para crear y guardar el tutor
        try {
            Tutor tutor = new Tutor();
            tutor.setDni(tutorDTO.getDni());
            tutor.setNombre(tutorDTO.getNombre());
            tutor.setApellido(tutorDTO.getApellido());
            tutor.setTelefono(tutorDTO.getTelefono());
            tutor.setDomicilio(tutorDTO.getDomicilio());
            tutor.setAlumnos(null); // Inicializar la lista de alumnos como null o una lista vacía
            if (tutorDTO.getTelAux() == null) {
                tutor.setTelAux("");
            } else {
                tutor.setTelAux(tutorDTO.getTelAux());
            }
            tutorRepository.save(tutor);
            return tutor;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el tutor");
        }
    }

    public List<TutorRespuestaDTO> listarTutores() {
        List<Tutor> tutores = (List<Tutor>) tutorRepository.findAll();
        return tutores.stream()
                .map(tutor -> TutorRespuestaDTO.builder()
                        .id(tutor.getId())
                        .nombre(tutor.getNombre())
                        .apellido(tutor.getApellido())
                        .dni(tutor.getDni())
                        .tutorDe(tutor.getAlumnos()
                            .stream()
                            .map(alumno -> alumno.getNombre() + " " + alumno.getApellido())
                            .toList())
                        .build())
                .toList();
    }
}
