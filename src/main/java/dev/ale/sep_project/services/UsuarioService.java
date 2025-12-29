package dev.ale.sep_project.services;

import dev.ale.sep_project.dtos.usuarios.UsuarioResponseDTO;
import dev.ale.sep_project.models.Usuario;
import dev.ale.sep_project.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public List<UsuarioResponseDTO> listarUsuarios() {
        List<UsuarioResponseDTO> usuarios = usuarioRepository.findAll().stream()
                .map(u -> UsuarioResponseDTO.builder()
                        .id(u.getId())
                        .nombreCompleto(u.getMaestro().getNombre() + " " + u.getMaestro().getApellido())
                        .username(u.getUsername())
                        .rol(u.getRol().name().toUpperCase().substring(0, 1) + u.getRol().name().toLowerCase().substring(1))
                        .build())
                .toList();
        return usuarios;
    }

    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("El usuario con id " + id + " no existe");
        }
        usuarioRepository.deleteById(id);
    }
}
