package dev.ale.sep_project.controllers;

import java.util.List;

import dev.ale.sep_project.dtos.usuarios.ActividadDTO;
import dev.ale.sep_project.models.Usuario;
import dev.ale.sep_project.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.ale.sep_project.models.Actividad;
import dev.ale.sep_project.services.ActividadService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/actividad")
@RequiredArgsConstructor
public class ActividadController {
    private final ActividadService actividadService;
    private final UsuarioRepository usuarioRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @GetMapping("/recientes")
    public ResponseEntity<?> getUltimas(@RequestParam(defaultValue = "10") int limite, Authentication auth) {
        Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<ActividadDTO> actividades = actividadService.obtenerUltimas(limite, usuario);
        return ResponseEntity.ok(actividades);
    }
}
