package dev.ale.sep_project.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import dev.ale.sep_project.dtos.alumnos.AlumnoInscriptoDTO;
import dev.ale.sep_project.dtos.alumnos.AlumnoResponseDTO;
import dev.ale.sep_project.dtos.grados.*;
import dev.ale.sep_project.dtos.maestros.MaestroAsignarCicloDTO;
import dev.ale.sep_project.exceptions.ResourceAlreadyExistsException;
import dev.ale.sep_project.models.*;
import dev.ale.sep_project.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import dev.ale.sep_project.exceptions.BusinessLogicException;
import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CicloGradoService {
    private final CicloGradoRepository cicloGradoRepository;
    private final GradoSeccionTurnoRepository gradoSeccionTurnoRepository;
    private final GradoRepository gradoRepository;
    private final MaestroRepository maestroRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;
    private final RegistroAlumnoRepository registroAlumnoRepository;

    public void crearCiclo(CicloCreateDTO cicloDto) {
        GradoSeccionTurno grado = gradoSeccionTurnoRepository.findById(cicloDto.getId_grado_seccion_grado())
            .orElseThrow(() -> new ResourceNotFoundException("GradoSeccionTurno", cicloDto.getId_grado_seccion_grado()));

        // Verificar si ya existe un ciclo para este grado en el año especificado
        boolean cicloExistente = grado.getCicloGrado().stream()
            .anyMatch(ciclo -> ciclo.getAnio() == cicloDto.getAnio());
        
        if (cicloExistente) {
            throw new ResourceAlreadyExistsException(
                String.format("Ya existe un ciclo para el grado %d° %s turno %s en el año %d",
                    grado.getGrado().getNroGrado(),
                    grado.getSeccion().getLetra(),
                    grado.getTurno().getNombreTurno(),
                    cicloDto.getAnio())
            );
        }

        CicloGrado nuevoCiclo = new CicloGrado();
        nuevoCiclo.setAnio(cicloDto.getAnio());
        nuevoCiclo.setGradoSeccionTurno(grado);
        cicloGradoRepository.save(nuevoCiclo);
    }
    public List<CiclosGradoDTO> listarCiclosPorUsuario(Authentication auth) {
        Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<CicloGrado> ciclos = cicloGradoRepository.findByMaestros_Usuario_Id(usuario.getId());
        if (ciclos == null) {
            throw new ResourceNotFoundException("No se encontraron los ciclos para el usuario con ID " + usuario.getId());
        }

        return ciclos.stream()
                .map(c -> CiclosGradoDTO.builder()
                        .id(c.getId())
                        .gradoId(c.getGradoSeccionTurno().getGrado().getId())
                        .anio(c.getAnio())
                        .grado(c.getGradoSeccionTurno().getGrado().getNroGrado())
                        .seccion(c.getGradoSeccionTurno().getSeccion().getLetra())
                        .turno(c.getGradoSeccionTurno().getTurno().getNombreTurno())
                        .build())
                .toList();
    }


    public List<GradoCiclosDTO> listarCiclosGrado(Long gradoId) {

        GradoSeccionTurno grado = gradoSeccionTurnoRepository.findById(gradoId)
                .orElseThrow(() -> new ResourceNotFoundException("GradoSeccionTurno", gradoId));

        return grado.getCicloGrado().stream()
                .map(this::buildDto)
                .toList();
    }

    public List<GradoCiclosDTO> listarCiclosByGrado(List<CicloGrado> ciclos) {
        return ciclos.stream()
                .map(this::buildDto)
                .toList();
    }


    public void eliminarCicloGrado(Long id) {
        CicloGrado ciclo = cicloGradoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ciclo", id));
        cicloGradoRepository.delete(ciclo);
    }

    public List<Long> getCiclosDisponibles() {
        return cicloGradoRepository.findAniosDisponibles();
    }

    public CicloGrado obtenerCiclo(Long cicloId) {
        return cicloGradoRepository.findById(cicloId)
            .orElseThrow(() -> new ResourceNotFoundException("Ciclo", cicloId));
    }

    // finalizar metodo
    public void finalizarCiclo(Long cicloId) {
        CicloGrado ciclo = obtenerCiclo(cicloId);
        // Aquí podrías agregar lógica para finalizar un ciclo
        // Por ejemplo, verificar que no haya registros activos
        if (!ciclo.getRegistros().isEmpty()) {
            throw new BusinessLogicException("No se puede finalizar un ciclo con registros activos");
        }
        // Más lógica de finalización...
    }

    public CicloDetalleDTO detalleSeccion(Long id) {
        CicloGrado ciclo = cicloGradoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CicloGrado", id));
        Long cantAlumnos = (long) ciclo.getRegistros().size();

//        List<AlumnoResponseDTO> alumnos = ciclo.getRegistros().stream()
//                .map(r -> AlumnoResponseDTO.builder()
//                        .id(r.getAlumno().getId())
//                        .nombre(r.getAlumno().getNombre())
//                        .apellido(r.getAlumno().getApellido())
//                        .dni(r.getAlumno().getDni())
//                        .registro(r.getId())
//                        .build())
//                .toList();

        return new CicloDetalleDTO(ciclo.getId(),
                cantAlumnos,
                (long) ciclo.getAnio(),
                (long) ciclo.getGradoSeccionTurno().getGrado().getNroGrado()
                );
    }

    public CicloGrado getCicloGrado(int anio, GradoSeccionTurno grado) {
        CicloGrado cicloGrado = cicloGradoRepository.findByAnioAndGradoSeccionTurno(anio, grado)
            .orElseThrow(() -> new ResourceNotFoundException("No se encontró el ciclo grado"));
        return cicloGrado;
    }

    public boolean existeCicloGrado(int anio, GradoSeccionTurno grado) {
        return cicloGradoRepository.existsByAnioAndGradoSeccionTurno(anio, grado);
    }

    public List<CiclosGradoDTO> getCiclosGradoDisponible() {
        List<CicloGrado> ciclosGrados = (List<CicloGrado>) cicloGradoRepository.findAll();

        return ciclosGrados.stream()
            .map(ciclo -> CiclosGradoDTO.builder()
                .id(ciclo.getId())
                .grado(ciclo.getGradoSeccionTurno().getGrado().getNroGrado())
                .seccion(ciclo.getGradoSeccionTurno().getSeccion().getLetra())
                .turno(ciclo.getGradoSeccionTurno().getTurno().getNombreTurno())
                .anio(ciclo.getAnio())
                .estado(ciclo.getEstado().name())
                .build())
            .toList();
    }

    public void asignarMaestroCiclo(Long idCiclo, Long idMaestro) {
        CicloGrado cicloGrado = cicloGradoRepository.findById(idCiclo)
                .orElseThrow(() -> new ResourceNotFoundException("CicloGrado", idCiclo));
        Maestro maestro = maestroRepository.findById(idMaestro)
                .orElseThrow(() -> new ResourceNotFoundException("Maestro", idMaestro));

        if (cicloGrado.getMaestros() == null) {
            cicloGrado.setMaestros(new ArrayList<>());
        }

        if (!cicloGrado.getMaestros().contains(maestro)) {
            cicloGrado.getMaestros().add(maestro);
        }

        cicloGradoRepository.save(cicloGrado);
    }

    public void desvincularMaestroCiclo(Long idCiclo, Long idMaestro) {
        CicloGrado cicloGrado = cicloGradoRepository.findById(idCiclo)
                .orElseThrow(() -> new ResourceNotFoundException("CicloGrado", idCiclo));
        Maestro maestro = maestroRepository.findById(idMaestro)
                .orElseThrow(() -> new ResourceNotFoundException("Maestro", idMaestro));

        if (cicloGrado.getMaestros() != null) {
            cicloGrado.getMaestros().removeIf(m -> m.getId().equals(maestro.getId()));
        }

        cicloGradoRepository.save(cicloGrado);
    }

    private GradoCiclosDTO buildDto(CicloGrado ciclo) {
        return GradoCiclosDTO.builder()
                .id(ciclo.getId())
                .anio(ciclo.getAnio())
                .cantAlumnos(ciclo.getRegistros().size())
                .estado(ciclo.getEstado())
                .build();
    }

    @Transactional
    public AlumnoInscriptoDTO agregarAlumno(Long cicloId, AsignarAlumnoRequest request) {
        Alumno alumno = alumnoRepository.findById(request.getAlumnoId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", request.getAlumnoId()));
        CicloGrado cicloGrado = cicloGradoRepository.findById(cicloId)
                .orElseThrow(() -> new ResourceNotFoundException("CicloGrado", cicloId));

        boolean anioRepetido = alumno.getRegistroAlumno().stream()
                .anyMatch(r -> r.getCicloGrado().getAnio() == cicloGrado.getAnio());

        if (anioRepetido) {
            throw new BusinessLogicException("El alumno ya tiene un registro este año " + cicloGrado.getAnio());
        } else {
            RegistroAlumno registroAlumno = RegistroAlumno.builder()
                    .alumno(alumno)
                    .cicloGrado(cicloGrado)
                    .fechaInicio(LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")))
                    .build();
            RegistroAlumno r = registroAlumnoRepository.save(registroAlumno);

            return AlumnoInscriptoDTO.builder()
                    .id(alumno.getId())
                    .nombre(alumno.getNombre() + " " + alumno.getApellido())
                    .dni(alumno.getDni())
                    .idRegistro(r.getId())
                    .build();
        }

    }

    @Transactional
    public void cerrarCiclo(Long idCiclo) {

        CicloGrado cicloGrado = cicloGradoRepository.findById(idCiclo)
                .orElseThrow(() -> new ResourceNotFoundException("CicloGrado", idCiclo));
        if (cicloGrado.getEstado() == EstadoCiclo.CERRADO) {
            throw new BusinessLogicException("El ciclo ya está cerrado");
        }

        cicloGrado.setEstado(EstadoCiclo.CERRADO);
        LocalDate fechaFin = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        cicloGrado.getRegistros().forEach(r -> r.setFechaFin(fechaFin));
    }
}