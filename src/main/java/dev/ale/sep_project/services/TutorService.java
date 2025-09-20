package dev.ale.sep_project.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.ale.sep_project.dtos.tutor.TutorCreateDTO;
import dev.ale.sep_project.dtos.tutor.TutorDetalleDTO;
import dev.ale.sep_project.dtos.tutor.TutorRespuestaDTO;
import dev.ale.sep_project.exceptions.BusinessLogicException;
import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import dev.ale.sep_project.models.Tutor;
import dev.ale.sep_project.repository.AlumnoRepository;
import dev.ale.sep_project.repository.TutorRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TutorService {
    private final TutorRepository tutorRepository;
    private final AlumnoRepository alumnoRepository;

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

    public TutorDetalleDTO obtenerDetalleTutor(Long id) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor", id));
        TutorDetalleDTO respuesta = TutorDetalleDTO.builder()
                .id(tutor.getId())
                .nombre(tutor.getNombre())
                .apellido(tutor.getApellido())
                .dni(tutor.getDni())
                .domicilio(tutor.getDomicilio())
                .telefono(tutor.getTelefono())
                .telAux(tutor.getTelAux())
                .tutorDe(tutor.getAlumnos()
                        .stream()
                        .map(alumno -> alumno.getNombre() + " " + alumno.getApellido() + " (DNI: " + alumno.getDni()
                                + ")")
                        .toList())
                .build();
        return respuesta;
    }

    public void modificarTutor(Long id, TutorCreateDTO tutorEdit) {
        Tutor tutor = tutorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tutor", id));
        
        if (tutorEdit == null) {
            // Manejar el caso en que el objeto DTO es nulo
            throw new IllegalArgumentException("Los datos del tutor son inválidos");
        }
        tutor.setNombre(tutorEdit.getNombre());
        tutor.setApellido(tutorEdit.getApellido());
        tutor.setDni(tutorEdit.getDni());
        tutor.setDomicilio(tutorEdit.getDomicilio());
        tutor.setTelefono(tutorEdit.getTelefono());
        tutor.setTelAux(tutorEdit.getTelAux());
        tutorRepository.save(tutor);
    }

    public void eliminarTutor(Long id) {
        Tutor tutor = tutorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tutor", id));
        
        // Remover la relación con los alumnos antes de eliminar el tutor
        if (!tutor.getAlumnos().isEmpty()) {
            tutor.getAlumnos().forEach(alumno -> {
                alumno.getTutores().remove(tutor);
                alumnoRepository.save(alumno); // Guardar cada alumno actualizado
            });
            tutor.getAlumnos().clear();
        }
        
        tutorRepository.deleteById(id);
    }
}
