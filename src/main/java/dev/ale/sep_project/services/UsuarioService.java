package dev.ale.sep_project.services;

import dev.ale.sep_project.dtos.usuarios.UsuarioCompletoDTO;
import dev.ale.sep_project.dtos.usuarios.UsuarioEditarDTO;
import dev.ale.sep_project.dtos.usuarios.UsuarioResponseDTO;
import dev.ale.sep_project.exceptions.ResourceNotFoundException;
import dev.ale.sep_project.models.Maestro;
import dev.ale.sep_project.models.Rol;
import dev.ale.sep_project.models.Usuario;
import dev.ale.sep_project.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
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
                        .rol(capitalizarEnum(u.getRol()))
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

    public UsuarioCompletoDTO obtenerUsuarioCompleto(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Maestro maestro = usuario.getMaestro();

        return UsuarioCompletoDTO.builder()
                .id(usuario.getId())
                .nombre(maestro != null ? maestro.getNombre() : "")
                .apellido(maestro != null ? maestro.getApellido() : "")
                .username(usuario.getUsername())
                .dni(maestro != null ? maestro.getDni() : "")
                .domicilio(maestro != null ? maestro.getDomicilio() : "")
                .rol(usuario.getRol() != null ? usuario.getRol().name() : "")
                .build();
    }

    @Transactional
    public UsuarioResponseDTO editarUsuario(Long id, UsuarioEditarDTO usuDTO) {
        Usuario usu = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        try {
            usu.setUsername(usuDTO.getUsername());
            usu.setRol(Rol.valueOf(usuDTO.getRol()));

            // Actualizar datos del maestro asociado
            Maestro maestro = usu.getMaestro();
            if (maestro != null) {
                maestro.setNombre(usuDTO.getNombre());
                maestro.setApellido(usuDTO.getApellido());
                maestro.setDni(usuDTO.getDni());
                maestro.setDomicilio(usuDTO.getDomicilio());
            }

            return UsuarioResponseDTO.builder()
                    .id(usu.getId())
                    .username(usu.getUsername())
                    .nombreCompleto(maestro.getNombre() + " " + maestro.getApellido())
                    .rol(usu.getRol().name())
                    .build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private String capitalizarEnum(Rol rol) {
        if (rol == null) return "";
        String nombre = rol.name().toLowerCase();
        return nombre.substring(0, 1).toUpperCase() + nombre.substring(1);
    }
}
