package dev.ale.sep_project.services;

import org.springframework.stereotype.Service;

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
}
