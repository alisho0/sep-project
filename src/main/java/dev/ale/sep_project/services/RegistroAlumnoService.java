package dev.ale.sep_project.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import dev.ale.sep_project.exceptions.ResourceNotFoundException;

import dev.ale.sep_project.dtos.registros.RegistroCreateDTO;
import dev.ale.sep_project.models.Alumno;
import dev.ale.sep_project.models.CicloGrado;
import dev.ale.sep_project.models.RegistroAlumno;
import dev.ale.sep_project.repository.AlumnoRepository;
import dev.ale.sep_project.repository.CicloGradoRepository;
import dev.ale.sep_project.repository.RegistroAlumnoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistroAlumnoService {

    private final RegistroAlumnoRepository registroAlumnoRepository;
    private final AlumnoRepository alumnoRepository;
    private final CicloGradoRepository cicloGradoRepository;

    public void crearRegistro(RegistroCreateDTO registroAlumno) {
        Alumno alumno = alumnoRepository.findById(registroAlumno.getIdAlumno())
            .orElseThrow(() -> new ResourceNotFoundException("Alumno", registroAlumno.getIdAlumno()));
        
        CicloGrado cicloGrado = cicloGradoRepository.findById(registroAlumno.getIdCicloGrado())
            .orElseThrow(() -> new ResourceNotFoundException("Ciclo Grado", registroAlumno.getIdCicloGrado()));
        
        try {
            RegistroAlumno registroNuevo = RegistroAlumno.builder()
                .alumno(alumno)
                .cicloGrado(cicloGrado)
                .fechaInicio(LocalDate.now())
                .fechaFin(null)
                .observaciones(new ArrayList<>())
                .build();
            registroAlumnoRepository.save(registroNuevo);
        } catch (Exception e) {
            new Exception("No se pudo guardar el registro" + " - " + e.getMessage());
        }
    }

    public void actualizarRegistro(Long id, RegistroAlumno registroAlumno) throws Exception { // pendiente
        if (!registroAlumnoRepository.existsById(id)) {
            throw new Exception("El registro no existe");
        }
        registroAlumno.setId(id);
        registroAlumnoRepository.save(registroAlumno);
    }

    public void eliminarRegistro(Long id) throws Exception {
        if (!registroAlumnoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Registro", id);
        }
        registroAlumnoRepository.deleteById(id);
    }

    public List<RegistroAlumno> obtenerRegistrosPorAlumno(Long alumnoId) { // Pendiente
        // return registroAlumnoRepository.findByAlumnoId(alumnoId);
        return null;
    }
}
