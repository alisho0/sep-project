package dev.ale.sep_project.controllers;

import dev.ale.sep_project.dtos.usuarios.CambioPasswordDTO;
import dev.ale.sep_project.dtos.usuarios.UsuarioCompletoDTO;
import dev.ale.sep_project.dtos.usuarios.UsuarioEditarDTO;
import dev.ale.sep_project.dtos.usuarios.UsuarioResponseDTO;
import dev.ale.sep_project.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @GetMapping("/listar")
    public ResponseEntity<?> listUsuarios() {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Long id) {
        UsuarioCompletoDTO usuario = usuarioService.obtenerUsuarioCompleto(id);
        return ResponseEntity.ok(usuario);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> modificarUsuario(@PathVariable Long id, @RequestBody UsuarioEditarDTO usuario) {
        UsuarioResponseDTO usu = usuarioService.editarUsuario(id, usuario);
        return ResponseEntity.ok(usu);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'MAESTRO')")
    @PutMapping("/cambiarPassword/{id}")
    public ResponseEntity<?> modificarPassword(@PathVariable Long id, @RequestBody CambioPasswordDTO dto) {
            usuarioService.cambiarPassword(id, dto);
            return ResponseEntity.ok("Contraseña cambiada correctamente");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> delUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado correctamente.");
    }
}
