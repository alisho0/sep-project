package dev.ale.sep_project.services;

import java.util.List;
import org.springframework.stereotype.Service;
import dev.ale.sep_project.dtos.grados.CicloCreateDTO;
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
    private final GradoRepository gradoRepository;

    public void crearCiclo(CicloCreateDTO cicloDto) {
        Grado grado = gradoRepository.findById(cicloDto.getId_grado())
            .orElseThrow(() -> new ResourceNotFoundException("Grado", cicloDto.getId_grado()));

        // Verificar si ya existe un ciclo para este grado en el año especificado
        boolean cicloExistente = grado.getCicloGrado().stream()
            .anyMatch(ciclo -> ciclo.getAnio() == cicloDto.getAnio());
        
        if (cicloExistente) {
            throw new BusinessLogicException(
                String.format("Ya existe un ciclo para el grado %d° %s turno %s en el año %d",
                    grado.getNroGrado(), 
                    grado.getSeccion(), 
                    grado.getTurno(), 
                    cicloDto.getAnio())
            );
        }

        CicloGrado nuevoCiclo = new CicloGrado();
        nuevoCiclo.setAnio(cicloDto.getAnio());
        nuevoCiclo.setGrado(grado);
        cicloGradoRepository.save(nuevoCiclo);
    }

    public List<GradoCiclosDTO> listarCiclosGrado(Long gradoId) {
        Grado grado = gradoRepository.findById(gradoId)
            .orElseThrow(() -> new ResourceNotFoundException("Grado", gradoId));
        
        return grado.getCicloGrado().stream()
            .map(ciclo -> GradoCiclosDTO.builder()
                .id(ciclo.getId())
                .año(ciclo.getAnio())
                .cantAlumnos(ciclo.getRegistros().size())
                .build())
            .toList();
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
}