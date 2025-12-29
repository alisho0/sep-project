package dev.ale.sep_project.controllers;

import dev.ale.sep_project.dtos.usuarios.UsuarioResponseDTO;
import dev.ale.sep_project.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping("/listar")
    public ResponseEntity<?> listUsuarios() {
        try {
            List<UsuarioResponseDTO> usuarios = usuarioService.listarUsuarios();
            return ResponseEntity.ok(usuarios);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al listar los usuarios");
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> delUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok("Usuario eliminado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar usuario");
        }
    }
}
