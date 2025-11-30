package dev.ale.sep_project.services;

import dev.ale.sep_project.repository.SeccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeccionService {
    private SeccionRepository seccionRepository;

    private void eliminarSeccion(Long id) {

    }
}
