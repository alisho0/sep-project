package dev.ale.sep_project.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import dev.ale.sep_project.models.Actividad;
import dev.ale.sep_project.repository.ActividadRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActividadService {
    private final ActividadRepository actividadRepository;

    public void registrarActividad(String descripcion, String tipo) {
        Actividad act = Actividad.builder()
            .descripcion(descripcion)
            .tipo(tipo)
            .fecha(LocalDateTime.now())
            .build();
        actividadRepository.save(act);
    }

    public List<Actividad> obtenerUltimas(int limite) {
        return actividadRepository.findAllByOrderByFechaDesc(PageRequest.of(0, limite)).getContent();
    }
}
