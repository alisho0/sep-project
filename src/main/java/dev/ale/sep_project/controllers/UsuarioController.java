package dev.ale.sep_project.controllers;

import dev.ale.sep_project.dtos.usuarios.UsuarioCompletoDTO;
import dev.ale.sep_project.dtos.usuarios.UsuarioEditarDTO;
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

    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Long id) {
        try {
            UsuarioCompletoDTO usuario = usuarioService.obtenerUsuarioCompleto(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al traer el usuario");
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> modificarUsuario(@PathVariable Long id, @RequestBody UsuarioEditarDTO usuario) {
        try {
            System.out.println("Lo que llega acá id: " + id);
            System.out.println("Lo que llega acá data: " + usuario);
            UsuarioResponseDTO usu = usuarioService.editarUsuario(id, usuario);
            return ResponseEntity.ok(usu);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al editar el usuario");
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
