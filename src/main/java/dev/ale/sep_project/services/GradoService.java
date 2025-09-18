package dev.ale.sep_project.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.ale.sep_project.dtos.grados.GradoDetalleDTO;
import dev.ale.sep_project.dtos.grados.GradoListaDTO;
import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import dev.ale.sep_project.models.Grado;
import dev.ale.sep_project.repository.GradoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GradoService {
    private final GradoRepository gradoRepository;
    private final CicloGradoService cicloGradoService;  // Inyectamos el servicio de ciclos

    public List<GradoListaDTO> listarGrados() {
        List<Grado> grados = (List<Grado>) gradoRepository.findAll();

        if (grados.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron los grados");
        }
        
        return grados.stream()
            .map(grado -> GradoListaDTO.builder()
                .grado(grado.getNroGrado())
                .seccion(grado.getSeccion())
                .turno(grado.getTurno())
                .id(grado.getId())
                .build())
            .toList();
    }

    public GradoDetalleDTO detalleGrado(Long id) {
        Grado grado = gradoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Grado", id));
        
        GradoListaDTO gradoDto = GradoListaDTO.builder()
            .id(grado.getId())
            .seccion(grado.getSeccion())
            .turno(grado.getTurno())
            .grado(grado.getNroGrado())
            .build();
        
        return GradoDetalleDTO.builder()
            .gradoListaDTO(gradoDto)
            .gradoCiclos(cicloGradoService.listarCiclosGrado(id))  // Usamos el servicio de ciclos
            .build();
    }

    // Métodos específicos de grado
    public Grado obtenerGrado(Long id) {
        return gradoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Grado", id));
    }

    // Aquí podrías agregar más métodos específicos de grado
    // como crear grado, actualizar grado, etc.
}

