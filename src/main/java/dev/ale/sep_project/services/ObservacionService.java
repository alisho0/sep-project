package dev.ale.sep_project.services;

import dev.ale.sep_project.models.CicloGrado;
import dev.ale.sep_project.repository.CicloGradoRepository;
import org.springframework.stereotype.Service;

import dev.ale.sep_project.dtos.observaciones.ObservacionCreateDTO;
import dev.ale.sep_project.dtos.observaciones.ObservacionDTO;
import dev.ale.sep_project.exceptions.BusinessLogicException;
import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import dev.ale.sep_project.models.Observacion;
import dev.ale.sep_project.models.RegistroAlumno;
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

    @Transactional
    public void nuevaObservacion(ObservacionCreateDTO observacionDTO) {
        // Validar que existe el registro
        RegistroAlumno registro = registroRepository.findById(observacionDTO.getIdRegistro())
            .orElseThrow(() -> new ResourceNotFoundException("Registro Alumno", observacionDTO.getIdRegistro()));

        // Validar datos de la observación
        if (observacionDTO.getContenido() == null || observacionDTO.getContenido().trim().isEmpty()) {
            throw new BusinessLogicException("El contenido de la observación no puede estar vacío");
        }

        Observacion observacion = new Observacion();
        observacion.setContenido(observacionDTO.getContenido());
        observacion.setFecha(observacionDTO.getFecha());
        observacion.setRegistroAlumno(registro);
        observacion.setUsuario(null); // TODO: Obtener usuario actual cuando implementes autenticación

        // Registro la actividad
        actividadService.registrarActividad("Nueva observación registrada para " + (registro.getAlumno().getNombre() + " " + registro.getAlumno().getApellido()), "OBSERVACION");

        observacionRepository.save(observacion);
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

        observacionRepository.delete(observacion);
    }

    public List<ObservacionDTO> listarObservacionesPorCicloGrado (Long idCiclo) {
        CicloGrado cicloGrado = cicloGradoRepository.findById(idCiclo)
                .orElseThrow(() -> new ResourceNotFoundException("CicloGrado", idCiclo));

        try {
            List<ObservacionDTO> respuesta = cicloGrado.getRegistros().stream()
                    .flatMap(r -> r.getObservaciones().stream()
                            .map(o -> ObservacionDTO.builder()
                                            .id(o.getId())
                                            .contenido(o.getContenido())
                                            .fecha(o.getFecha())
                                            //.nombreUsuario(o.getUsuario().getMaestro().getNombre() + " " + o.getUsuario().getMaestro().getApellido())
                                            .nombreUsuario("John Doe")
                                            .build()
                            ))
                            .toList();
            return respuesta;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error creando el listado de observaciones por ciclo. " + e.getMessage());
        }

    }
}
