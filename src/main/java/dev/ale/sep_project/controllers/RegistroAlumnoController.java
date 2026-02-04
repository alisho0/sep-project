package dev.ale.sep_project.controllers;

import dev.ale.sep_project.dtos.registros.RegistroAniosDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import dev.ale.sep_project.dtos.registros.RegistroCreateDTO;
import dev.ale.sep_project.dtos.registros.RegistroRespuestaDTO;
import dev.ale.sep_project.services.RegistroAlumnoService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/registro")
@RequiredArgsConstructor
public class RegistroAlumnoController {

    private final RegistroAlumnoService registroAlumnoService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearRegistro(@RequestBody RegistroCreateDTO registro) {
        RegistroAniosDTO r = registroAlumnoService.crearRegistro(registro);
        return ResponseEntity.ok(r);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> obtenerRegistro (@PathVariable Long id) {
        RegistroRespuestaDTO respuestaDTO = registroAlumnoService.obtenerDatosRegistro(id);
        return ResponseEntity.ok().body(respuestaDTO);
    }

    @GetMapping("/aniosDisponibles/{id}")
    public ResponseEntity<?> getAniosDisponibles(@PathVariable Long id) {
        return ResponseEntity.ok().body(registroAlumnoService.obtenerAniosDisponibles(id));
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarRegistro(@PathVariable Long id) throws Exception {
        registroAlumnoService.eliminarRegistro(id);
        return ResponseEntity.ok("Registro eliminado correctamente");
    }

    // Analizarlo más adelante
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<String> actualizarRegistro(@PathVariable Long id, @RequestBody RegistroCreateDTO registro) throws Exception {
        // registroAlumnoService.actualizarRegistro(id);
        return ResponseEntity.ok("Registro actualizado correctamente");
    }

}
