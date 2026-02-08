package dev.ale.sep_project.services;

import dev.ale.sep_project.models.*;
import dev.ale.sep_project.repository.CicloGradoRepository;
import dev.ale.sep_project.repository.UsuarioRepository;
import dev.ale.sep_project.security.jwt.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import dev.ale.sep_project.dtos.observaciones.ObservacionCreateDTO;
import dev.ale.sep_project.dtos.observaciones.ObservacionDTO;
import dev.ale.sep_project.exceptions.BusinessLogicException;
import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import dev.ale.sep_project.repository.ObservacionRepository;
import dev.ale.sep_project.repository.RegistroAlumnoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ObservacionService {
    private final ObservacionRepository observacionRepository;
    private final RegistroAlumnoRepository registroRepository;
    private final ActividadService actividadService;
    private final CicloGradoRepository cicloGradoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public ObservacionDTO nuevaObservacion(ObservacionCreateDTO observacionDTO) {
        // Validar que existe el registro
        RegistroAlumno registro = registroRepository.findById(observacionDTO.getIdRegistro())
            .orElseThrow(() -> new ResourceNotFoundException("Registro Alumno", observacionDTO.getIdRegistro()));
        if (registro.getCicloGrado().getEstado() == EstadoCiclo.CERRADO) {
            throw new BusinessLogicException("No se puede crear una observación a un ciclo cerrado.");
        }
        // Validar datos de la observación
        if (observacionDTO.getContenido() == null || observacionDTO.getContenido().trim().isEmpty()) {
            throw new BusinessLogicException("El contenido de la observación no puede estar vacío");
        }

        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Long userId = claims.get("userId", Long.class);
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", userId));

        Observacion observacion = new Observacion();
        observacion.setContenido(observacionDTO.getContenido());
        observacion.setFecha(observacionDTO.getFecha());
        observacion.setRegistroAlumno(registro);
        observacion.setUsuario(usuario);

        // Registro la actividad
        actividadService.registrarActividad("Nueva observación registrada para " + (registro.getAlumno().getNombre() + " " + registro.getAlumno().getApellido()), "OBSERVACION");

        observacionRepository.save(observacion);
        return ObservacionDTO.builder()
                .id(observacion.getId())
                .contenido(observacion.getContenido())
                .nombreUsuario(usuario.getUsername())
                .alumno(observacion.getRegistroAlumno().getAlumno().getNombre() + " " + observacion.getRegistroAlumno().getAlumno().getApellido() )
                .fecha(observacion.getFecha())
                .build();
    }

    public ObservacionDTO traerObservacion(Long id) {
        Observacion observacion = observacionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Observación", id));

        return ObservacionDTO.builder()
            .id(observacion.getId())
            .contenido(observacion.getContenido())
            .fecha(observacion.getFecha())
            .nombreUsuario("John Doe")
            // .nombreUsuario(observacion.getUsuario().getMaestro().getNombre() + " " + observacion.getUsuario().getMaestro().getApellido()) // TODO: Obtener nombre real del usuario
            .build();
    }

    public void eliminarObservacion(Long id) {
        Observacion observacion = observacionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Observación", id));
        if (observacion.getRegistroAlumno().getCicloGrado().getEstado() == EstadoCiclo.CERRADO) {
            throw new BusinessLogicException("No se puede eliminar una observación de un ciclo cerrado.");
        }
        observacionRepository.delete(observacion);
    }

    public List<ObservacionDTO> listarObservacionesPorCicloGrado (Long idCiclo) {
        CicloGrado cicloGrado = cicloGradoRepository.findById(idCiclo)
                .orElseThrow(() -> new ResourceNotFoundException("CicloGrado", idCiclo));

        List<ObservacionDTO> respuesta = cicloGrado.getRegistros().stream()
                .flatMap(r -> r.getObservaciones().stream()
                        .map(o -> ObservacionDTO.builder()
                                .id(o.getId())
                                .contenido(o.getContenido())
                                .fecha(o.getFecha())
                                //.nombreUsuario(o.getUsuario().getMaestro().getNombre() + " " + o.getUsuario().getMaestro().getApellido())
                                .nombreUsuario(o.getUsuario() != null ? o.getUsuario().getUsername() : "Sin usuario")
                                .alumno(o.getRegistroAlumno().getAlumno().getNombre() + " " + o.getRegistroAlumno().getAlumno().getApellido())
                                .build()
                        ))
                .toList();
        return respuesta;
    }
}
