package dev.ale.sep_project.services;

import java.util.List;
import java.util.stream.Collectors;

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
    
    public void crearDiscapacidad(String nombre) {
        try {
            
            Discapacidad discapacidad = Discapacidad.builder()
                .nombre(nombre)
                .build();
            discapacidadRepository.save(discapacidad);
        } catch (Exception e) {
            new Exception("Error al crear una discapacidad" + e.getMessage());
        }
    }

    public List<DiscapacidadesListDTO> listarDiscapacidades() {
        try {
            List<Discapacidad> discapacidades = (List<Discapacidad>) discapacidadRepository.findAll();
            List<DiscapacidadesListDTO> discDto = discapacidades.stream()
                .map(d -> new DiscapacidadesListDTO(d.getId(), d.getNombre()))
                .collect(Collectors.toList());
            return discDto;
        } catch (BusinessLogicException e) {
            throw new BusinessLogicException("Error al listar discapacidades");
        }
    }
}
