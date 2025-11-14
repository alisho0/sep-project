package dev.ale.sep_project.services;

import java.util.List;
import java.util.stream.Collectors;

import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import dev.ale.sep_project.dtos.discapacidades.DiscapacidadesListDTO;
import dev.ale.sep_project.exceptions.BusinessLogicException;
import dev.ale.sep_project.models.Discapacidad;
import dev.ale.sep_project.repository.DiscapacidadRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscapacidadService {
    private final DiscapacidadRepository discapacidadRepository;
    
    public DiscapacidadesListDTO crearDiscapacidad(String nombre) {
        try {
            
            Discapacidad discapacidad = Discapacidad.builder()
                .nombre(nombre)
                .build();
            Discapacidad disc = discapacidadRepository.save(discapacidad);
            return new DiscapacidadesListDTO(disc.getId(), disc.getNombre());
        } catch (Exception e) {
            throw new BusinessLogicException("Error al crear una discapacidad");
        }
    }

    public List<DiscapacidadesListDTO> listarDiscapacidades() {
        try {
            List<Discapacidad> discapacidades = (List<Discapacidad>) discapacidadRepository.findAll();
            return discapacidades.stream()
                .map(d -> new DiscapacidadesListDTO(d.getId(), d.getNombre()))
                .collect(Collectors.toList());
        } catch (BusinessLogicException e) {
            throw new BusinessLogicException("Error al listar discapacidades");
        }
    }

    public void borrarDiscapacidad(Long id) {
        try {
            Discapacidad dis = discapacidadRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("discapacidad", id));
            discapacidadRepository.delete(dis);
        } catch (BusinessLogicException e) {
            throw new BusinessLogicException("Error al eliminar una discapacidad" + e.getMessage());
        }
    }
}
