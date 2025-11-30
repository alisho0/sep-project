package dev.ale.sep_project.services;

import java.util.List;

import dev.ale.sep_project.models.GradoSeccionTurno;
import dev.ale.sep_project.repository.GradoSeccionTurnoRepository;
import org.springframework.stereotype.Service;
import dev.ale.sep_project.dtos.grados.CicloCreateDTO;
import dev.ale.sep_project.dtos.grados.CiclosGradoDTO;
import dev.ale.sep_project.dtos.grados.GradoCiclosDTO;
import dev.ale.sep_project.exceptions.BusinessLogicException;
import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import dev.ale.sep_project.models.CicloGrado;
import dev.ale.sep_project.models.Grado;
import dev.ale.sep_project.repository.CicloGradoRepository;
import dev.ale.sep_project.repository.GradoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CicloGradoService {
    private final CicloGradoRepository cicloGradoRepository;
    private final GradoSeccionTurnoRepository gradoSeccionTurnoRepository;
    private final GradoRepository gradoRepository;

    public void crearCiclo(CicloCreateDTO cicloDto) {
        GradoSeccionTurno grado = gradoSeccionTurnoRepository.findById(cicloDto.getId_grado_seccion_grado())
            .orElseThrow(() -> new ResourceNotFoundException("GradoSeccionTurno", cicloDto.getId_grado_seccion_grado()));

        // Verificar si ya existe un ciclo para este grado en el año especificado
        boolean cicloExistente = grado.getCicloGrado().stream()
            .anyMatch(ciclo -> ciclo.getAnio() == cicloDto.getAnio());
        
        if (cicloExistente) {
            throw new BusinessLogicException(
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

    public List<GradoCiclosDTO> listarCiclosGrado(Long gradoId) {
        GradoSeccionTurno grado = gradoSeccionTurnoRepository.findById(gradoId)
            .orElseThrow(() -> new ResourceNotFoundException("GradoSeccionTurno", gradoId));
        
        return grado.getCicloGrado().stream()
            .map(ciclo -> GradoCiclosDTO.builder()
                .id(ciclo.getId())
                .anio(ciclo.getAnio())
                .cantAlumnos(ciclo.getRegistros().size())
                .build())
            .toList();
    }
    public List<GradoCiclosDTO> listarCiclosByGrado(List<CicloGrado> ciclos) {
        return ciclos.stream()
                .map(c -> GradoCiclosDTO.builder()
                        .id(c.getId())
                        .anio(c.getAnio())
                        .cantAlumnos(c.getRegistros().size())
                        .build())
                .toList();
    }

    public void eliminarCicloGrado(Long id) {
        try {
            CicloGrado ciclo = cicloGradoRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Ciclo", id));
            cicloGradoRepository.delete(ciclo);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Long> getCiclosDisponibles() {
        return cicloGradoRepository.findAniosDisponibles();
    }

    public CicloGrado obtenerCiclo(Long cicloId) {
        return cicloGradoRepository.findById(cicloId)
            .orElseThrow(() -> new ResourceNotFoundException("Ciclo", cicloId));
    }

    public void finalizarCiclo(Long cicloId) {
        CicloGrado ciclo = obtenerCiclo(cicloId);
        // Aquí podrías agregar lógica para finalizar un ciclo
        // Por ejemplo, verificar que no haya registros activos
        if (!ciclo.getRegistros().isEmpty()) {
            throw new BusinessLogicException("No se puede finalizar un ciclo con registros activos");
        }
        // Más lógica de finalización...
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
                .build())
            .toList();
    }
}