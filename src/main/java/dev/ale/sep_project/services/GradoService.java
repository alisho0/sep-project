package dev.ale.sep_project.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.ale.sep_project.dtos.grados.CiclosInfoDTO;
import dev.ale.sep_project.dtos.grados.SeccionCiclo;
import dev.ale.sep_project.exceptions.BusinessLogicException;
import dev.ale.sep_project.models.CicloGrado;
import dev.ale.sep_project.models.GradoSeccionTurno;
import dev.ale.sep_project.models.Seccion;
import dev.ale.sep_project.repository.*;
import org.springframework.stereotype.Service;

import dev.ale.sep_project.dtos.grados.GradoDetalleDTO;
import dev.ale.sep_project.dtos.grados.GradoListaDTO;
import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import dev.ale.sep_project.models.Grado;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GradoService {
    private final GradoRepository gradoRepository;
    private final SeccionRepository seccionRepository;
    private final AlumnoRepository alumnoRepository;
    private final GradoSeccionTurnoRepository gradoSeccionTurnoRepository;
    private final CicloGradoRepository cicloGradoRepository;
    private final CicloGradoService cicloGradoService;  // Inyectamos el servicio de ciclos

    public List<GradoListaDTO> listarGrados() {
        List<Integer> grados = getGrados(); // ej. [1, 2, 3, ..., 47]

        List<GradoListaDTO> resultado = new ArrayList<>();

        for (Integer nroGrado : grados) {
            List<String> seccionesDisponibles = seccionRepository.findDistinctByCombinaciones_Grado_NroGrado(nroGrado.longValue()).stream()
                    .map(Seccion::getLetra)
                    .toList();

            Long cantAlumnos = alumnoRepository.countByRegistroAlumno_CicloGrado_GradoSeccionTurno_Grado_NroGrado(nroGrado.longValue());
            Grado grado = gradoRepository.findByNroGrado(nroGrado).orElseThrow(() -> new BusinessLogicException("Grado no encontrado"));
            GradoListaDTO dto = GradoListaDTO.builder()
                    .id(grado.getId())
                    .grado(nroGrado)
                    .secciones(seccionesDisponibles)
                    .cantAlumnos(cantAlumnos)
                    .build();
            // Si tenés un id de grado, lo podés setear también:
            // dto.setId(gradoRepository.getIdPorNroGrado(nroGrado));

            resultado.add(dto);
        }
        return resultado;
    }


    public GradoDetalleDTO detalleGrado(Long id) {
        Grado grado = gradoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Grado", id));

        List<SeccionCiclo> seccionCiclo = grado.getCombinaciones().stream()
                .map(g -> SeccionCiclo.builder()
                        .id(g.getId())
                        .seccion(g.getSeccion().getLetra())
                        .turno(g.getTurno().getNombreTurno())
                        .gradoCiclos(cicloGradoService.listarCiclosByGrado(g.getCicloGrado()))
                        .build())
                .toList();

        Long inscriptos = grado.getCombinaciones().stream()
                .flatMap(gst -> gst.getCicloGrado().stream())
                .mapToLong(c -> c.getRegistros().size())
                .sum();

        return GradoDetalleDTO.builder()
                .id(grado.getId())
                .nro((long) grado.getNroGrado())
                .inscriptosActuales(inscriptos)
                .seccionCiclos(seccionCiclo)
            .build();
    }

    // Métodos específicos de grado
    public Grado obtenerGrado(Long id) {
        return gradoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Grado", id));
    }

    public List<Integer> getGrados() {
        return gradoRepository.findByNroGradoAsc();
    }

    public List<String> getSecciones() {
        return seccionRepository.findSeccionesDisponibles();
    }

    public boolean existeGrado(int nroGrado, String seccion, String turno) {
        return gradoSeccionTurnoRepository.existsByGrado_NroGradoAndSeccion_LetraAndTurno_NombreTurno(nroGrado, seccion, turno);
    }

    public GradoSeccionTurno getGradoByNroSeccionTurno(int nroGrado, String seccion, String turno) {
        GradoSeccionTurno grado = gradoSeccionTurnoRepository.findByGrado_NroGradoAndSeccion_LetraAndTurno_NombreTurno(nroGrado, seccion, turno)
            .orElseThrow(() -> new ResourceNotFoundException("No se encontró el grado"));
        return grado;
    }
    // Aquí podrías agregar más métodos específicos de grado
    // como crear grado, actualizar grado, etc.
}

