package dev.ale.sep_project.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import dev.ale.sep_project.dtos.observaciones.ObservacionDTO;
import dev.ale.sep_project.dtos.registros.RegistroAniosDTO;
import dev.ale.sep_project.dtos.registros.RegistroCreateDTO;
import dev.ale.sep_project.dtos.registros.RegistroRespuestaDTO;
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

    public RegistroRespuestaDTO obtenerDatosRegistro(Long id) {
        RegistroAlumno registro = registroAlumnoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Registro", id));

        try {
            RegistroRespuestaDTO respuestaDTO = RegistroRespuestaDTO.builder()
                .id(registro.getId())
                .nroGrado(registro.getCicloGrado().getGrado().getNroGrado())
                .seccion(registro.getCicloGrado().getGrado().getSeccion())
                .turno(registro.getCicloGrado().getGrado().getTurno())
                .anioCiclo(registro.getCicloGrado().getAnio())
                .fechaInicio(registro.getFechaInicio())
                .fechaFin(registro.getFechaFin())
                .observaciones(
                    registro.getObservaciones()
                        .stream()
                        .map(observacion -> ObservacionDTO.builder()
                            .id(observacion.getId())
                            .contenido(observacion.getContenido())
                            .nombreUsuario("John Doe")
                            .fecha(observacion.getFecha())
                            .build())
                        .collect(Collectors.toList()))
                .build();
            
            return respuestaDTO;
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un problema al devolver el registro - " + e.getMessage());
        }
    }

    public List<RegistroAniosDTO> obtenerAniosDisponibles(Long id) {
        try {
            Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", id));

            List<RegistroAlumno> registros = alumno.getRegistroAlumno();

            List<RegistroAniosDTO> listaRespuesta = registros.stream()
                .map( registro -> RegistroAniosDTO.builder()
                    .id(registro.getId())
                    .anio(registro.getCicloGrado().getAnio())
                    .build())
                .collect(Collectors.toList());

            return listaRespuesta;
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un problema al devolver los años disponibles - " + e.getMessage());
        }
    }
}
