package dev.ale.sep_project.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.ale.sep_project.dtos.grados.GradoListaDTO;
import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import dev.ale.sep_project.models.Grado;
import dev.ale.sep_project.repository.GradoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GradoService {
    private final GradoRepository gradoRepository;

    public List<GradoListaDTO> listarGrados() throws Exception {
        List<Grado> grados = (List<Grado>) gradoRepository.findAll();

        if (grados.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron los grados");
        }
        List<GradoListaDTO> gradoLista; // GradoListaDTO.builder() // Usar stream
        return null;
    }
}
