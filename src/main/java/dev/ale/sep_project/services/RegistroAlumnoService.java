package dev.ale.sep_project.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.ale.sep_project.models.GradoSeccionTurno;
import jakarta.transaction.Transactional;
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
    private final CicloGradoService cicloGradoService;
    private final GradoService gradoService;

    @Transactional
    public RegistroAniosDTO crearRegistro(RegistroCreateDTO registroAlumno) {
        Alumno alumno = alumnoRepository.findById(registroAlumno.getIdAlumno())
            .orElseThrow(() -> new ResourceNotFoundException("Alumno no encontrado", registroAlumno.getIdAlumno()));
//        CicloGrado cicloGrado = cicloGradoRepository.findById(registroAlumno.getIdCicloGrado())
//            .orElseThrow(() -> new ResourceNotFoundException("Ciclo Grado", registroAlumno.getIdCicloGrado()));

        try {
            RegistroAlumno primerRegistro = new RegistroAlumno();

            if (!gradoService.existeGrado(Math.toIntExact(registroAlumno.getNroGrado()), registroAlumno.getSeccionGrado(), registroAlumno.getTurnoGrado())) {
                throw new Exception("No existe el grado enviado");
            }
            // traemos el grado
            GradoSeccionTurno grado = gradoService.getGradoByNroSeccionTurno(Math.toIntExact(registroAlumno.getNroGrado()), registroAlumno.getSeccionGrado(), registroAlumno.getTurnoGrado());

            if (!cicloGradoService.existeCicloGrado(Math.toIntExact(registroAlumno.getAnioCicloGrado()), grado)) {
                throw new Exception("No existe el ciclo grado enviado");
            }

            CicloGrado ciclo = cicloGradoService.getCicloGrado(Math.toIntExact(registroAlumno.getAnioCicloGrado()), grado);
            primerRegistro.setCicloGrado(ciclo);
            primerRegistro.setAlumno(alumno);

            boolean yaExiste = registroAlumnoRepository.existsByAlumnoIdAndCicloGradoId(registroAlumno.getIdAlumno(), ciclo.getId());
            System.out.println("Salida: " + yaExiste);
            if (yaExiste) {
                throw new IllegalStateException("El alumno ya tiene un registro para este ciclo grado");
            }

            RegistroAlumno registroNuevo = new RegistroAlumno();
            registroNuevo.setAlumno(alumno);
            registroNuevo.setCicloGrado(ciclo);
            registroNuevo.setObservaciones(new ArrayList<>());
            registroNuevo.setFechaFin(null);
            registroNuevo.setFechaInicio(LocalDate.now());

            RegistroAlumno r = registroAlumnoRepository.save(registroNuevo);
            return RegistroAniosDTO.builder()
                    .id(r.getId())
                    .anio(ciclo.getAnio())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo guardar el registro - " + e.getMessage(), e);
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
                .nroGrado(registro.getCicloGrado().getGradoSeccionTurno().getGrado().getNroGrado())
                .seccion(registro.getCicloGrado().getGradoSeccionTurno().getSeccion().getLetra())
                .turno(registro.getCicloGrado().getGradoSeccionTurno().getTurno().getNombreTurno())
                .anioCiclo(registro.getCicloGrado().getAnio())
                .fechaInicio(registro.getFechaInicio())
                .fechaFin(registro.getFechaFin())
                .observaciones(
                    registro.getObservaciones()
                        .stream()
                        .map(observacion -> ObservacionDTO.builder()
                            .id(observacion.getId())
                            .contenido(observacion.getContenido())
                            .nombreUsuario(observacion.getUsuario() != null ? observacion.getUsuario().getUsername() : "Sin usuario")
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
