package dev.ale.sep_project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice // Intercepta las excepciones de los controladores
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class) // Especifica que excepción va a manejar
    @ResponseStatus(HttpStatus.NOT_FOUND) // El código HTTP que va a retornar
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity // La respuesta
            .status(HttpStatus.NOT_FOUND)
            .body(ex.getMessage());
    }

    @ExceptionHandler(BusinessLogicException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleBusinessLogic(BusinessLogicException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ex.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler({
        BadCredentialsException.class,
        DisabledException.class,
        LockedException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAuthenticationException(Exception ex) {
        String mensaje = "Error de autenticación: ";
        if (ex instanceof BadCredentialsException) {
            mensaje += "Credenciales inválidas";
        } else if (ex instanceof DisabledException) {
            mensaje += "Usuario deshabilitado";
        } else if (ex instanceof LockedException) {
            mensaje += "Usuario bloqueado";
        }
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(mensaje);
    }

    // Manejador para excepciones no controladas
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error interno del servidor: " + ex.getMessage());
    }
}