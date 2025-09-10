package dev.ale.sep_project.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import dev.ale.sep_project.dtos.alumnos.AlumnoCreateDTO;
import dev.ale.sep_project.dtos.alumnos.AlumnoResponseDTO;
import dev.ale.sep_project.dtos.alumnos.AlumnoUpdateDTO;
import dev.ale.sep_project.models.Alumno;
import dev.ale.sep_project.models.RegistroAlumno;
import dev.ale.sep_project.models.Tutor;
import dev.ale.sep_project.repository.AlumnoRepository;
import dev.ale.sep_project.repository.TutorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final TutorRepository tutorRepository;

    public void crearAlumno(AlumnoCreateDTO alumnoDto) throws Exception {
        System.out.println(alumnoDto);
        if (alumnoRepository.findByDni(alumnoDto.getDni()).isPresent()) {
            throw new Exception("El alumno ya existe");
        }
        try {
            Alumno alumno = new Alumno();
            alumno.setNombre(alumnoDto.getNombre());
            alumno.setApellido(alumnoDto.getApellido());
            alumno.setDiscapacidad(alumnoDto.getDiscapacidad());
            alumno.setDetalleDiscap(alumnoDto.getDetalleDiscap());
            alumno.setDomicilio(alumnoDto.getDomicilio());
            alumno.setDni(alumnoDto.getDni());
            alumno.setRegistroAlumno(null);
            for (Long tutorId : alumnoDto.getTutoresIds()) {
                if (!tutorRepository.existsById(tutorId)) {
                    throw new Exception("El tutor con ID " + tutorId + " no existe");
                }
                // Si el tutor existe, se puede agregar a la lista de tutores del alumno
                Tutor tutor = tutorRepository.findById(tutorId).orElseThrow(() -> new Exception("El tutor con ID " + tutorId + " no existe"));
                alumno.getTutores().add(tutor);
            }

            if (alumno.getRegistroAlumno() == null) {
                alumno.setRegistroAlumno(new ArrayList<>());
            }
            RegistroAlumno registroAlumno  = new RegistroAlumno();
            registroAlumno.setAlumno(alumno);
            alumno.getRegistroAlumno().add(registroAlumno);
            alumnoRepository.save(alumno);
            
        } catch (Exception e) {
            throw new Exception(e.getMessage().toString());
        }
    }

    // Método que traer todos los alumnos con detalles mínimos.
    public List<AlumnoResponseDTO> obtenerAlumnos() throws Exception {
        try {
            List<Alumno> alumnos = (List<Alumno>) alumnoRepository.findAll();
            List<AlumnoResponseDTO> alumnosDTO = new ArrayList<>();

            for (Alumno alu : alumnos) {
                // System.out.println(alu.getNombre());
                RegistroAlumno ultimoRegistro = alu.getRegistroAlumno().stream()
                    .sorted(Comparator.comparing(RegistroAlumno::getFechaInicio).reversed())
                    .findFirst()
                    .orElseThrow(() -> new Exception("No se encontraron registros"));
                
                AlumnoResponseDTO alumnoRespuesta = AlumnoResponseDTO.builder()
                    .nombre(alu.getNombre())
                    .apellido(alu.getApellido())
                    .dni(alu.getDni())
                    .ultGrado(ultimoRegistro.getCicloGrado().getGrado().getNroGrado())
                    .seccionGrado(ultimoRegistro.getCicloGrado().getGrado().getSeccion())
                    .turno(ultimoRegistro.getCicloGrado().getGrado().getTurno())
                    .build();
                
                alumnosDTO.add(alumnoRespuesta);
            }

            return alumnosDTO;
        } catch (Exception e) {
            throw new Exception(e.getMessage().toString());
        }
    }

    public void eliminarAlumno(Long id) throws Exception {
        try {
            if (!alumnoRepository.existsById(id)) {
                throw new Exception("El alumno no existe");
            }
            alumnoRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception(e.getMessage().toString());
        }
    }

    // Creo que puedo utilizar el DTO de creación, tiene la misma estructura, solo habría que sacarle la lista de tutores, o adaptarla para no mandar nada
    public void actualizarAlumno(Long id, AlumnoUpdateDTO alumnoDto) throws Exception {
        try {
            if (!alumnoRepository.existsById(id)) {
                throw new Exception("El alumno no existe");
            }
            Alumno alumno = alumnoRepository.findById(id).orElseThrow(() -> new Exception("El alumno no existe"));
            alumno.setNombre(alumnoDto.getNombre());
            alumno.setApellido(alumnoDto.getApellido());
            alumno.setDiscapacidad(alumnoDto.getDiscapacidad());
            alumno.setDetalleDiscap(alumnoDto.getDetalleDiscap());
            alumno.setDomicilio(alumnoDto.getDomicilio());
            alumno.setDni(alumnoDto.getDni());
            alumnoRepository.save(alumno);
        } catch (Exception e) {
            throw new Exception(e.getMessage().toString());
        }
    }
}
