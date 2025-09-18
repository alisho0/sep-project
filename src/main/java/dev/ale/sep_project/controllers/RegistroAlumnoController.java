package dev.ale.sep_project.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.ale.sep_project.dtos.registros.RegistroCreateDTO;
import dev.ale.sep_project.dtos.registros.RegistroRespuestaDTO;
import dev.ale.sep_project.services.RegistroAlumnoService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/registro")
@RequiredArgsConstructor
public class RegistroAlumnoController {

    private final RegistroAlumnoService registroAlumnoService;

    @PostMapping("/crear")
    public ResponseEntity<String> crearRegistro(@RequestBody RegistroCreateDTO registro) {
        registroAlumnoService.crearRegistro(registro);
        return ResponseEntity.ok("Registro creado correctamente");
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> obtenerRegistro (@PathVariable Long id) {
        RegistroRespuestaDTO respuestaDTO = registroAlumnoService.obtenerDatosRegistro(id);
        return ResponseEntity.ok().body(respuestaDTO);
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
