package dev.ale.sep_project.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import dev.ale.sep_project.dtos.alumnos.AlumnoResponseDTO;
import dev.ale.sep_project.models.Alumno;
import dev.ale.sep_project.models.RegistroAlumno;
import dev.ale.sep_project.repository.AlumnoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;

    public Alumno crearAlumno(Alumno alumno) throws Exception {
        if (alumnoRepository.findByDni(alumno.getDni()).isPresent()) {
            throw new Exception("El alumno ya existe");
        }
        try {
            return alumnoRepository.save(alumno);
        } catch (Exception e) {
            throw new Exception("Error al crear el alumno");
        }
    }

    // Método que traer todos los alumnos con detalles mínimos.
    public List<AlumnoResponseDTO> obtenerAlumnos() throws Exception {
        try {
            List<Alumno> alumnos = (List<Alumno>) alumnoRepository.findAll();
            List<AlumnoResponseDTO> alumnosDTO = new ArrayList<>();

            for (Alumno alu : alumnos) {
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
            throw new Exception("Error al obtener los alumnos");
        }
    }
}
