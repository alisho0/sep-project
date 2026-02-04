package dev.ale.sep_project.services;

import java.util.List;
import java.util.stream.Collectors;

import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
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
        } catch (DataAccessException e) {
            throw new BusinessLogicException("Error al crear una discapacidad - " + e);
        }
    }

    public List<DiscapacidadesListDTO> listarDiscapacidades() {
        return discapacidadRepository.findAll().stream()
                .map(d -> new DiscapacidadesListDTO(d.getId(), d.getNombre()))
                .collect(Collectors.toList());
    }


    public void borrarDiscapacidad(Long id) {
        Discapacidad dis = discapacidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("discapacidad", id));
        try {
            discapacidadRepository.delete(dis);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessLogicException("Error al eliminar una discapacidad" + e);
        }
    }
}
