package dev.ale.sep_project.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import dev.ale.sep_project.dtos.usuarios.ActividadDTO;
import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import dev.ale.sep_project.models.Usuario;
import dev.ale.sep_project.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import dev.ale.sep_project.models.Actividad;
import dev.ale.sep_project.repository.ActividadRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActividadService {
    private final ActividadRepository actividadRepository;
    private final UsuarioRepository usuarioRepository;

    public void registrarActividad(String descripcion, String tipo) {

        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Long userId = claims.get("userId", Long.class);
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", userId));

        Actividad act = Actividad.builder()
            .descripcion(descripcion + " | Autor: " + usuario.getMaestro().getApellido() + " " + usuario.getMaestro().getNombre())
            .tipo(tipo)
            .fecha(LocalDateTime.now())
            .usuario(usuario)
            .build();
        actividadRepository.save(act);
    }

    public List<ActividadDTO> obtenerUltimas(int limite, Usuario usuario) {
        Pageable pageable = PageRequest.of(0, limite);

        if (usuario.getRol().name().equals("ADMIN")) {
            return actividadRepository.findAllByOrderByFechaDesc(pageable).getContent()
                    .stream()
                    .map(act -> new ActividadDTO(act.getId(),
                            act.getDescripcion(),
                            act.getTipo(),
                            act.getFecha(),
                            (act.getUsuario() != null ? act.getUsuario().getMaestro().getNombre() + " " + act.getUsuario().getMaestro().getApellido() : "Usuario desconocido")))
                    .collect(Collectors.toList());
        } else {
             return actividadRepository.findByUsuarioIdOrderByFechaDesc(usuario.getId(), pageable).getContent()
                     .stream()
                     .map(act -> new ActividadDTO(act.getId(),
                             act.getDescripcion(),
                             act.getTipo(),
                             act.getFecha(),
                             (act.getUsuario() != null ? act.getUsuario().getMaestro().getNombre() + " " + act.getUsuario().getMaestro().getApellido() : "Usuario desconocido")))
                     .collect(Collectors.toList());
        }
    }
}
